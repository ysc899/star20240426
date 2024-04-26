package kr.co.seesoft.nemo.starnemoapp.ui.customersupport.add;

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
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoCodePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportCodePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportReceiptPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoEmptyListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportReceiptRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class CustomerSupportAddViewModel extends AndroidViewModel {

    // 요청유형 목록
    private MutableLiveData<ArrayList<NemoCustomerMemoCodeListRO>> codeList;

    private MutableLiveData<Date> cpRecpDt;
    private MutableLiveData<String> cpRecpDtYmd;

    private MutableLiveData<Boolean> updateFlag;

    private MutableLiveData<NemoCustomerSupportReceiptRO> receiptInfo;
    private MutableLiveData<Boolean> progressFlag;
    private ArrayList<NemoHospitalSearchRO> baseHospitalList;

    private Application application;

    private String userId;
    private String deptCode;

    private NemoAPI api;
    private Handler saveHandler, codeHandler, infoHandler;


    public CustomerSupportAddViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        updateFlag = new MediatorLiveData<>();
        updateFlag.setValue(false);

        api = new NemoAPI(application);

        codeList = new MediatorLiveData<>();

        cpRecpDt = new MediatorLiveData<>();
        cpRecpDtYmd = new MediatorLiveData<>();

        receiptInfo = new MediatorLiveData<>();

        this.setCpRecpDt(new Date());


        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");

        saveHandler = new Handler(Looper.myLooper()){
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

                            updateFlag.setValue(true);

                        }else{
                            AndroidUtil.toast(application, "전송중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        }
                        break;
                    default:
                        AndroidUtil.toast(application, "전송중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        break;
                }

            }
        };

        codeHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoCustomerMemoCodeListRO> codeListTemp = (ArrayList<NemoCustomerMemoCodeListRO>)msg.obj;
                        codeList.setValue(codeListTemp);
                        break;
                    default:
//                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        infoHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        NemoCustomerSupportReceiptRO intoTemp = (NemoCustomerSupportReceiptRO)msg.obj;
                        receiptInfo.setValue(intoTemp);
                        break;
                    default:
//                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        apiCodeList();


    }

    public void apiCodeList()
    {
        NemoCustomerSupportCodePO param = new NemoCustomerSupportCodePO();

        param.setCtgrId(Const.CUSTOMER_TYPE_CTGR_ID);

        AndroidUtil.log("NemoCustomerSupportCodePO : " + param);

        this.progressFlag.setValue(true);
        api.getCustomerSupportCodeList(param,codeHandler);
    }

    public MutableLiveData<ArrayList<NemoCustomerMemoCodeListRO>> getCodeList() {
        return codeList;
    }

    public MutableLiveData<NemoCustomerSupportReceiptRO> getReceiptInfo() {
        return receiptInfo;
    }

    public void setCpRecpDt(Date searchDate) {
        this.cpRecpDt.setValue(searchDate);
        this.cpRecpDtYmd.setValue(DateUtil.getFormatString(this.cpRecpDt.getValue(), "yyyy년 MM월 dd일"));
    }

    public MutableLiveData<Date> getCpRecpDt() {
        return cpRecpDt;
    }

    public MutableLiveData<String> getCpRecpDtYmd() {
        return cpRecpDtYmd;
    }

    public void getCustomerInfo(String recpNo)
    {
        progressFlag.setValue(true);

        NemoCustomerSupportReceiptPO param = new NemoCustomerSupportReceiptPO();

        Date drptDt = this.getCpRecpDt().getValue();
        param.setRecpDt(DateUtil.getFormatString(drptDt, "yyyyMMdd"));
        param.setRecpNo(recpNo);

        AndroidUtil.log("NemoCustomerSupportReceiptPO : "+ param);


        api.getCustomerSupportReceiptInfo(param, infoHandler);
    }

    public void apiSave(NemoCustomerSupportPO param)
    {
        progressFlag.setValue(true);

        param.setUserId(userId);

        AndroidUtil.log("NemoCustomerSupportPO : "+ param);


        api.setCustomerSupport(param, saveHandler);
    }



    public String getCustSuptUpdtRqstTypeCd(int inNm)
    {
        String rValue = "";

        try
        {
            rValue = codeList.getValue().get(inNm).getCmmnCd();
        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

        return rValue;
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

    public void clear()
    {
        receiptInfo = new MediatorLiveData<>();
    }


}