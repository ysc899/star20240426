package kr.co.seesoft.nemo.starnemoapp.ui.register.voc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerVOCListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesTransactionListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerVOCListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class VOCViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<NemoCustomerVOCListRO>> vocList;

    private MutableLiveData<Boolean> progressFlag;
    private Application application;

    private String userId, deptCode, upperDeptCd;

    private NemoAPI api;
    private Handler apiHandler;

    private MutableLiveData<Date> searchDate;
    private MutableLiveData<String> searchDateYmd;

    private NemoScheduleListRO selHospital;

    // 현재 페이지
    private int currentPage;


    public VOCViewModel(@NonNull Application application) {
        super(application);

        searchDate = new MediatorLiveData<>();
        searchDateYmd = new MediatorLiveData<>();

        vocList = new MediatorLiveData<>();

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");
        upperDeptCd = sp.getString(AndroidUtil.SP_LOGIN_UPPR_DEPT, "");


        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoCustomerVOCListRO> vocTempList = (ArrayList<NemoCustomerVOCListRO>)msg.obj;


                        if( currentPage > 1 )
                        {
                            // 현재 MutableLiveData의 값을 가져옴
                            ArrayList<NemoCustomerVOCListRO> currentData = vocList.getValue();

                            // 가져온 데이터를 현재 데이터에 추가
                            currentData.addAll(vocTempList);

                            // MutableLiveData의 값을 변경하여 LiveData를 갱신
                            vocList.setValue(currentData);
                        }
                        else
                        {
                            vocList.setValue(vocTempList);
                        }

                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 정보가 없습니다.");
                        break;
                }

            }
        };

        currentPage = 1;

        this.setSearchDate(new Date());

    }


    public MutableLiveData<ArrayList<NemoCustomerVOCListRO>> getVocList() {
        return vocList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setHospital(NemoScheduleListRO hospitalInfo) {

        this.selHospital = hospitalInfo;

        apiVOCList();

    }

    public void apiVOCList()
    {
        this.progressFlag.setValue(true);

        NemoCustomerVOCListPO param = new NemoCustomerVOCListPO();

        param.setCustCd(selHospital.getCustCd());

        Date searchDt = this.getSearchDate().getValue();

        param.setSearchDt(DateUtil.getFormatString(searchDt, "yyyyMMdd"));

        param.setPageIndex(currentPage);
        param.setPageSize(Const.PAGE_SIZE);

        AndroidUtil.log("NemoCustomerVOCListPO : " + param);

        api.getVOCList(param, apiHandler);
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate.setValue(searchDate);
        this.searchDateYmd.setValue(DateUtil.getFormatString(this.searchDate.getValue(), "yyyy년 MM월 dd일"));
    }

    public MutableLiveData<Date> getSearchDate() {
        return searchDate;
    }

    public MutableLiveData<String> getSearchDateYmd() {
        return searchDateYmd;
    }

    public void setClear()
    {
        this.setSearchDate(new Date());
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

}