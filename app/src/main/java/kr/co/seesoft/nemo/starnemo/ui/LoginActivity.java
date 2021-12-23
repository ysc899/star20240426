package kr.co.seesoft.nemo.starnemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemo.BuildConfig;
import kr.co.seesoft.nemo.starnemo.R;
import kr.co.seesoft.nemo.starnemo.api.po.LoginPO;
import kr.co.seesoft.nemo.starnemo.api.ro.LoginRO;
import kr.co.seesoft.nemo.starnemo.api.service.StarAPI;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoLoginPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoLoginRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.CipherUtil;

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
    private Handler LoginAPIHandler;
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

        BtnClick click = new BtnClick();

        btnLogin.setOnClickListener(click);
        findViewById(R.id.ivLoginIdIco).setOnClickListener(click);
        findViewById(R.id.ivLoginPwIco).setOnClickListener(click);


        tvVersion.setOnLongClickListener(new BtnLongClick());

        sp = getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        editor = sp.edit();

        gson = new Gson();

        //시큐어 코드 확인
        String secures = sp.getString(AndroidUtil.SP_SECURE_TAG, "");

        if (StringUtils.isEmpty(secures)) {

            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NeoDIN"+ File.separator + "serial.txt";
            File sFile = new File(filePath);
            if(sFile.isFile()){
                //파일에 있는거 불러들이기
                try {
                    secures = FileUtils.readFileToString(sFile, "UTF-8");
                } catch (IOException e) {
                    secures = getDevicesUUID();
                }

            }else {
                //시큐어 코드 생성
                secures = getDevicesUUID();
            }

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
                        //통신 완료
                        ArrayList<NemoLoginRO> result = (ArrayList<NemoLoginRO>)msg.obj;
                        checkLoginInfo(result);
                        break;
                    default:
                    case 0:
                        AndroidUtil.toast(context, "전산실에 문의 부탁드립니다.");
                        break;

                }
            }
        };
        //API 설정 끝

        //세션 정보 초기화
        editor.putString(AndroidUtil.SP_LOGIN_SESSION_INFO, "");
        editor.commit();

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
        //개발인지 운영인지 분기 처리
        return IS_REAL? Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID) : "126757373ab1a776";
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

//        LoginPO param = new LoginPO();
//        param.setUserId(id);
//        param.setPassword(CipherUtil.Encrypt(pw));
//        param.setInstCd("01");
//
//        api.login(param, LoginAPIHandler);


        NemoLoginPO param = new NemoLoginPO();
        param.setUserId(id);
        param.setPwd(pw);
        param.setSecureId(secure);

        api.login(param, LoginAPIHandler);
    }


    /**
     *
     * @param loginRO 로그인 서버 리턴 정보 확인
     */
    private void checkLoginInfo(final ArrayList<NemoLoginRO> loginRO){

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

        if(loginRO.size() == 0 || loginRO.get(0) == null){
            AndroidUtil.toast(context, "담당자에게 문의 부탁드립니다.");
        }else{



            AndroidUtil.toast(context, "로그인 되었습니다.");

            boolean autoLogin = swLogin.isChecked();

            sp = getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
            editor = sp.edit();
            //아이디 기억
            editor.putString(AndroidUtil.SP_LOGIN_ID, etId.getText().toString().toUpperCase());
            //부서 번호 기억
            editor.putString(AndroidUtil.SP_LOGIN_DEPT, loginRO.get(0).getDpt());

            if(autoLogin){
                editor.putBoolean(AndroidUtil.SP_LOGIN_AUTO, autoLogin);
                editor.putString(AndroidUtil.SP_LOGIN_PW, etPw.getText().toString());
            }

            String loginInfo = gson.toJson(loginRO);
            editor.putString(AndroidUtil.SP_LOGIN_SESSION_INFO, loginInfo);

            editor.commit();

            Intent intent = new Intent(context, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }



//        if(loginRO.isLoginSuccess()){
//            AndroidUtil.toast(context, "로그인 되었습니다.");
//
//            boolean autoLogin = swLogin.isChecked();
//
//            sp = getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
//            editor = sp.edit();
//            //아이디 기억
//            editor.putString(AndroidUtil.SP_LOGIN_ID, etId.getText().toString());
//
//            if(autoLogin){
//                editor.putBoolean(AndroidUtil.SP_LOGIN_AUTO, autoLogin);
//                editor.putString(AndroidUtil.SP_LOGIN_PW, etPw.getText().toString());
//            }
//
//            String loginInfo = gson.toJson(loginRO);
//            editor.putString(AndroidUtil.SP_LOGIN_SESSION_INFO, loginInfo);
//
//            editor.commit();
//
//            Intent intent = new Intent(context, MainMenuActivity.class);
//            startActivity(intent);
//            finish();
//
//        }else{
//            AndroidUtil.toast(context, "계정정보를 확인하세요.");
//        }
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
                case R.id.ivLoginIdIco:
                    etId.requestFocus();
                    break;
                case R.id.ivLoginPwIco:
                    etPw.requestFocus();
                    break;
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
