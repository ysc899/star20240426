package kr.co.seesoft.nemo.starnemoapp.ui.transaction.payment;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesTransactionListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionPaymentViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> progressFlag;

    private NemoHospitalSearchRO selHospital;
    private MutableLiveData<ArrayList<NemoSalesTransactionListRO>> transactionlList;

    private MutableLiveData<String> startYM;
    private MutableLiveData<String> endYM;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler detailApiHandler, addApiHandler;


    public TransactionPaymentViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        startYM = new MutableLiveData<>();
        endYM = new MutableLiveData<>();

        Calendar mon = Calendar.getInstance();
        String toYM = new java.text.SimpleDateFormat("yyyy-MM").format(mon.getTime());
//        mon.add(Calendar.MONTH , -12);
//        String fromYM = new java.text.SimpleDateFormat("yyyy-MM").format(mon.getTime());

        setStartYM(toYM);
        setEndYM(toYM);

        transactionlList = new MediatorLiveData<>();

        api = new NemoAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");

        detailApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("msg.obj : " + msg.obj);

                        ArrayList<NemoSalesTransactionListRO> transcationList = (ArrayList<NemoSalesTransactionListRO>)msg.obj;

                        transactionlList.setValue(transcationList);

                        break;
                    default:

                        break;
                }

            }
        };

        addApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("===============> " + msg.obj);

                        NemoResultRO addObj = (NemoResultRO)msg.obj;
                        if("00020".equals(addObj.getMessageId())) {
                            AndroidUtil.toast(application, "등록 되었습니다.");

                        }else{
                            AndroidUtil.toast(application, "등록중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        }
                        break;
                    default:
                        AndroidUtil.toast(application, "등록중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        break;
                }

            }
        };

    }

    public void setHospital(NemoHospitalSearchRO hospitalInfo) {

        this.selHospital = hospitalInfo;

        apiTransactionList();

    }

    public void apiTransactionList()
    {
        this.progressFlag.setValue(true);

        // 거래 대장
        NemoSalesTransactionListPO param = new NemoSalesTransactionListPO();

        param.setCustCd(selHospital.getCustCd());
        param.setJobYmFrom(this.startYM.getValue().replace("-",""));
        param.setJobYmTo(this.endYM.getValue().replace("-",""));

        AndroidUtil.log("NemoSalesTransactionListPO : " + param);

        api.getSalesTransactionList(param, detailApiHandler);
    }

    public void apiAddApproval(NemoSalesApprovalAddPO param)
    {

        param.setCustCd(selHospital.getCustCd());
        //param.setChgrId(userId);
        param.setUserId(userId);

        AndroidUtil.log("NemoSalesApprovalAddPO : " + param);

        api.addApproval(param, addApiHandler);
    }


    public MutableLiveData<ArrayList<NemoSalesTransactionListRO>> getTransactionlList() {
        return transactionlList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setStartYM(String startYM) {
        this.startYM.setValue(startYM);
    }

    public LiveData<String> getStartYM()
    {
        return this.startYM;
    }

    public void setEndYM(String endYM) {
        this.endYM.setValue(endYM);
    }

    public LiveData<String> getEndYM()
    {
        return this.endYM;
    }

}