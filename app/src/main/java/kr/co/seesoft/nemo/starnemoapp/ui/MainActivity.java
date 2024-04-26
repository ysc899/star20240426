package kr.co.seesoft.nemo.starnemoapp.ui;

import android.app.Activity;
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
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.LocationVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerVOCCountPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoGpsAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerVOCCountRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerVOCListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.service.LocationUpdatesService;
import kr.co.seesoft.nemo.starnemoapp.service.Utils;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    // MG 23.12.22
    // 위치 서비스 삭제
    //private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    private NemoRepository nemoRepository;

    private Context context;


    //gps 등록 관련
    private NemoAPI api;


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


        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_menu, R.id.navigation_myinfo, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        context = this;


        api = new NemoAPI(context);


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
//                    /////////////////////////////////
//                    // MG - 23.10.31
//                    // GSP API 가 없어 일단 막는다
////                    api.gpsAdd(param);
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
//            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //TODO 마냥 수집 할지 확인 필요
//        mService.removeLocationUpdates();
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
                Toast.makeText(MainActivity.this, Utils.getLocationText(location),
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
     * 오래된 폴더가 있는지 확인 후 삭제
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

                    if( t != null && t.length() == 8 && "20".equals(t.substring(0,2)) )
                    {
                        //AndroidUtil.log("today:" + t);

                        long diff = DateUtil.dayDiff(t, today, "yyyyMMdd");
                        if (diff > 259200l) {

                            AndroidUtil.log("삭제 대상 : " + t);
                            deleteDir(targetDir.getAbsolutePath() + File.separator + t);
                            nemoRepository.deleteByYmd(t);
                            AndroidUtil.log("삭제 완료 : " + t);
                        }
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


}