package kr.co.seesoft.nemo.starnemoapp.ui.customer.detail;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerDetailPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesTransactionListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerDetailRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class CustomerDetailViewModel extends AndroidViewModel {

    private Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    Gson gson = new Gson();

    private MutableLiveData<NemoCustomerDetailRO> customerDetailInfo;

    //    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanHospitalList;
    private MutableLiveData<ArrayList<NemoHospitalSearchRO>> visitPlanHospitalList;
    private MutableLiveData<Boolean> progressFlag;
    private ArrayList<NemoHospitalSearchRO> baseHospitalList;

    private Application application;

    private String userId , userDeptCd;

    private NemoAPI api;
    private Handler apiHandler;

    private String hospitalInfo;


    public CustomerDetailViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        userDeptCd = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");

        customerDetailInfo = new MediatorLiveData<>();

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("===============> " + msg.obj);

                        NemoCustomerDetailRO apiList = (NemoCustomerDetailRO)msg.obj;
                        customerDetailInfo.setValue(apiList);
                        break;
                    default:
                        //AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

    }

    public void setHospital(String hospitalInfo) {

        this.hospitalInfo = hospitalInfo;

        apiCustomerDetailList();

    }

    public void apiCustomerDetailList()
    {
        progressFlag.setValue(true);

        // 고객 정보 상세
        NemoCustomerDetailPO param = new NemoCustomerDetailPO();

        param.setCustCd(hospitalInfo);

        AndroidUtil.log("NemoCustomerDetailPO : " + param);

        api.getCustomerDetail(param, apiHandler);
    }

    public MutableLiveData<NemoCustomerDetailRO> getCustomerDetailInfo() {
        return customerDetailInfo;
    }



    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }



}