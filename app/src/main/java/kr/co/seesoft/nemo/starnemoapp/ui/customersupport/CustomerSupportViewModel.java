package kr.co.seesoft.nemo.starnemoapp.ui.customersupport;

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
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class CustomerSupportViewModel extends AndroidViewModel {

    private MutableLiveData<Date> searchDate;
    private MutableLiveData<String> searchDateYmd;
    private MutableLiveData<Boolean> progressFlag;

    private MutableLiveData<ArrayList<NemoCustomerSupportListRO>> customerSupportList;

    private Application application;

    private String userId, userDeptCd;

    private NemoAPI api;
    private Handler apiHandler;

    // 현재 페이지
    private int currentPage;


    public CustomerSupportViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        userDeptCd = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        ArrayList<NemoCustomerSupportListRO> visitPlanList = (ArrayList<NemoCustomerSupportListRO>)msg.obj;

                        if( currentPage > 1 )
                        {
                            // 현재 MutableLiveData의 값을 가져옴
                            ArrayList<NemoCustomerSupportListRO> currentData = customerSupportList.getValue();

                            // 가져온 데이터를 현재 데이터에 추가
                            currentData.addAll(visitPlanList);

                            // MutableLiveData의 값을 변경하여 LiveData를 갱신
                            customerSupportList.setValue(currentData);
                        }
                        else
                        {
                            customerSupportList.setValue(visitPlanList);
                        }

                        break;
                    default:
                        //AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };


        customerSupportList = new MediatorLiveData<>();

        searchDate = new MediatorLiveData<>();
        searchDateYmd = new MediatorLiveData<>();

        currentPage = 1;

        //this.setSearchDate(new Date());

    }


    public void setSearchDate(Date searchDate) {
        this.searchDate.setValue(searchDate);
        this.searchDateYmd.setValue(DateUtil.getFormatString(this.searchDate.getValue(), "yyyy년 MM월 dd일"));
        this.getAPICustomerSupportListData();
    }

    public MutableLiveData<Date> getSearchDate() {
        return searchDate;
    }

    public void getAPICustomerSupportListData()
    {
        this.progressFlag.setValue(true);

        NemoCustomerSupportListPO param = new NemoCustomerSupportListPO();
        param.setDeptCd(userDeptCd);
        param.setPageIndex(currentPage);
        param.setPageSize(Const.PAGE_SIZE);

        Date d = this.searchDate.getValue();

        param.setSearchDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        AndroidUtil.log("NemoCustomerSupportListPO : " + param);

        api.getCustomerSupportList(param, apiHandler);
    }

    public MutableLiveData<String> getSearchDateYmd() {
        return searchDateYmd;
    }

    public MutableLiveData<ArrayList<NemoCustomerSupportListRO>> getCustomerSupportList() {
        return customerSupportList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public int getCurrentPage(){ return this.currentPage; }

    public void setClear()
    {

        if( customerSupportList.getValue() != null && customerSupportList.getValue().size() > 0)
        {
            customerSupportList.getValue().clear();
        }

        currentPage = 1;

        this.setSearchDate(new Date());
    }

    public void setListClear()
    {

        if( customerSupportList.getValue() != null && customerSupportList.getValue().size() > 0)
        {
            customerSupportList.getValue().clear();
        }

        currentPage = 1;

        this.getAPICustomerSupportListData();
    }

}