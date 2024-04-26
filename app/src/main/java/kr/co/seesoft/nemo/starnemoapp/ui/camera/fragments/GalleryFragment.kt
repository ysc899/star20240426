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

package kr.co.seesoft.nemo.starnemoapp.ui.camera.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.media.MediaScannerConnection
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import kr.co.seesoft.nemo.starnemoapp.BuildConfig
import kr.co.seesoft.nemo.starnemoapp.R
import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository
import kr.co.seesoft.nemo.starnemoapp.ui.camera.CameraMainActivity
import kr.co.seesoft.nemo.starnemoapp.ui.camera.utils.padWithDisplayCutout
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.ConfirmDialog
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil
import kr.co.seesoft.nemo.starnemoapp.util.Const
import java.io.File
import java.util.*
import java.util.stream.Collectors


val EXTENSION_WHITELIST = arrayOf("JPG")

/** Fragment used to present the user with a gallery of photos taken */
class GalleryFragment internal constructor() : Fragment() {

    /** AndroidX navigation arguments */
    private val args: GalleryFragmentArgs by navArgs()

    //db 처리
    private lateinit var pictureRepository: NemoRepository

    private lateinit var mediaList: MutableList<File>

    private lateinit var confirmDialog: ConfirmDialog
    private lateinit var deleteHandler: Handler

    private var isFirst: Boolean = true

    /** Adapter class used to present a fragment containing one photo or video as a page */
    inner class MediaPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = mediaList.size
        override fun getItem(position: Int): Fragment = PhotoFragment.create(mediaList[position])
        override fun getItemPosition(obj: Any): Int = POSITION_NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mark this as a retain fragment, so the lifecycle does not get restarted on config change
        retainInstance = true

        // Get root directory of media from navigation arguments
        val rootDirectory = File(args.rootDirectory)

        //db 관련
        pictureRepository = NemoRepository(context, CameraMainActivity.hospitalCd.toString(), CameraMainActivity.today.toString())


        // Walk through all files in the root directory
        // We reverse the order of the list to present the last photos first
        mediaList = rootDirectory.listFiles { file ->
            EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT)) && file.length() > 0
                    //&& fileList.contains(file.name)
        }?.sortedDescending()?.toMutableList() ?: mutableListOf()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidUtil.log("사진 isFirst : " + isFirst);

        //Checking media files list
        if (mediaList.isEmpty()) {
            view.findViewById<ImageButton>(R.id.delete_button).isEnabled = false
            view.findViewById<ImageButton>(R.id.share_button).isEnabled = false
        }

        // Populate the ViewPager and implement a cache of two media items
        val mediaViewPager = view.findViewById<ViewPager>(R.id.photo_view_pager).apply {
            offscreenPageLimit = 2
            adapter = MediaPagerAdapter(childFragmentManager)
        }

        //가끔 디비에 파일 정보가 저장 안된 경우가 있어서 보정 처리
        pictureRepository.hospitalYmdDatas.observe(viewLifecycleOwner, Observer {

            var fileList = it.stream().map { it.filePath }.collect(Collectors.toList())

            var removeFiles =ArrayList<File>()

            mediaList.forEach {
                if(!fileList.contains(it.path)){
                    removeFiles.add(it)
                }
            };

            if(removeFiles.size > 0) {
                mediaList.removeAll(removeFiles);
                mediaViewPager.adapter?.notifyDataSetChanged()
            }

            if( isFirst )
            {
                val fileName = arguments?.getString("file_name", "");

                var tFile = mediaList.stream().filter{ it.path.equals(fileName)}.findFirst().orElse(null)
                if(tFile!= null){
//                AndroidUtil.log("카운트 : " + mediaList.indexOf(tFile));
                    mediaViewPager.currentItem = mediaList.indexOf(tFile);
                }
            }

            isFirst = false

        })


        // Make sure that the cutout "safe area" avoids the screen notch if any
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use extension method to pad "inside" view containing UI using display cutout's bounds
            view.findViewById<ConstraintLayout>(R.id.cutout_safe_area).padWithDisplayCutout()
        }

        // Handle back button press
        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigateUp()
        }

        // Handle share button press
        view.findViewById<ImageButton>(R.id.share_button).setOnClickListener {

            AndroidUtil.log("share_button Click")

            mediaList.getOrNull(mediaViewPager.currentItem)?.let { mediaFile ->

                //AndroidUtil.log("absolutePath :" + mediaFile.absolutePath)
                //AndroidUtil.log(mediaFile.absoluteFile)

                val mediaType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(mediaFile.extension)
                // Get URI from our FileProvider implementation
                val uri = FileProvider.getUriForFile(
                    view.context, BuildConfig.APPLICATION_ID + ".provider", mediaFile)

                // Create a sharing intent
                val intent = Intent().apply {
                    // Infer media type from file extension

                    // Set the appropriate intent extra, type, action and flags
                    putExtra(Intent.EXTRA_STREAM, uri)
                    action = Intent.ACTION_SEND
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                }.setDataAndType(uri,mediaType)

                // Launch the intent letting the user choose which app to share with
                startActivity(Intent.createChooser(intent, getString(R.string.share_hint)))
            }
        }

        // Handle delete button press
        view.findViewById<ImageButton>(R.id.delete_button).setOnClickListener {

            confirmDialog = ConfirmDialog(activity, deleteHandler)

            confirmDialog.show()
            confirmDialog.setMsg("삭제 하시겠습니까?")


        }

        deleteHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    Const.HANDLER_CONFIRM ->
                        mediaList.getOrNull(mediaViewPager.currentItem)?.let { mediaFile ->

                            var path = mediaFile.absolutePath

                            // Delete current photo
                            mediaFile.delete()

                            //DB 사진 데이터 삭제 관련 처리
                            pictureRepository.deleteByHospitalAndYmdAndPath(CameraMainActivity.hospitalCd.toString(), CameraMainActivity.today.toString(), path);

                            // Send relevant broadcast to notify other apps of deletion
                            MediaScannerConnection.scanFile(
                                view.context, arrayOf(mediaFile.absolutePath), null, null)

                            // Notify our view pager
                            mediaList.removeAt(mediaViewPager.currentItem)
                            mediaViewPager.adapter?.notifyDataSetChanged()

                            // If all photos have been deleted, return to camera
                            if (mediaList.isEmpty()) {
                                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigateUp()
                            }
                        }
                }
            }
        }

    }
}
