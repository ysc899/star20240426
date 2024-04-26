package kr.co.seesoft.nemo.starnemoapp.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.LocationVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.service.LocationUpdatesService;
import kr.co.seesoft.nemo.starnemoapp.service.Utils;
import kr.co.seesoft.nemo.starnemoapp.ui.setting.SettingFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.TransactionFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplan.VisitPlanFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplanadd.VisitPlanAddFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class MainMenuActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    //private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    private NemoRepository nemoRepository;

    private Context context;


    //gps 등록 관련
    private NemoAPI api;
    private String userId;
    private String deptCode;


    protected TextView textTitle;
    protected ImageButton btnBack;
    protected ImageButton btnHome;
    protected TextView txtConfig;

//    protected OnBackPressedListener onBackPressedListener;

//    public interface OnBackPressedListener {
//        void doBack();
//    }
//
//    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
//        this.onBackPressedListener = onBackPressedListener;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (onBackPressedListener != null)
//            onBackPressedListener.doBack();
//        else
//            super.onBackPressed();
//    }


    // Monitors the state of the connection to the service.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
//            mService = binder.getService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//            mBound = false;
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myReceiver = new MyReceiver();


        setContentView(R.layout.activity_main_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_menu, R.id.navigation_myinfo, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        FragmentView(1);

        context = this;


        api = new NemoAPI(context);

        SharedPreferences sp = context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");


        nemoRepository = new NemoRepository(this);
        checkDirectory();

//        nemoRepository.getLocLastOneData().observe(this, new Observer<LocationVO>() {
//            @Override
//            public void onChanged(LocationVO locationVOS) {
//                AndroidUtil.log("---------------------위치 경위도 변경----------------------");
//
//                if(locationVOS != null) {
//
//                    NemoGpsAddPO param = new NemoGpsAddPO();
//                    param.setUserId(userId);
//                    param.setDeptCode(deptCode);
//                    param.setYmd(DateUtil.getFormatString(locationVOS.date, "yyyyMMdd"));
//                    param.setTime(DateUtil.getFormatString(locationVOS.date, "HHmmss"));
//                    param.setLat(locationVOS.latitude);
//                    param.setLng(locationVOS.longitude);
//
//                    api.gpsAdd(param);
//                }
//
////                locationVOS.forEach(t ->{
////                    AndroidUtil.log(t.toString());
////                });
////                AndroidUtil.log("---------------------위치 경위도 변경----------------------");
//            }
//        });
//
//        //위경도 과거 데이터 삭제
//        nemoRepository.deleteLocationsByBeforeDate(DateUtil.addDay(new Date(), -3));



        initUI();

        Intent secondIntent = getIntent();
        int viewNo = secondIntent.getIntExtra("view", 1);

        FragmentView(viewNo);
    }


    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


//        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                Context.BIND_AUTO_CREATE);
//
//
//        Handler h = new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                mService.requestLocationUpdates();
//            }
//        };
//        h.sendEmptyMessageDelayed(0, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            //unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //TODO 마냥 수집 할지 확인 필요

//        onBackPressedListener = null;

        //mService.removeLocationUpdates();
        super.onDestroy();
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(MainMenuActivity.this, Utils.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
//            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
//                    false));
        }
    }



    /**
     * 오래된 폴더가 있는지 확인 후 삭제btnMenuVisitPlan
     * 3일 이전 폴더들은 삭제 처리 해버림
     */
    private void checkDirectory(){

        File extDir = Arrays.asList(context.getExternalFilesDirs(context.getResources().getString(R.string.app_name))).stream().findFirst().orElse(null);

        checkChildDirectory(extDir);

        if(context.getExternalMediaDirs().length > 0){

            File medParentDir = context.getExternalMediaDirs()[0];
            File targetDir = new File(medParentDir, context.getResources().getString(R.string.app_name));

            File medDir = Arrays.asList(targetDir).stream().findFirst().orElse(null);
            checkChildDirectory(medDir);
        }


    }

    private void checkChildDirectory(File targetDir){
        if(targetDir != null){

            if(!targetDir.exists()){
                return;
            }

            String today = DateUtil.getFormatString(new Date(), "yyyyMMdd");

            Arrays.asList(targetDir.list()).stream().forEach(t -> {
                try {

                    long diff = DateUtil.dayDiff(t, today, "yyyyMMdd");
                    if (diff > 259200l) {

                        AndroidUtil.log("삭제 대상 : " + t);
                        deleteDir(targetDir.getAbsolutePath() + File.separator + t);
                        nemoRepository.deleteByYmd(t);
                        AndroidUtil.log("삭제 완료 : " + t);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    /**
     * 파일 디렉토리 삭제
     * @param dirPath 삭제할 경로
     */
    private void deleteDir(String dirPath){
        File dir = new File(dirPath);

        if(dir.isFile()){
            dir.delete();
        }else {
            File[] listFiles = dir.listFiles();
            if (dir.exists()) {
                for (File f : listFiles) {
                    if (f.isDirectory()) {
                        deleteDir(f.getAbsolutePath());
                    } else {
                        f.delete();
                    }
                }
                dir.delete();
            }
        }
    }


    public void FragmentView(int fragment){

        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 일일 방문 계획

                textTitle.setText("일일 방문 계획");

                VisitPlanFragment fragmentVisitPlan = new VisitPlanFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragmentVisitPlan).commitAllowingStateLoss();
                break;

            case 11:
                // 일일 방문 계획 추가

                textTitle.setText("일일 방문 계획 추가");

                VisitPlanAddFragment fragmentVisitPlanAdd = new VisitPlanAddFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragmentVisitPlanAdd).commitAllowingStateLoss();
                break;

            case 2:
                // 거래대장

                textTitle.setText("거래대장");

                TransactionFragment fragmentTransaction = new TransactionFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragmentTransaction).commitAllowingStateLoss();
                break;

            case 100:
                // 환경 설정

                AndroidUtil.log("Call 환경 설정");

                textTitle.setText("환경 설정");

                SettingFragment fragmentSetting = new SettingFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragmentSetting).commitAllowingStateLoss();
                break;
        }

    }

    private void initUI() {
        context = this;

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        textTitle = (TextView) findViewById(R.id.textTitle);
        txtConfig = (TextView) findViewById(R.id.txtConfig);


        MainMenuActivity.Click click = new MainMenuActivity.Click();

        btnBack.setOnClickListener(click);
        btnHome.setOnClickListener(click);
        txtConfig.setOnClickListener(click);
    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnHome:

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();

//                    calendarDialog.show(statisViewModel.getVisPlanDate().getValue());
                    break;

//                case R.id.btnBack:
//
//                    onBackPressed();

                case R.id.txtConfig:

                    FragmentView(100);

//                    statisViewModel.calcVisPlanDate(1);
                    break;
                case R.id.btnStatisPre:
//                    statisViewModel.calcVisPlanDate(-1);
                    break;
            }
        }
    }

    public void setTitle(String title)
    {
        textTitle.setText(title);
    }



}