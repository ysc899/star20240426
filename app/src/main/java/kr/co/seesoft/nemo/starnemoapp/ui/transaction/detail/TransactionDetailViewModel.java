package kr.co.seesoft.nemo.starnemoapp.ui.transaction.detail;

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
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerRecpInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesTransactionListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoTermsSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class TransactionDetailViewModel extends AndroidViewModel {

    private Type visitPlanListType = new TypeToken<ArrayList<NemoSalesTransactionListRO>>() {
    }.getType();
    Gson gson = new Gson();

    //    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanHospitalList;
    private MutableLiveData<ArrayList<NemoHospitalSearchRO>> visitPlanHospitalList;

    private MutableLiveData<ArrayList<NemoSalesTransactionListRO>> transactionlList;
    private MutableLiveData<Boolean> progressFlag;
    private ArrayList<NemoHospitalSearchRO> baseHospitalList;

    private Application application;

    private String userId, deptCode, upperDeptCd;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler detailApiHandler;
    private Handler signApiHandler;
    private Handler amtApiHandler;

    private MutableLiveData<String> startYM;
    private MutableLiveData<String> endYM;

    private NemoHospitalSearchRO selHospital;
    private MutableLiveData<NemoCustomerInfoRO> hospitalInfo;

    private String amtMonth = "0";


    public TransactionDetailViewModel(@NonNull Application application) {
        super(application);

        startYM = new MutableLiveData<>();
        endYM = new MutableLiveData<>();

        hospitalInfo = new MediatorLiveData<>();

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

                        NemoCustomerInfoRO result = (NemoCustomerInfoRO)msg.obj;
                        setHospitalInfo(result);
                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        detailApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("msg.obj : detailApiHandler : " + msg.obj);

                        ArrayList<NemoSalesTransactionListRO> transcationList = (ArrayList<NemoSalesTransactionListRO>)msg.obj;

                        transactionlList.setValue(transcationList);

                        break;
                    default:

                        break;
                }

            }
        };

        signApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        NemoResultRO result = (NemoResultRO)msg.obj;

                        if( "00020".equals(result.getMessageId()))
                        {
                            AndroidUtil.toast(application, "전송 완료 되었습니다.");

                            apiTransactionList();
                        }
                        else
                        {
                            AndroidUtil.toast(application, result.getMessageContent());
                        }

                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        amtApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        AndroidUtil.log("msg.obj : amtApiHandler : " + msg.obj);

                        ArrayList<NemoSalesTransactionListRO> transcationList = (ArrayList<NemoSalesTransactionListRO>)msg.obj;

                        if( transcationList.size() > 0 )
                        {
                            NemoSalesTransactionListRO t = transcationList.get(0);

                            amtMonth = AndroidUtil.dispCurrency(t.getTmrqUcamt());

                        }

                        break;
                    default:

                        break;
                }

            }
        };

        Calendar mon = Calendar.getInstance();
        String toYM = new java.text.SimpleDateFormat("yyyy-MM").format(mon.getTime());
        mon.add(Calendar.MONTH , -12);
        String fromYM = new java.text.SimpleDateFormat("yyyy-MM").format(mon.getTime());

        setStartYM(fromYM);
        setEndYM(toYM);

        transactionlList = new MediatorLiveData<>();


    }


    public MutableLiveData<ArrayList<NemoSalesTransactionListRO>> getTransactionlList() {
        return transactionlList;
    }

    public MutableLiveData<ArrayList<NemoHospitalSearchRO>> getVisitPlanHospitalList() {
        return visitPlanHospitalList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setHospital(NemoHospitalSearchRO hospitalInfo) {

        this.selHospital = hospitalInfo;

        apiCustomerInfo();

        apiTransactionList();

        apiAmtTransactionList();

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

    // 미수금 가져오기
    public void apiAmtTransactionList()
    {
        this.progressFlag.setValue(true);

        // 거래 대장
        NemoSalesTransactionListPO param = new NemoSalesTransactionListPO();

        Calendar mon = Calendar.getInstance();
        String toYM = new java.text.SimpleDateFormat("yyyyMM").format(mon.getTime());

        param.setCustCd(selHospital.getCustCd());
        param.setJobYmFrom(toYM);
        param.setJobYmTo(toYM);

        AndroidUtil.log("NemoSalesTransactionListPO : " + param);

        api.getSalesTransactionList(param, amtApiHandler);
    }

    public void apiSingImageAdd(byte[] outArray, NemoSalesTransactionListRO item)
    {

        String today = DateUtil.getFormatString(new Date(), "yyyyMM");

        NemoSignImageAddPO param = new NemoSignImageAddPO();
        param.setUserId(userId);
        param.setUpprDeptCd(upperDeptCd);
        param.setDeptCd(deptCode);
        param.setCustCd(selHospital.getCustCd());
        param.setJobYm(today);

        String imageBase64 = Base64.getEncoder().encodeToString(outArray);

        param.setImageBase64(imageBase64);

        AndroidUtil.log("NemoSignImageAddPO : " + param);

        api.addSignImage(param, signApiHandler);

    }

    public void apiCustomerInfo()
    {

        progressFlag.setValue(true);

        // 고객 기본 정보
        NemoCustomerInfoPO param = new NemoCustomerInfoPO();

        param.setCustCd(selHospital.getCustCd());

        api.getCustomerInfo(param, apiHandler);
    }

    public void setHospitalInfo(NemoCustomerInfoRO hospitalInfo) {
        this.hospitalInfo.setValue(hospitalInfo);
    }

    public MutableLiveData<NemoCustomerInfoRO> getHospitalInfo() {
        return hospitalInfo;
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

    public String getAmtMonth() {
        return amtMonth;
    }
}