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

package kr.co.seesoft.nemo.starnemoapp.ui.camera

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent
import android.os.Build
import android.widget.FrameLayout
import kr.co.seesoft.nemo.starnemoapp.R
import kr.co.seesoft.nemo.starnemoapp.ui.camera.utils.FLAGS_FULLSCREEN
import kr.co.seesoft.nemo.starnemoapp.util.Const

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L

/**
 * Main entry point into our app. This app follows the single-activity pattern, and all
 * functionality is implemented in the form of fragments.
 */
class CameraMainActivity : AppCompatActivity() {
    private lateinit var container: FrameLayout

    val hospitalCd by lazy {
        intent.getStringExtra(Const.HOSPITAL_CD).toString()
    }
    val today by lazy {
        intent.getStringExtra(Const.TODAY).toString()
    }
    val goAlbum by lazy {
        intent.getBooleanExtra(Const.GO_ALBUM, false)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_main)
        container = findViewById(R.id.fragment_container)


        CameraMainActivity.hospitalCd = hospitalCd
        CameraMainActivity.today = today
        CameraMainActivity.goAlbum = goAlbum

        if(Build.MODEL.startsWith(Const.DEVICE_A52_MODEL_ID)){
            CameraMainActivity.deviceModel = Const.DEVICE_A52
        }else if(Build.MODEL.startsWith(Const.DEVICE_A51_MODEL_ID)){
            CameraMainActivity.deviceModel = Const.DEVICE_A51
        }else if(Build.MODEL.startsWith(Const.DEVICE_A53_MODEL_ID)){
            CameraMainActivity.deviceModel = Const.DEVICE_A53
        }else{
            CameraMainActivity.deviceModel = Const.DEVICE_A90
        }

        CameraMainActivity.deviceOs = Integer.valueOf(Build.VERSION.RELEASE.toString())



    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        container.postDelayed({
            container.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }



    companion object {

        var hospitalCd: String? = null
        var today: String? = null
        var goAlbum: Boolean? = false

        var deviceModel: Int?= 0
        var deviceOs: Int?= 0

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {

            val appContext = context.applicationContext
//            context.getExternalFilesDir(appContext.resources.getString(R.string.app_name))



            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)+File.separator + today + File.separator + hospitalCd).apply { mkdirs() } }

//            val mediaDir = context.getExternalFilesDirs(appContext.resources.getString(R.string.app_name)).firstOrNull()?.let {
//                File(it, today + File.separator + hospitalCd).apply { mkdirs() } }


            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}
