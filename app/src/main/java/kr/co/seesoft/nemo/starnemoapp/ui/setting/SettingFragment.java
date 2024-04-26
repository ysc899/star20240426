package kr.co.seesoft.nemo.starnemoapp.ui.setting;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import androidx.navigation.Navigation;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;

    private Context context;

    TextView tvSerialNumber, tvSettingVersion;
    CheckBox cbAutoLogin, cbCameraSound, cbAutoSend, cbCameraThum, cbCameraSoundType;
    Button btnSave, btnSendLog;

    private CustomIngProgressDialog progressDialog;

    private CustomProgressDialog progressDialog2;

    // Title bar button
    private ImageButton btnBack, btnHome;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        context = getContext();

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view){

        tvSerialNumber = (TextView)view.findViewById(R.id.tvSettingSerial);
        tvSettingVersion = (TextView)view.findViewById(R.id.tvSettingVersion);

        cbAutoLogin = (CheckBox) view.findViewById(R.id.cbSettingAutologin);
        cbCameraSound = (CheckBox) view.findViewById(R.id.cbSettingCameraSound);
        cbAutoSend = (CheckBox)view.findViewById(R.id.cbSettingAutoSend);
        cbCameraThum = (CheckBox)view.findViewById(R.id.cbSettingCameraThum);
        cbCameraSoundType = (CheckBox)view.findViewById(R.id.cbSettingCameraSoundType);
        btnSave = (Button) view.findViewById(R.id.btnSettingSave);
        btnSendLog = (Button) view.findViewById(R.id.btnSettingSendLog);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init(){

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        settingViewModel.getSerialNumber().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvSerialNumber.setText(s);
            }
        });

        settingViewModel.getAutoLogin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbAutoLogin.setChecked(aBoolean);
            }
        });

        settingViewModel.getCameraSound().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbCameraSound.setChecked(aBoolean);
            }
        });
        settingViewModel.getCameraSoundType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbCameraSoundType.setChecked(aBoolean);
            }
        });

        settingViewModel.getAutoSend().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbAutoSend.setChecked(aBoolean);
            }
        });

        settingViewModel.getCameraThum().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbCameraThum.setChecked(aBoolean);
            }
        });

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            tvSettingVersion.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            AndroidUtil.log("설정 에러 : ", e);
            e.printStackTrace();
        }


        settingViewModel.setNemoRepository();

        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnSave.setOnClickListener(click);
        btnSendLog.setOnClickListener(click);
//        btnSendLog.setEnabled(false);

        tvSerialNumber.setOnLongClickListener(new LongClick());


        settingViewModel.getAllDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {


                settingViewModel.setGetDatasFlag(true);

//                btnSendLog.setEnabled(true);

//                btnSendLog.setActivated(true);
                System.out.println("체인지 완료");
//
//                for (PictureVO pictureVO : pictureVOS) {
//                    System.out.println(pictureVO);
//                }
//
//                System.out.println("체인지 완료");



            }
        });

        progressDialog = new CustomIngProgressDialog(getContext());
        settingViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        progressDialog2 = new CustomProgressDialog(getContext());
        settingViewModel.getProgressFlag2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog2.show();
                }else{
                    progressDialog2.dismiss();
                }

            }
        });


    }

    private class Click implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnSettingSave:

                    settingViewModel.saveInfo(cbAutoLogin.isChecked(), cbCameraSound.isChecked(), cbAutoSend.isChecked(), cbCameraThum.isChecked(), cbCameraSoundType.isChecked());

                    break;
                case R.id.btnSettingSendLog:

                    if(!settingViewModel.isGetDatas()){
                        AndroidUtil.toast(getContext(), "데이터 수집 중 입니다. \n 잠시 후 다시 눌러주세요");
                        return;
                    }


                    List<PictureVO> alls = settingViewModel.getAllPictureData();

                    System.out.println("=====================================================");
                    alls.forEach(it ->{
                        System.out.println(it.toString());
                    });
                    System.out.println("=====================================================");

                    settingViewModel.collectLogDatas();
                    
                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }


        }
    }

    private class LongClick implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()){
                case R.id.tvSettingSerial:
                    AndroidUtil.copyText(context, "Serial Number", settingViewModel.getSerialNumber().getValue());

                    break;
            }

            return false;
        }
    }

}