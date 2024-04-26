package kr.co.seesoft.nemo.starnemoapp.ui.transaction.payoff;

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
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesDepositListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionPayoffViewModel extends AndroidViewModel {

    /** 병원 정보 */
    private NemoHospitalSearchRO selHospital;

    private MutableLiveData<ArrayList<NemoSalesDepositListRO>> adapterlList;
    private MutableLiveData<Boolean> progressFlag;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler apiHandler;

    private MutableLiveData<String> searchYM;


    public TransactionPayoffViewModel(@NonNull Application application) {
        super(application);

        searchYM = new MutableLiveData<>();

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

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

                        ArrayList<NemoSalesDepositListRO> adapterList = (ArrayList<NemoSalesDepositListRO>)msg.obj;
                        adapterlList.setValue(adapterList);
                        break;
                    default:
                        //AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        Calendar mon = Calendar.getInstance();
        String searchYM = new java.text.SimpleDateFormat("yyyy").format(mon.getTime());

        setSearchYM(searchYM);


        adapterlList = new MediatorLiveData<>();


    }

    public void setHospital(NemoHospitalSearchRO hospitalInfo)
    {
        this.selHospital = hospitalInfo;

        apiSalesDepositList();
    }

    public void apiSalesDepositList()
    {
        progressFlag.setValue(true);

        // 수금 내역 List
        NemoSalesDepositListPO param = new NemoSalesDepositListPO();

        param.setCustCd(selHospital.getCustCd());
        param.setSearchYm(this.searchYM.getValue().replace("-",""));

        AndroidUtil.log("NemoSalesDepositListPO : " + param);

        api.getSalesDepositList(param, apiHandler);
    }

    public MutableLiveData<ArrayList<NemoSalesDepositListRO>> getAdapterlList() {
        return adapterlList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setSearchYM(String searchYM) {
        this.searchYM.setValue(searchYM);
    }

    public LiveData<String> getSearchYM()
    {
        return this.searchYM;
    }

}