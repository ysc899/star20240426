/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.seesoft.nemo.starnemo.ui.camera.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.media.AudioAttributes
import android.media.MediaActionSound
import android.media.MediaScannerConnection
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.Metadata
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.seesoft.nemo.starnemo.R
import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO
import kr.co.seesoft.nemo.starnemo.ui.camera.CameraMainActivity
import kr.co.seesoft.nemo.starnemo.ui.camera.KEY_EVENT_ACTION
import kr.co.seesoft.nemo.starnemo.ui.camera.KEY_EVENT_EXTRA
import kr.co.seesoft.nemo.starnemo.ui.camera.utils.ANIMATION_FAST_MILLIS
import kr.co.seesoft.nemo.starnemo.ui.camera.utils.ANIMATION_SLOW_MILLIS
import kr.co.seesoft.nemo.starnemo.ui.camera.utils.simulateClick
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomPhotoThumDialog
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil
import kr.co.seesoft.nemo.starnemo.util.Const
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.ArrayDeque
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

/**
 * Main fragment for this app. Implements all camera operations including:
 * - Viewfinder
 * - Photo taking
 * - Image analysis
 */
class CameraFragment : Fragment() {

    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var hospitalCd: String
    private lateinit var today: String

    //db 처리
    private lateinit var nemoRepository: NemoRepository

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    //셔터음 관련 처리
    private var cameraSoundFlag: Boolean = true
    private var cameraSoundTypeFlag: Boolean = true
    private var sound: MediaActionSound = MediaActionSound()

    lateinit var soundPool: SoundPool
    var soundResourceId by Delegates.notNull<Int>()


    //플래시 관련 처리
    private var cameraFlashMode: Int = Const.CAMERA_FLASH_MODE_OFF


    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private var isGoAlbum: Boolean = false

    private var cameraThumFlag: Boolean = false
    private var thumDialog: CustomPhotoThumDialog? = null




    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    /** Volume down button receiver used to trigger shutter */
    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                // When the volume down button is pressed, simulate a shutter button click
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    val shutter = container
                            .findViewById<ImageButton>(R.id.camera_capture_button)
                    shutter.simulateClick()
                }
            }
        }
    }

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CameraFragment.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    override fun onResume() {

        super.onResume()

        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                    CameraFragmentDirections.actionCameraToPermissions()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()

        // Unregister the broadcast receivers and listeners
        broadcastManager.unregisterReceiver(volumeDownReceiver)
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_camera, container, false)

    private fun setGalleryThumbnail(uri: Uri) {
        // Reference of the view that holds the gallery thumbnail
        val thumbnail = container.findViewById<ImageButton>(R.id.photo_view_button)

        // Run the operations in the view's thread
        thumbnail.post {

            // Remove thumbnail padding
            thumbnail.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())

            // Load thumbnail into circular button using Glide
            Glide.with(thumbnail)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail)
        }
    }

    private fun setPhotoThumbnail(uri: Uri) {

        var handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            thumDialog?.show(uri)
        }, 0);
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isGoAlbum) {

            container = view as ConstraintLayout
            viewFinder = container.findViewById(R.id.view_finder)

            hospitalCd = CameraMainActivity.hospitalCd.toString()
            today = CameraMainActivity.today.toString()

            thumDialog = CustomPhotoThumDialog(context)

            //db 관련
            nemoRepository = NemoRepository(view.context, hospitalCd, today)

            cameraSoundFlag = view.context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE).getBoolean(AndroidUtil.SP_CAMERA_SOUND, true)
            cameraSoundTypeFlag = view.context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE).getBoolean(AndroidUtil.SP_CAMERA_SOUND_TYPE, true)
            cameraThumFlag = view.context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE).getBoolean(AndroidUtil.SP_CAMERA_THUM, false)

            cameraFlashMode = view.context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE).getInt(AndroidUtil.SP_CAMERA_FLASH, Const.CAMERA_FLASH_MODE_OFF)



            val   audioAttributes    =  AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()

            soundPool = SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(2).build()


            soundResourceId = soundPool.load(context, R.raw.camera_shutter_click10, 1)

            soundPool.setOnLoadCompleteListener{soundPool, i, i2 -> AndroidUtil.log("로딩 완료") }


            // Initialize our background executor
            cameraExecutor = Executors.newSingleThreadExecutor()

            broadcastManager = LocalBroadcastManager.getInstance(view.context)

            // Set up the intent filter that will receive events from our main activity
            val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
            broadcastManager.registerReceiver(volumeDownReceiver, filter)

            // Every time the orientation of device changes, update rotation for use cases
            displayManager.registerDisplayListener(displayListener, null)

            // Determine the output directory
            outputDirectory = CameraMainActivity.getOutputDirectory(requireContext())

            // Wait for the views to be properly laid out
            viewFinder.post {
                // Keep track of the display in which this view is attached
                displayId = viewFinder.display.displayId

                // Build UI controls
                updateCameraUi()

                // Set up the camera and its use cases
                setUpCamera()

            }

            //앨범 바로 가기
            if (CameraMainActivity.goAlbum == true) {
                isGoAlbum = true
                if (true == outputDirectory.listFiles()?.isNotEmpty()) {
                    Navigation.findNavController(
                            requireActivity(), R.id.fragment_container
                    ).navigate(CameraFragmentDirections
                            .actionCameraToGallery(outputDirectory.absolutePath))
                }
            }
        }else{
            requireActivity().finish()
        }
    }

    /**
     * Inflate camera controls and update the UI manually upon config changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     *
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Redraw the camera UI controls
        updateCameraUi()

        // Enable or disable switching between cameras
        updateCameraSwitchButton()
    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // Enable or disable switching between cameras
            updateCameraSwitchButton()

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /** Declare and bind preview, capture and analysis use cases */
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

//        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val screenAspectRatio = AspectRatio.RATIO_4_3

        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = viewFinder.display.rotation

        Log.d(TAG, "rotation: $rotation")

        // CameraProvider
        val cameraProvider = cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()


        //플래시 관련 처리
        var fMode = ImageCapture.FLASH_MODE_ON
        if(cameraFlashMode != Const.CAMERA_FLASH_MODE_ON){
            fMode = ImageCapture.FLASH_MODE_OFF
        }



            // ImageCapture
        imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
//                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)

                //플래쉬 설정
//                .setFlashMode(ImageCapture.FLASH_MODE_ON)
                .setFlashMode(fMode)
                //해상도 설정 (화질 다운 시키는용)
                .setTargetResolution(Size(3000,4000 ))
//                .setTargetResolution(Size(2448,3264 ))
//                .setTargetResolution(Size(1932,2576 ))

                .build()

        // ImageAnalysis
        imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        // Values returned from our analyzer are passed to the attached listener
                        // We log image analysis results here - you should do something useful
                        // instead!
//                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer)



            //손전등 처리
            if (cameraFlashMode == Const.CAMERA_FLASH_MODE_ALL) {
                camera!!.cameraControl.enableTorch(true)
            } else {
                camera!!.cameraControl.enableTorch(false)
            }

            val exposureIndex = 10;
//            val exposureIndex = 5;

            if(CameraMainActivity.deviceModel == Const.DEVICE_A51) {

                camera!!.cameraControl.setExposureCompensationIndex(exposureIndex)
            }


            AndroidUtil.log("------------------------- 카메라 정보 ------------------------------")

            AndroidUtil.log( "lower : " + camera!!.cameraInfo.exposureState.exposureCompensationRange.lower);
            AndroidUtil.log( "upper : " + camera!!.cameraInfo.exposureState.exposureCompensationRange.upper);
            AndroidUtil.log( "index : " + camera!!.cameraInfo.exposureState.exposureCompensationIndex);

            AndroidUtil.log("------------------------- 카메라 정보 끝------------------------------")


            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    /**
     *  [androidx.camera.core.ImageAnalysisConfig] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** Method used to re-draw the camera UI controls, called every time configuration changes. */
    private fun updateCameraUi() {

        // Remove previous UI if any
        container.findViewById<ConstraintLayout>(R.id.camera_ui_container)?.let {
            container.removeView(it)
        }

        // Inflate a new view containing all UI for controlling the camera
        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        // In the background, load latest photo taken (if any) for gallery thumbnail
        lifecycleScope.launch(Dispatchers.IO) {
            outputDirectory.listFiles { file ->
                EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
            }?.max()?.let {
                setGalleryThumbnail(Uri.fromFile(it))
            }
        }

        controls.findViewById<PreviewView>(R.id.view_finder).setOnClickListener {
            val shutter = container
                    .findViewById<ImageButton>(R.id.camera_capture_button)
            shutter.simulateClick()
        }

        // Listener for button used to capture photo
        controls.findViewById<ImageButton>(R.id.camera_capture_button).setOnClickListener {

            // Get a stable reference of the modifiable image capture use case
            imageCapture?.let { imageCapture ->

                // Create output file to hold the image
                val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)

                // Setup image capture metadata
                val metadata = Metadata().apply {
                    // Mirror image when using the front camera
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                        .setMetadata(metadata)
                        .build()

                // Setup image capture listener which is triggered after photo has been taken
                imageCapture.takePicture(
                        outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        AndroidUtil.log("$hospitalCd : Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {


                        if(cameraSoundFlag) {
                            if(cameraSoundTypeFlag) {
                                sound.play(MediaActionSound.SHUTTER_CLICK)
                            }else {
                                soundPool.play(soundResourceId, 5f, 5f, 1, 0, 1f)
                            }
                        }

                        val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                        AndroidUtil.log("$hospitalCd : Photo capture succeeded: $savedUri")

                        val tf = File(savedUri.toString().removePrefix("file://"))

                        val b = BitmapFactory.decodeFile(tf.absolutePath)

                        val mat = Matrix()
                        mat.postRotate(90f)

                        if(CameraMainActivity.deviceModel == Const.DEVICE_A52) {
                            if(CameraMainActivity.deviceOs == Const.DEVECE_OS_12){

                                //a52s os12 ㄷㅐ응
                                    AndroidUtil.log("A52s os 12");
                                mat.preScale(0.70f, 0.70f)

                            }else {
                                mat.preScale(0.85f, 0.85f)
                            }
                        }else if(CameraMainActivity.deviceModel == Const.DEVICE_A53) {
                            mat.preScale(0.605f, 0.605f)

                        }else{
                            mat.preScale(0.70f, 0.70f)

                        }
                        val outB = Bitmap.createBitmap(b, 0, 0, b.width, b.height, mat, false)
                        tf.delete()

                        val fos = FileOutputStream(tf)
                        outB.compress(Bitmap.CompressFormat.JPEG, 80, fos)

                        val vo = PictureVO();
                        vo.ymd = today
                        vo.hospitalKey = hospitalCd
                        vo.sendFlag = false
                        vo.filePath = savedUri.toString().removePrefix("file://")

                        nemoRepository.insert(vo);

                        b.recycle()
                        outB.recycle()
                        fos.close()


                        setGalleryThumbnail(savedUri)

                        if(cameraThumFlag) {
                            setPhotoThumbnail(savedUri)
                        }


                        // If the folder selected is an external media directory, this is
                        // unnecessary but otherwise other apps will not be able to access our
                        // images unless we scan them using [MediaScannerConnection]
                        val mimeType = MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension(savedUri.toFile().extension)

                        MediaScannerConnection.scanFile(
                                context,
                                arrayOf(tf.absolutePath),
//                                arrayOf(savedUri.toFile().absolutePath),
                                arrayOf(mimeType)
                        ) { _, uri ->
                            AndroidUtil.log(  "$hospitalCd : Image capture scanned into media store: $uri")
                        }
                    }
                })

                // We can only change the foreground Drawable using API level 23+ API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Display flash animation to indicate that photo was captured
                    container.postDelayed({
                        container.foreground = ColorDrawable(Color.WHITE)
                        container.postDelayed(
                                { container.foreground = null }, ANIMATION_FAST_MILLIS)
                    }, ANIMATION_SLOW_MILLIS)
                }
            }
        }



        controls.findViewById<TextView>(R.id.tvCameraCount).let {
            var tv = it as TextView
            nemoRepository.hospitalYmdDatas.observe(viewLifecycleOwner, Observer {
                tv.setText("촬영 건수 : "+ it.size)
            })
        }

        // Setup for button used to switch cameras
        controls.findViewById<ImageButton>(R.id.camera_switch_button).let {

            // Disable the button until the camera is set up
            it.isEnabled = false


            if(cameraFlashMode == Const.CAMERA_FLASH_MODE_OFF) {
                it.setImageResource(R.drawable.flash_icon_off)
            }else{
                it.setImageResource(R.drawable.flash_icon_on)
            }


            // Listener for button used to switch cameras. Only called if the button is enabled
            it.setOnClickListener {
                cameraFlashMode++

                if(cameraFlashMode == Const.CAMERA_FLASH_MODE_CLEAR){
                    cameraFlashMode = Const.CAMERA_FLASH_MODE_OFF
                }

                val itIv: ImageView = it as ImageView


                if(cameraFlashMode == Const.CAMERA_FLASH_MODE_ON){
                    itIv.setImageResource(R.drawable.flash_icon_on)
                }else if(cameraFlashMode == Const.CAMERA_FLASH_MODE_OFF){
                    itIv.setImageResource(R.drawable.flash_icon_off)
                }else if(cameraFlashMode == Const.CAMERA_FLASH_MODE_ALL){
//                    itIv.setImageResource(R.drawable.flashlight_icon_on)
                    itIv.setImageResource(R.drawable.flash_icon_on)
                }


                val sharedPref = context?.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                if (editor != null) {
                    editor.putInt(AndroidUtil.SP_CAMERA_FLASH, cameraFlashMode)
                    editor.commit()
                }

//                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
//                    CameraSelector.LENS_FACING_BACK
//                } else {
//                    CameraSelector.LENS_FACING_FRONT
//                }
//                // Re-bind use cases to update selected camera
                bindCameraUseCases()
            }
        }

        // Listener for button used to view the most recent photo
        controls.findViewById<ImageButton>(R.id.photo_view_button).setOnClickListener {
            // Only navigate when the gallery has photos
            if (true == outputDirectory.listFiles()?.isNotEmpty()) {
                Navigation.findNavController(
                        requireActivity(), R.id.fragment_container
                ).navigate(CameraFragmentDirections
                        .actionCameraToGallery(outputDirectory.absolutePath))
            }
        }
    }

    /** Enabled or disabled a button to switch cameras depending on the available cameras */
    private fun updateCameraSwitchButton() {
        val switchCamerasButton = container.findViewById<ImageButton>(R.id.camera_switch_button)
        try {
            switchCamerasButton.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            switchCamerasButton.isEnabled = false
        }
    }

    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    /**
     * Our custom image analysis class.
     *
     * <p>All we need to do is override the function `analyze` with our desired operations. Here,
     * we compute the average luminosity of the image by looking at the Y plane of the YUV frame.
     */
    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * Used to add listeners that will be called with each luma computed
         */
        fun onFrameAnalyzed(listener: LumaListener) = listeners.add(listener)

        /**
         * Helper extension function used to extract a byte array from an image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        /**
         * Analyzes an image to produce a result.
         *
         * <p>The caller is responsible for ensuring this analysis method can be executed quickly
         * enough to prevent stalls in the image acquisition pipeline. Otherwise, newly available
         * images will not be acquired and analyzed.
         *
         * <p>The image passed to this method becomes invalid after this method returns. The caller
         * should not store external references to this image, as these references will become
         * invalid.
         *
         * @param image image being analyzed VERY IMPORTANT: Analyzer method implementation must
         * call image.close() on received images when finished using them. Otherwise, new images
         * may not be received or the camera may stall, depending on back pressure setting.
         *
         */
        override fun analyze(image: ImageProxy) {
            // If there are no listeners attached, we don't need to perform analysis
            if (listeners.isEmpty()) {
                image.close()
                return
            }

            // Keep track of frames analyzed
            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)

            // Compute the FPS using a moving average
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            // Analysis could take an arbitrarily long amount of time
            // Since we are running in a different thread, it won't stall other use cases

            lastAnalyzedTimestamp = frameTimestamps.first

            // Since format in ImageAnalysis is YUV, image.planes[0] contains the luminance plane
            val buffer = image.planes[0].buffer

            // Extract image data from callback object
            val data = buffer.toByteArray()

            // Convert the data into an array of pixel values ranging 0-255
            val pixels = data.map { it.toInt() and 0xFF }

            // Compute average luminance for the image
            val luma = pixels.average()

            // Call all listeners with new value
            listeners.forEach { it(luma) }

            image.close()
        }
    }

    companion object {

        private const val TAG = "hopalt"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
                File(baseFolder, SimpleDateFormat(format, Locale.KOREA)
                        .format(System.currentTimeMillis()) + extension)
    }
}
