package kr.co.seesoft.nemo.starnemo.ui.setting;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.seesoft.nemo.starnemo.R;
import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemo.logapi.po.SendLogoPO;
import kr.co.seesoft.nemo.starnemo.logapi.service.LogCollectorAPI;
import kr.co.seesoft.nemo.starnemo.ui.MainMenuActivity;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.DateUtil;

public class SettingViewModel extends AndroidViewModel {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    //시리얼 넘버
    private MutableLiveData<String> serialNumber;

    private MutableLiveData<Boolean> autoLogin, cameraSound, autoSend, cameraThum, cameraSoundType;

    private Context context;


    private String userId;


    private MutableLiveData<Boolean> progressFlag;

//    private CustomProgressDialog progressDialog;

    private LogCollectorAPI api;
    private Handler apiHandler;



    //로그 전송용 관련
    /** db */
    private NemoRepository nemoRepository;
    /** context */
    private Application application;

    private LiveData<List<PictureVO>> allDatas;

    private boolean getDatasFlag = false;


    public SettingViewModel(Application application) {
        super(application);
        context = application;
        this.application = application;


        sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        editor = sp.edit();

        serialNumber = new MutableLiveData<>();
        serialNumber.setValue(sp.getString(AndroidUtil.SP_SECURE_TAG, ""));

        autoLogin = new MutableLiveData<>();
        autoLogin.setValue(sp.getBoolean(AndroidUtil.SP_LOGIN_AUTO, false));

        cameraSound = new MutableLiveData<>();
        cameraSound.setValue(sp.getBoolean(AndroidUtil.SP_CAMERA_SOUND, true));

        cameraSoundType = new MutableLiveData<>();
        cameraSoundType.setValue(sp.getBoolean(AndroidUtil.SP_CAMERA_SOUND_TYPE, true));

        autoSend = new MutableLiveData<>();
        autoSend.setValue(sp.getBoolean(AndroidUtil.SP_ERROR_AUTO_SEND, true));

        cameraThum = new MutableLiveData<>();
        cameraThum.setValue(sp.getBoolean(AndroidUtil.SP_CAMERA_THUM, false));

        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");



        api = new LogCollectorAPI(application);

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:

                        break;
                }
            }
        };

    }

    public MutableLiveData<String> getSerialNumber() {
        return serialNumber;
    }

    public MutableLiveData<Boolean> getAutoLogin() {
        return autoLogin;
    }

    public MutableLiveData<Boolean> getCameraSound() {
        return cameraSound;
    }

    public MutableLiveData<Boolean> getCameraSoundType() {
        return cameraSoundType;
    }

    public MutableLiveData<Boolean> getAutoSend() {
        return autoSend;
    }

    public MutableLiveData<Boolean> getCameraThum() {
        return cameraThum;
    }

    public LiveData<List<PictureVO>> getAllDatas() {
        return allDatas;
    }


    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public boolean isGetDatas() {
        return getDatasFlag;
    }

    public void setGetDatasFlag(boolean getDatasFlag) {
        this.getDatasFlag = getDatasFlag;
    }

    public void saveInfo(boolean autoLoginFlag, boolean cameraSoundFlag, boolean autoSendFlag, boolean cameraThumFlag, boolean cameraSoundTypeFlag){
        editor.putBoolean(AndroidUtil.SP_LOGIN_AUTO, autoLoginFlag);
        editor.putBoolean(AndroidUtil.SP_CAMERA_SOUND, cameraSoundFlag);
        editor.putBoolean(AndroidUtil.SP_ERROR_AUTO_SEND, autoSendFlag);
        editor.putBoolean(AndroidUtil.SP_CAMERA_THUM, cameraThumFlag);
        editor.putBoolean(AndroidUtil.SP_CAMERA_SOUND_TYPE, cameraSoundTypeFlag);

        if(!autoLoginFlag){
            editor.putString(AndroidUtil.SP_LOGIN_PW, "");
        }

        editor.commit();

        AndroidUtil.toast(context, "저장되었습니다.");
    }




    public void setNemoRepository(){
        this.nemoRepository = new NemoRepository(application, true);
        this.allDatas = this.nemoRepository.findAll();
        
        System.out.println("디비 설정 완료");

    }

    public List<PictureVO> getAllPictureData(){
        return this.allDatas.getValue();
    }



    public void collectLogDatas(){


        progressFlag.setValue(true);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{


            try {
                File starDir = null;

                AndroidUtil.log("파일 정보 시작");
                if(context.getExternalMediaDirs().length > 0){

                    File medParentDir = context.getExternalMediaDirs()[0];
                    File targetDir = new File(medParentDir, context.getResources().getString(R.string.app_name));

                    starDir = Arrays.asList(targetDir).stream().findFirst().orElse(null);

                    checkChildFile(starDir);
                }

                AndroidUtil.log("파일 정보 끝");


                AndroidUtil.log("디비 정보 시작");

                getAllPictureData().forEach(it ->{
                    AndroidUtil.log(it.toString());
                });

                AndroidUtil.log("디비 정보 끝");

                File logDir = new File(Environment.getExternalStorageDirectory()+ File.separator + "Android"+File.separator+"data"+ File.separator +"kr.co.seesoft.nemo.starnemo"+ File.separator +"log");

                AndroidUtil.log(logDir.getAbsolutePath());

                if(!logDir.exists()){
                    logDir.mkdirs();
                }


                File zipFolder = new File( starDir, DateUtil.getFormatString(new Date(), "yyyyMMdd"));
                if(!zipFolder.exists()){
                    zipFolder.mkdirs();
                }

                File zipLog = new File(zipFolder, userId +"_log_" + DateUtil.getFormatString(new Date(), "yyyyMMddHHmmss") + ".zip");

                ZipFile zipFile = new ZipFile(zipLog);


                zipFile.addFolder(logDir);

                SendLogoPO param = new SendLogoPO();
                param.setUserId(userId);
                param.setLogFile(zipLog);

                api.sendLog(param, apiHandler);




            } catch (Exception e) {

                apiHandler.sendEmptyMessage(500);

                e.printStackTrace();
            }

//            AndroidUtil.log( starDir.getAbsolutePath());




            executorService.shutdown();
        });



    }

    private void checkChildFile(File targetDir){
        if(targetDir != null){

            if(!targetDir.exists()){
                return;
            }

            if(targetDir.isDirectory()){
                Arrays.asList(targetDir.listFiles()).stream().forEach(t -> {
                    this.checkChildFile(t);
                });
            }else {

                AndroidUtil.log(targetDir.getAbsolutePath() + " : " + DateUtil.getFormatString(targetDir.lastModified(), "yyyy-MM-dd HH:mm:ss") + " : " + targetDir.length());

            }

        }
    }

}