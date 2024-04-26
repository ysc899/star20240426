package kr.co.seesoft.nemo.starnemoapp.ui.transaction.paysend;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesBillSendPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesDepositListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionPaysendViewModel extends AndroidViewModel {

    private Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    Gson gson = new Gson();

    /** 병원 정보 */
    private NemoHospitalSearchRO selHospital;

    private MutableLiveData<NemoCustomerInfoRO> hospitalInfo;

    private MutableLiveData<Boolean> progressFlag;

    private MutableLiveData<Boolean> updateFlag;

    private Application application;

    private String userId, deptCode, upperDeptCd;

    private MutableLiveData<String> jobYM;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler apiSendHandler;


    public TransactionPaysendViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        jobYM = new MutableLiveData<>();

        hospitalInfo = new MediatorLiveData<>();

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        updateFlag = new MediatorLiveData<>();
        updateFlag.setValue(false);

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
                        //AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        apiSendHandler = new Handler(Looper.myLooper()){
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
                            AndroidUtil.toast(application, "청구서가 발송 되었습니다.");

                            updateFlag.setValue(true);
                        }
                        else
                        {
                            AndroidUtil.toast(application, result.getMessageContent());
                        }

                        break;
                    default:
                        AndroidUtil.toast(application, "청구서 발송중 오류가 발생 했습니다.");
                        break;
                }

            }
        };

        Calendar mon = Calendar.getInstance();
        String searchYM = new java.text.SimpleDateFormat("yyyy-MM").format(mon.getTime());

        setJobYM(searchYM);

    }

    public void apiSendBill(String email)
    {
        progressFlag.setValue(true);

        NemoSalesBillSendPO param = new NemoSalesBillSendPO();

        param.setUserId(userId);
        param.setCustCd(selHospital.getCustCd());
        param.setJobYm(this.jobYM.getValue().replace("-",""));
        param.setDocDivCd(Const.DOC_DIV_CD);
        param.setCustEmalAddr(email);

        AndroidUtil.log("NemoSalesBillSendPO  : " + param);

        api.sendSalesBill(param, apiSendHandler);
    }

    public void setHospital(NemoHospitalSearchRO hospitalInfo)
    {
        this.selHospital = hospitalInfo;

        updateFlag.setValue(false);

        apiCustomerInfo();
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

    public void setJobYM(String jobYM) {
        this.jobYM.setValue(jobYM);
    }

    public LiveData<String> getJobYM()
    {
        return this.jobYM;
    }


    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public MutableLiveData<Boolean> getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(boolean setValue)
    {
        updateFlag.setValue(setValue);
    }

}