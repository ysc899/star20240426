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

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kr.co.seesoft.nemo.starnemo.R
import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageAddPO
import kr.co.seesoft.nemo.starnemo.nemoapi.service.NemoAPI
import kr.co.seesoft.nemo.starnemo.ui.camera.CameraMainActivity
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomErrorDialog
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomProgressDialog
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/** Fragment used for each individual page showing a photo inside of [GalleryFragment] */
class PhotoFragment internal constructor() : Fragment() {

    private lateinit var hospitalCd: String
    private lateinit var today: String
    private lateinit var userId: String
    private lateinit var deptCode: String
    private lateinit var upprDeptCd: String

    //db 처리
    private lateinit var pictureRepository: NemoRepository

    //사진 정보
    private lateinit var picInfo: PictureVO

    //재전송 관련
    private var progressDialog: CustomProgressDialog? = null
    private var errorDialog: CustomErrorDialog? = null
    private lateinit var api: NemoAPI
    private lateinit var apiHandler: Handler
    private lateinit var reSendExecutor: ExecutorService



//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?) = ImageView(context)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.adapter_gallery_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return
        val resource = args.getString(FILE_NAME_KEY)?.let { File(it) } ?: R.drawable.ic_photo

        iv = view.findViewById(R.id.ivAdGalleryImage);

        Glide.with(view).load(resource).into(iv);
//        Glide.with(view).load(resource).into(view.findViewById(R.id.ivAdGalleryImage))
//        Glide.with(view).load(resource).into(view as ImageView)


//        PhotoViewAttacher(iv)

        hospitalCd = CameraMainActivity.hospitalCd.toString()
        today = CameraMainActivity.today.toString()
        //db 관련
        pictureRepository = NemoRepository(view.context, hospitalCd, today);

        progressDialog = CustomProgressDialog(context)
        errorDialog = CustomErrorDialog(context)
        reSendExecutor = Executors.newSingleThreadExecutor()
        api = NemoAPI(context)

        val sp: SharedPreferences = requireContext().getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE)
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "").toString()
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "").toString()
        upprDeptCd = sp.getString(AndroidUtil.SP_LOGIN_UPPR_DEPT, "").toString()

        var fileName = args.getString(FILE_NAME_KEY)

        val tvTotal = view.findViewById<TextView>(R.id.tvAdGalleryTotal)
        val tvSend = view.findViewById<TextView>(R.id.tvAdGallerySend)

        pictureRepository.hospitalYmdDatas.observe(viewLifecycleOwner, Observer {

            var totalCount = it.size
            picInfo = it.stream().filter({ it.filePath.equals(fileName) }).findFirst().orElse(null)

            var btText = "전송"

            if(picInfo != null){

                var idx = (it.indexOf(picInfo) + 1);
                tvTotal.setText( idx.toString() + " / " +  totalCount.toString())
                if(picInfo.sendFlag){
                    btText = "재전송"

                    if(StringUtils.isNotEmpty(picInfo.saveFileName)){
//                        tvFileName.setText(picInfo.saveFileName)

                        tvSend.setText("전송 완료 ("+ picInfo.saveFileName + ")")

//                        var a = picInfo.saveFileName.split("_")
//                        if(a.size > 3){
//                            tvSend.setText("전송 완료 ("+ a[2]+ ")")
//                        }else{
//                            tvSend.setText("전송 완료")
//                        }

                    }else{
//                        tvFileName.setText("")
                        tvSend.setText("전송 완료")
                    }


                }else{
                    tvSend.setText("미전송")
                }
            }

            view.findViewById<Button>(R.id.btReSend).setText(btText)
        })

        view.findViewById<Button>(R.id.btReSend).let {


            it.setOnClickListener(View.OnClickListener {
                progressDialog!!.show()

                reSendExecutor.execute {
                    var f = File(picInfo.filePath)

                    AndroidUtil.log("사진 상세 화면 전송 클릭 :  " + picInfo.toString())

                    try {
                        val fileContent = FileUtils.readFileToByteArray(f)
                        val imageBase64 = Base64.getEncoder().encodeToString(fileContent)
                        val param = NemoImageAddPO()

                        param.userId = userId
                        param.custCd = hospitalCd
                        param.deptCd = deptCode
                        param.smplTakePlanDt = today
                        param.upprDeptCd = upprDeptCd
                        param.imageBase64 = imageBase64
                        var isLast = true

                        val listParam = ArrayList<NemoImageAddPO>()
                        listParam.add(param)

                        val result: NemoResultImageAddRO = api.addSyncRegisterImage(listParam)

                        AndroidUtil.log("NemoResultImageAddRO KT : $result")

                        var fName = ""

                        if (result != null) {
                            val messageId: String = result.getMessageId()
                            if ("00020" == messageId) {
                                val arrFName: java.util.ArrayList<String> = result.getResult()
                                if (arrFName.size > 0) {
                                    fName = arrFName.get(0)
                                }
                            }
                        }

                        if (StringUtils.isNotEmpty(fName)) {

                            picInfo.sendFlag = true;
                            picInfo.saveFileName = fName;
                            pictureRepository.update(picInfo);

                            AndroidUtil.log("사진 상세 화면 전송 완료 :  " + picInfo.toString())

                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(context)
                                builder.setMessage("전송이 완료 되었습니다.")
                                        .setCancelable(true)
                                        .setPositiveButton("확인") { dialog, id -> // FIRE ZE MISSILES!
                                            dialog.dismiss()
                                        }

                                builder.create()
                                builder.show()

                            }, 0)

                        } else {

                            AndroidUtil.log("사진 상세 화면 전송 실패 (서버 결과 없음) :  " + picInfo.toString())

                            Handler(Looper.getMainLooper()).postDelayed({
                                errorDialog!!.show()
                            }, 0)
                        }
                    } catch (e: IOException) {
                        AndroidUtil.log("에러에러")
                        AndroidUtil.log("사진 상세 화면 전송 에러 ",  e)
                        e.printStackTrace()
                    }finally {
                        reSendExecutor.shutdown()
                    }
                }

            })
        }

    }




    companion object {

        lateinit var iv : ImageView

        private const val FILE_NAME_KEY = "file_name"

        fun create(image: File) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putString(FILE_NAME_KEY, image.absolutePath)
            }
        }

    }
}