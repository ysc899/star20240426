package kr.co.seesoft.nemo.starnemoapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


import java.io.File;
import java.io.IOException;

import kr.co.seesoft.nemo.starnemoapp.BuildConfig;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoEmptyListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoLoginPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoAppInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoLoginRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.ConfirmDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

/**
 * 로그인 화면
 */
public class LoginActivity extends AppCompatActivity {

    protected Context context;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Gson gson;
    /** 통합 api 인터페이스 */
    protected NemoAPI api;
    /** api 콜백용 */
    private Handler LoginAPIHandler, appVerAPIHandler , updateHandler;
    // 위젯 맴버 변수
    protected EditText etId, etPw;
    protected ImageButton btnLogin;
    protected CheckBox swLogin;
    protected TextView tvVersion;
    // 위젯 맴버 변수

    //권한 확인용
    private int REQUEST_CODE_PERMISSIONS = 10; //arbitrary number, can be changed accordingly
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH" };

    // android 분기 처리
    // 참고 : https://lasselindh.tistory.com/1
    private final boolean IS_REAL = !BuildConfig.DEBUG;

    private ConfirmDialog confirmDialog;
    private String urlNewVersion = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        if(allPermissionsGranted()){
            init();
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initUI() {
        context = this;
        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        swLogin = (CheckBox) findViewById(R.id.cbLogin);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
    }

    private void init() {


//        findViewById(R.id.ivLoginIdIco).setOnClickListener(click);
//        findViewById(R.id.ivLoginPwIco).setOnClickListener(click);


        tvVersion.setOnLongClickListener(new BtnLongClick());

        sp = getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        editor = sp.edit();

        gson = new Gson();

        //시큐어 코드 확인
        String secures = sp.getString(AndroidUtil.SP_SECURE_TAG, "");

        if (StringUtils.isEmpty(secures)) {

//            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NeoDIN"+ File.separator + "serial.txt";
//            File sFile = new File(filePath);
//            if(sFile.isFile()){
//                //파일에 있는거 불러들이기
//                try {
//                    secures = FileUtils.readFileToString(sFile, "UTF-8");
//                } catch (IOException e) {
//                    secures = getDevicesUUID();
//                }
//
//            }else {
//                //시큐어 코드 생성
//                secures = getDevicesUUID();
//            }

            //시큐어 코드 생성
            secures = getDevicesUUID();

            editor.putString(AndroidUtil.SP_SECURE_TAG, secures);
            editor.commit();
        }
        //시큐어 코드 확인 끝

        //앱 버전 표시
        tvVersion.setText(AndroidUtil.getVersionName(context));
        //앱 버전 표시 끝


        //API 설정
        api = new NemoAPI(context);
        LoginAPIHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:
                    case 201:
                        //통신 완료
                        NemoLoginRO result = (NemoLoginRO)msg.obj;
                        checkLoginInfo(result);
                        break;
                    default:
                    case 0:
                        AndroidUtil.toast(context, "전산실에 문의 부탁드립니다.");
                        break;

                }
            }
        };

        appVerAPIHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:
                    case 201:
                        //통신 완료
                        NemoAppInfoRO result = (NemoAppInfoRO)msg.obj;

                        AndroidUtil.log("NemoAppInfoRO : " + result);

                        String env = result.getEnv();
                        tvVersion.setText(env + " " + AndroidUtil.getVersionName(context));
                        versionCheck(result);

                        break;
                    default:
                    case 0:
                        //AndroidUtil.toast(context, "전산실에 문의 부탁드립니다.");
                        break;

                }
            }
        };

        updateHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_CONFIRM:

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNewVersion));

                        startActivity(intent);

                        exitProgram();

                        break;
                }
            }
        };

        //API 설정 끝

        //세션 정보 초기화
        editor.putString(AndroidUtil.SP_LOGIN_SESSION_INFO, "");
        editor.commit();



        getAppVersion();

    }

    private void versionCheck(NemoAppInfoRO result)
    {
        String nowVer = AndroidUtil.getVersionName(context);

        if( nowVer.equals( result.getVer() ))
        {
            autoLogin();
        }
        else
        {
            //autoLogin();

            urlNewVersion = result.getFileUrl();

            confirmDialog = new ConfirmDialog(context, updateHandler);

            confirmDialog.show();
            confirmDialog.setMsg(Const.STR_NEW_VERSION);

            confirmDialog.setBtnCancelGone();
            confirmDialog.setCancelable(false);

        }

    }

    private void exitProgram() {

        try
        {
            Thread.sleep(3000);
        }
        catch (Exception ex)
        {

        }



        // 종료

        // 태스크를 백그라운드로 이동
        // moveTaskToBack(true);



        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            finishAndRemoveTask();
        } else {
            // 액티비티 종료
            finish();
        }

        System.exit(0);
    }

    private void autoLogin()
    {

        BtnClick click = new BtnClick();

        btnLogin.setOnClickListener(click);

        //자동 로그인 확인
        String id = sp.getString(AndroidUtil.SP_LOGIN_ID, "");

        if(sp.getBoolean(AndroidUtil.SP_LOGIN_AUTO, false)){
            String pw = sp.getString(AndroidUtil.SP_LOGIN_PW, "");

            etId.setText(id);
            etPw.setText(pw);
            swLogin.setChecked(sp.getBoolean(AndroidUtil.SP_LOGIN_AUTO, false));

            if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(pw)) {
                goLogin();
            }
        }else{
            etId.setText(id);

            etPw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    if(i == EditorInfo.IME_ACTION_DONE){
                        goLogin();
                    }

                    return true;
                }
            });
        }


        //자동 로그인 끝
    }




    // 구글 보안 정책에 위배 되어 수정
    public String getDevicesUUID() {

        return Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //개발인지 운영인지 분기 처리
//        return IS_REAL? Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID) : "126757373ab1a776"; //JJJJJ
//        return IS_REAL? Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID) : "c550d6928d1f648f";  //KKKKK
    }

    /**
     * 로그인 메소드
     */
    private void goLogin() {

        if(!AndroidUtil.isConnectNetwork(context)){
            AndroidUtil.toast(context, "단말기가 인터넷에 연결되어 있지 않습니다. \n통신상태를 확인해주세요.");
            return;
        }



        String id = etId.getText().toString().toUpperCase();
        String pw = etPw.getText().toString();
        String secure = sp.getString(AndroidUtil.SP_SECURE_TAG, "");
        String version = AndroidUtil.getVersionName(context);


        NemoLoginPO param = new NemoLoginPO();
        param.setUserId(id);
        param.setPswd(pw);
        param.setMoblSno(secure);
        param.setVer(version);

        AndroidUtil.log("Login : " + param);

        api.login(param, LoginAPIHandler);
    }

    private void getAppVersion() {

        NemoEmptyListPO param = new NemoEmptyListPO();

        api.getAppInfo(param, appVerAPIHandler);
    }


    /**
     *
     * @param loginRO 로그인 서버 리턴 정보 확인
     */
    private void checkLoginInfo(final NemoLoginRO loginRO){

//        AndroidUtil.log("---------------------------------");
//        AndroidUtil.log(loginRO.size());
//        AndroidUtil.log(loginRO);
//        NemoLoginRO ro = loginRO.get(0);
//        AndroidUtil.log(ro == null);
////        AndroidUtil.log(ro);
//
//        AndroidUtil.log("---------------------------------");
//
////        if(1==1){
////            return;
////        }

        if(loginRO == null){
            AndroidUtil.toast(context, "담당자에게 문의 부탁드립니다.");
        }else{

// AndroidUtil.log(String.valueOf(System.nanoTime()));
// AndroidUtil.log(String.valueOf(System.currentTimeMillis()));

            //AndroidUtil.log("NemoLoginRO : " + loginRO);


            AndroidUtil.toast(context, "로그인 되었습니다.");

            boolean autoLogin = swLogin.isChecked();

            sp = getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
            editor = sp.edit();
            //아이디 기억
            editor.putString(AndroidUtil.SP_LOGIN_ID, AndroidUtil.spaceRemove(etId.getText().toString().toUpperCase()));
            //부서 번호 기억
            editor.putString(AndroidUtil.SP_LOGIN_DEPT, loginRO.getDeptCd());
            // 상위 부서 코드 기억
            editor.putString(AndroidUtil.SP_LOGIN_UPPR_DEPT, loginRO.getUpprDeptCd());

            if(autoLogin){
                editor.putBoolean(AndroidUtil.SP_LOGIN_AUTO, autoLogin);
                //editor.putString(AndroidUtil.SP_LOGIN_PW, etPw.getText().toString());
            }

            //////////////////////////////////////////////////////////
            // MG 24.01.04
            // 검사 결과 조회시 password 필요
            // Autologin 과 상관없이 저장한다.
            editor.putString(AndroidUtil.SP_LOGIN_PW, etPw.getText().toString());

            String loginInfo = gson.toJson(loginRO);
            editor.putString(AndroidUtil.SP_LOGIN_SESSION_INFO, loginInfo);

            editor.commit();

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * 클릭 리스너
     */
    private class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btnLogin:
                    goLogin();
                    break;
//                case R.id.ivLoginIdIco:
//                    etId.requestFocus();
//                    break;
//                case R.id.ivLoginPwIco:
//                    etPw.requestFocus();
//                    break;
            }
        }
    }



    private  class BtnLongClick implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.tvVersion:
                    String secure = sp.getString(AndroidUtil.SP_SECURE_TAG, "");
                    AndroidUtil.showAlert(context, "Serial Number", secure, "복사", new Handler() {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what){
                                case AndroidUtil.ALERT_CALLBACK_OK:

                                    AndroidUtil.copyText(context, "Serial Number", sp.getString(AndroidUtil.SP_SECURE_TAG, ""));

                                    break;
                            }

                        }
                    });

                break;

            }

            return false;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //start camera when permissions have been granted otherwise exit app
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                init();
            } else{
                Toast.makeText(this, "권한을 허용 하여야만 사용 가능 합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted(){
        //check if req permissions have been granted
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}
