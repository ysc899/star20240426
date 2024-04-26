package kr.co.seesoft.nemo.starnemoapp.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoCodePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerVOCCountPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoEmptyListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerVOCCountRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoAppInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.ConfirmDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SearchCustomerDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class MenuFragment extends Fragment {

    private MenuViewModel homeViewModel;

    private TextView tvCount;
    private Handler apiVOCHandler, apiMenuHandler, appVerAPIHandler , updateHandler;

    private NemoAPI api;
    private String deptCode;
    private String vocCfmDtm;

    private ArrayList<NemoCustomerMemoCodeListRO> codeMenuList;

    private View view = null;

    private ConfirmDialog confirmDialog;
    private String urlNewVersion = "";

    private NestedScrollView nsvDisp;
    private ConstraintLayout conLayoutButton;
    private ImageButton btnBagSend;
    private ImageButton btnCustomer;
    private ImageButton btnMemo;
    private ImageButton btnVOC;
    private ImageButton btnItemSearch;
    private ImageButton btnResultSearch;
    private ImageButton btnCustomerSupport;
    private boolean isResize;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        initUI(root);
        return root;
    }

    private void initUI(View view){

        this.view = view;

        api = new NemoAPI(getContext());

        SharedPreferences sp = getContext().getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");
        vocCfmDtm = sp.getString(AndroidUtil.SP_VOC_VIEW_DATE, "");

        tvCount = (TextView) view.findViewById(R.id.tvCount);

        nsvDisp = view.findViewById(R.id.nsvDisp);
        conLayoutButton = view.findViewById(R.id.conLayoutButton);
        btnBagSend = view.findViewById(R.id.btnBagSend);
        btnCustomer = view.findViewById(R.id.btnCustomer);
        btnMemo = view.findViewById(R.id.btnMemo);
        btnVOC = view.findViewById(R.id.btnVOC);
        btnItemSearch = view.findViewById(R.id.btnItemSearch);
        btnResultSearch = view.findViewById(R.id.btnResultSearch);
        btnCustomerSupport = view.findViewById(R.id.btnCustomerSupport);

        isResize =  true;

        ViewTreeObserver vto = btnBagSend.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                AndroidUtil.log("btnVisitPlan height : " + btnBagSend.getHeight());
//                AndroidUtil.log("screen height : " + getScreenHeight());
//                AndroidUtil.log("nsvDisp height : " + nsvDisp.getHeight());
//                AndroidUtil.log("conLayoutButton height : " + conLayoutButton.getHeight());

                if( conLayoutButton.getHeight() > nsvDisp.getHeight() && isResize )
                {

                    isResize = false;

                    setMargin(btnBagSend);
                    setMargin(btnCustomer);
                    setMargin(btnMemo);
                    setMargin(btnVOC);
                    setMargin(btnItemSearch);
                    setMargin(btnResultSearch);
                    setMargin(btnCustomerSupport);

                }

            }
        });




        //getDispMenu();

        getVOCCount();

        appVerAPIHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:
                    case 201:
                        //통신 완료
                        NemoAppInfoRO result = (NemoAppInfoRO)msg.obj;

                        AndroidUtil.log("NemoAppInfoRO : " + result);

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

        getAppVersion();


    }

    private void setMargin(ImageButton button)
    {
        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) button.getLayoutParams();

        //AndroidUtil.log("Button topMargin : " + params1.topMargin);

        params1.topMargin = 30;

        button.setLayoutParams(params1);
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    private void getAppVersion() {

        NemoEmptyListPO param = new NemoEmptyListPO();

        api.getAppInfo(param, appVerAPIHandler);
    }

    private void versionCheck(NemoAppInfoRO result)
    {
        String nowVer = AndroidUtil.getVersionName(getContext());

        if( nowVer.equals( result.getVer() ))
        {
            getDispMenu();
        }
        else
        {
            //getDispMenu();

            urlNewVersion = result.getFileUrl();

            confirmDialog = new ConfirmDialog(getContext(), updateHandler);

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
            getActivity().finishAndRemoveTask();
        } else {
            // 액티비티 종료
            getActivity().finish();
        }

        System.exit(0);
    }

    private void getVOCCount()
    {
        try {

            apiVOCHandler = new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);

                    switch (msg.what){
                        case 200:
                        case 201:
                            AndroidUtil.log("===============> " + msg.obj);

                            NemoResultCustomerVOCCountRO vocResult = (NemoResultCustomerVOCCountRO)msg.obj;

                            dispVOCCount(vocResult);

                            break;
                    }

                }
            };

            NemoCustomerVOCCountPO param = new NemoCustomerVOCCountPO();
            param.setDeptCd(deptCode);
            param.setVocCfmDtm(vocCfmDtm);

            AndroidUtil.log("NemoCustomerVOCCountPO : " + param);

            api.getVOCCount(param,apiVOCHandler);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void dispVOCCount(NemoResultCustomerVOCCountRO vocResult)
    {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                try
                {
                    tvCount.setVisibility(View.GONE);

                    int vocCount = vocResult.getResult();

                    if( vocCount > 0 )
                    {
                        tvCount.setVisibility(View.VISIBLE);

                        tvCount.setText(String.valueOf(vocCount));
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getDispMenu()
    {
        try {

            apiMenuHandler = new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);

                    switch (msg.what){
                        case 200:
                        case 201:
                            AndroidUtil.log("===============> " + msg.obj);

                            codeMenuList = (ArrayList<NemoCustomerMemoCodeListRO>)msg.obj;

                            setActionListener();

                            break;
                    }

                }
            };

            NemoCustomerMemoCodePO param = new NemoCustomerMemoCodePO();

            param.setCtgrId(Const.MAIN_MENU_CTGR_ID);

            AndroidUtil.log("NemoCustomerMemoCodePO : " + param);

            api.getMenuList(param,apiMenuHandler);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setActionListener()
    {

        Click click = new Click();

        Bundle dataBundle = new Bundle();
        dataBundle.putString("fromMenu", "true");

        if( isDisp("VISIT_PLAN") )
            view.findViewById(R.id.btnVisitPlan).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_visitPlanFragment,dataBundle));
        else
            view.findViewById(R.id.btnVisitPlan).setOnClickListener(click);

        if( isDisp("TRANSACTION") )
            view.findViewById(R.id.btnTransaction).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_transactionFragment,dataBundle));
        else
            view.findViewById(R.id.btnTransaction).setOnClickListener(click);

        if( isDisp("BAG_SEND") )
            view.findViewById(R.id.btnBagSend).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_bagsendFragment,dataBundle));
        else
            view.findViewById(R.id.btnBagSend).setOnClickListener(click);

        if( isDisp("CUSTOMER") )
            view.findViewById(R.id.btnCustomer).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_customerFragment,dataBundle));
        else
            view.findViewById(R.id.btnCustomer).setOnClickListener(click);

        if( isDisp("MEMO") )
            view.findViewById(R.id.btnMemo).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_memoFragment,dataBundle));
        else
            view.findViewById(R.id.btnMemo).setOnClickListener(click);

//        if( isDisp("CONTACT") )
//            view.findViewById(R.id.btnContact).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_departmentContactFragment,dataBundle));
//        else
//            view.findViewById(R.id.btnContact).setOnClickListener(click);

        if( isDisp("VOC") )
            view.findViewById(R.id.btnVOC).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_vocFragment,dataBundle));
        else
            view.findViewById(R.id.btnVOC).setOnClickListener(click);


        Bundle fromBundle1 = new Bundle();
        fromBundle1.putString("callType", "1");

        if( isDisp("ITEM_SEARCH") )
            view.findViewById(R.id.btnItemSearch).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_resultSearchFragment,fromBundle1));
        else
            view.findViewById(R.id.btnItemSearch).setOnClickListener(click);

        Bundle fromBundle2 = new Bundle();
        fromBundle2.putString("callType", "2");

        if( isDisp("RESULT_SEARCH") )
            view.findViewById(R.id.btnResultSearch).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_resultSearchFragment,fromBundle2));
        else
            view.findViewById(R.id.btnResultSearch).setOnClickListener(click);

        if( isDisp("CUSTOMER_SUPPORT") )
            view.findViewById(R.id.btnCustomerSupport).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_customerSupportFragment,dataBundle));
        else
            view.findViewById(R.id.btnCustomerSupport).setOnClickListener(click);


        view.findViewById(R.id.btnConfig).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_setting));
    }

    private boolean isDisp(String menu)
    {
        boolean rValue = false;

        try {
            List<NemoCustomerMemoCodeListRO> hList = codeMenuList.stream().filter(it -> {
                return it.getCmmnCd().equals(menu);
            }).collect(Collectors.toList());

            if(hList.size() > 0){
                rValue = true;
            }
        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

//        AndroidUtil.log("isDisp ===> " + menu + " / " + rValue);

        return rValue;

    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            AndroidUtil.toast(getContext(), "준비중입니다.");
        }
    }
}