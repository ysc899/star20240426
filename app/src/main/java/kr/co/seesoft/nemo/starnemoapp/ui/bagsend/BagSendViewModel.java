package kr.co.seesoft.nemo.starnemoapp.ui.bagsend;

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
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoBagSendListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class BagSendViewModel extends AndroidViewModel {

    private MutableLiveData<Date> searchDate;
    private MutableLiveData<String> searchDateYmd;

    private MutableLiveData<ArrayList<NemoBagSendListRO>> bagSendList;


    private MutableLiveData<Boolean> progressFlag;

    private Application application;

    private String userId;
    private String deptCode;

    private NemoAPI api;
    private Handler apiHandler;


    public BagSendViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

        searchDate = new MediatorLiveData<>();
        searchDateYmd = new MediatorLiveData<>();
        bagSendList = new MediatorLiveData<>();

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoBagSendListRO> bagSendTempList = (ArrayList<NemoBagSendListRO>)msg.obj;
                        bagSendList.setValue(bagSendTempList);

                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 정보가 없습니다.");
                        break;
                }

            }
        };


        this.setSearchDate(new Date());
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate.setValue(searchDate);
        this.searchDateYmd.setValue(DateUtil.getFormatString(this.searchDate.getValue(), "yyyy년 MM월 dd일"));
        this.getAPIBagSendListData();
    }

    public MutableLiveData<Date> getSearchDate() {
        return searchDate;
    }

    public void getAPIBagSendListData()
    {
        this.progressFlag.setValue(true);

        NemoBagSendListPO param = new NemoBagSendListPO();
        param.setUserId(userId);
        param.setDeptCd(deptCode);

        Date d = this.searchDate.getValue();

        param.setSendDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        AndroidUtil.log("NemoBagSendListPO : " + param);

        api.getBagSendList(param, apiHandler);
    }

    public MutableLiveData<String> getSearchDateYmd() {
        return searchDateYmd;
    }

    public MutableLiveData<ArrayList<NemoBagSendListRO>> getBagSendList() {
        return bagSendList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

}