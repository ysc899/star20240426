package kr.co.seesoft.nemo.starnemoapp.ui.memo;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class MemoViewModel extends AndroidViewModel {

    private MutableLiveData<Date> searchDate;
    private MutableLiveData<String> searchDateYmd;

    private MutableLiveData<ArrayList<NemoCustomerMemoListRO>> memoList;


    private MutableLiveData<Boolean> progressFlag;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler apiHandler;


    public MemoViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

        searchDate = new MediatorLiveData<>();
        searchDateYmd = new MediatorLiveData<>();
        memoList = new MediatorLiveData<>();

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoCustomerMemoListRO> memoTempList = (ArrayList<NemoCustomerMemoListRO>)msg.obj;
                        memoList.setValue(memoTempList);

                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 정보가 없습니다.");
                        break;
                }

            }
        };

    }

    public void setSearchDate(Date searchDate) {
        this.searchDate.setValue(searchDate);
        this.searchDateYmd.setValue(DateUtil.getFormatString(this.searchDate.getValue(), "yyyy년 MM월 dd일"));
        this.getAPIMemoListData();
    }

    public MutableLiveData<Date> getSearchDate() {
        return searchDate;
    }

    public void getAPIMemoListData()
    {
        this.progressFlag.setValue(true);

        NemoCustomerMemoPO param = new NemoCustomerMemoPO();
        param.setUserId(userId);

        Date d = this.searchDate.getValue();

        param.setCmplRqstDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        AndroidUtil.log("NemoCustomerMemoPO : " + param);

        api.getMemoList(param, apiHandler);
    }

    public MutableLiveData<String> getSearchDateYmd() {
        return searchDateYmd;
    }

    public MutableLiveData<ArrayList<NemoCustomerMemoListRO>> getMemoList() {
        return memoList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setClear()
    {

        if( memoList.getValue() != null && memoList.getValue().size() > 0)
        {
            memoList.getValue().clear();
        }

        this.setSearchDate(new Date());
    }

}