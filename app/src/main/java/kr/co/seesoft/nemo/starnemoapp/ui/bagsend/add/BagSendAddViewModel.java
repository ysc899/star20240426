package kr.co.seesoft.nemo.starnemoapp.ui.bagsend.add;

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
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoEmptyListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class BagSendAddViewModel extends AndroidViewModel {

    // 발송 지점
    private MutableLiveData<ArrayList<NemoDeptCdNmListRO>> branchList;
    // 도착 센터
    private MutableLiveData<ArrayList<NemoDeptCdNmListRO>> centerList;
    // 운송 구분
    private MutableLiveData<ArrayList<NemoCdNmListRO>> transportationList;
    // 운송회사
    private MutableLiveData<ArrayList<NemoCdNmListRO>> transportCompanyList;

    private MutableLiveData<Date> cpbgSendDt;
    private MutableLiveData<String> cpbgSendDtYmd;

    private MutableLiveData<Date> cpbgArvlPragDt;
    private MutableLiveData<String> cpbgArvlPragDtYmd;

    private MutableLiveData<String> cpbgSendTm;
    private MutableLiveData<String> cpbgArvlPragTm;

    private MutableLiveData<Boolean> updateFlag;


    private Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    Gson gson = new Gson();

    //    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanHospitalList;
    private MutableLiveData<ArrayList<NemoHospitalSearchRO>> visitPlanHospitalList;
    private MutableLiveData<Boolean> progressFlag;
    private ArrayList<NemoHospitalSearchRO> baseHospitalList;

    private Application application;

    private String userId;
    private String deptCode;

    private NemoAPI api;
    private Handler saveHandler, sendHandler, branchHandler, centerHandler, transportationHandler, transportCompanyHandler;


    public BagSendAddViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        updateFlag = new MediatorLiveData<>();
        updateFlag.setValue(false);

        api = new NemoAPI(application);

        branchList = new MediatorLiveData<>();
        centerList = new MediatorLiveData<>();
        transportationList = new MediatorLiveData<>();
        transportCompanyList = new MediatorLiveData<>();

        cpbgSendDt = new MediatorLiveData<>();
        cpbgSendDtYmd = new MediatorLiveData<>();
        cpbgArvlPragDt = new MediatorLiveData<>();
        cpbgArvlPragDtYmd = new MediatorLiveData<>();

        cpbgSendTm = new MediatorLiveData<>();
        cpbgArvlPragTm = new MediatorLiveData<>();

        this.setCpbgSendDt(new Date());
        this.setCpbgArvlPragDt(new Date());


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

        branchHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoDeptCdNmListRO> branchListTemp = (ArrayList<NemoDeptCdNmListRO>)msg.obj;
                        branchList.setValue(branchListTemp);

                        break;
                    default:
//                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        centerHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoDeptCdNmListRO> centerListTemp = (ArrayList<NemoDeptCdNmListRO>)msg.obj;
                        centerList.setValue(centerListTemp);

                        break;
                    default:
//                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        transportationHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoCdNmListRO> transportationListTemp = (ArrayList<NemoCdNmListRO>)msg.obj;
                        transportationList.setValue(transportationListTemp);

                        break;
                    default:
//                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        transportCompanyHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        AndroidUtil.log("===============> " + msg.obj);

                        ArrayList<NemoCdNmListRO> transportCompanyListTemp = (ArrayList<NemoCdNmListRO>)msg.obj;
                        transportCompanyList.setValue(transportCompanyListTemp);

                        break;
                    default:
//                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };


        Calendar mon = Calendar.getInstance();
        String tohhmm = new java.text.SimpleDateFormat("HH:mm").format(mon.getTime());
        mon.add(Calendar.HOUR , 5);
        String fromhhmm = new java.text.SimpleDateFormat("HH:mm").format(mon.getTime());

        setCpbgSendTm(tohhmm);
        setCpbgArvlPragTm(fromhhmm);

        AndroidUtil.log("tohhmm : " + tohhmm);
        AndroidUtil.log("fromhhmm : " + fromhhmm);


        // 출발 지점 코드
        apiBranchList();
        // 도착 센터 코드
        apiCenterList();
        // 운송 구분 코드
        apiTransportationList();
        // 운송 회사 코드
        apiTransportCompanyList();
    }

    public void apiBranchList()
    {
        NemoEmptyListPO param = new NemoEmptyListPO();

        api.getBranchList(param,branchHandler);
    }

    public void apiCenterList()
    {
        NemoEmptyListPO param = new NemoEmptyListPO();

        api.getCenterList(param,centerHandler);
    }

    public void apiTransportationList()
    {
        NemoEmptyListPO param = new NemoEmptyListPO();

        api.getTransportationList(param,transportationHandler);
    }

    public void apiTransportCompanyList()
    {
        NemoEmptyListPO param = new NemoEmptyListPO();

        api.getTransportCompanyList(param,transportCompanyHandler);
    }

    public void apiBagSend(NemoBagSendAddPO param)
    {
        progressFlag.setValue(true);

        //Date sendDt = new Date();
        //param.setCpbgSendDt(DateUtil.getFormatString(sendDt, "yyyyMMdd"));

        //param.setCpbgSendDeptCd(deptCode);

        Date drptDt = this.getCpbgSendDt().getValue();
        param.setCpbgDprtDt(DateUtil.getFormatString(drptDt, "yyyyMMdd"));
        param.setCpbgSendTm(cpbgSendTm.getValue().replace(":",""));

        Date arvDt = this.getCpbgArvlPragDt().getValue();
        param.setCpbgArvlPragDt(DateUtil.getFormatString(arvDt, "yyyyMMdd"));
        param.setCpbgArvlPragTm(cpbgArvlPragTm.getValue().replace(":",""));

        param.setUserId(userId);

        AndroidUtil.log("NemoBagSendAddPO : "+ param);


        api.addBagSend(param, saveHandler);
    }

    public MutableLiveData<ArrayList<NemoDeptCdNmListRO>> getBranchList() {
        return branchList;
    }

    public MutableLiveData<ArrayList<NemoDeptCdNmListRO>> getCenterList() {
        return centerList;
    }

    public MutableLiveData<ArrayList<NemoCdNmListRO>> getTransportationList() {
        return transportationList;
    }

    public MutableLiveData<ArrayList<NemoCdNmListRO>> getTransportCompanyList() {
        return transportCompanyList;
    }

    public void setCpbgSendDt(Date searchDate) {
        this.cpbgSendDt.setValue(searchDate);
        this.cpbgSendDtYmd.setValue(DateUtil.getFormatString(this.cpbgSendDt.getValue(), "yyyy년 MM월 dd일"));
    }

    public MutableLiveData<Date> getCpbgSendDt() {
        return cpbgSendDt;
    }

    public void setCpbgArvlPragDt(Date searchDate) {
        this.cpbgArvlPragDt.setValue(searchDate);
        this.cpbgArvlPragDtYmd.setValue(DateUtil.getFormatString(this.cpbgArvlPragDt.getValue(), "yyyy년 MM월 dd일"));
    }

    public MutableLiveData<Date> getCpbgArvlPragDt() {
        return cpbgArvlPragDt;
    }

    public MutableLiveData<String> getCpbgSendDtYmd() {
        return cpbgSendDtYmd;
    }

    public MutableLiveData<String> getCpbgArvlPragDtYmd() {
        return cpbgArvlPragDtYmd;
    }

    public void setCpbgSendTm(String cpbgSendTm) {
        this.cpbgSendTm.setValue(cpbgSendTm);
    }

    public MutableLiveData<String> getCpbgSendTm() {
        return cpbgSendTm;
    }

    public void setCpbgArvlPragTm(String cpbgArvlPragTm) {
        this.cpbgArvlPragTm.setValue(cpbgArvlPragTm);
    }

    public MutableLiveData<String> getCpbgArvlPragTm() {
        return cpbgArvlPragTm;
    }

    public String getCpbgSendDeptCd(int inNm)
    {
        String rValue = "";

        try
        {
            rValue = branchList.getValue().get(inNm).getDeptCd();
        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

        return rValue;
    }

    public String getCpbgArvlLocCntrCd(int inNm)
    {
        String rValue = "";

        try
        {
            rValue = centerList.getValue().get(inNm).getDeptCd();
        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

        return rValue;
    }

    public String getCpbgTrptDivCd(int inNm)
    {
        String rValue = "";

        try
        {
            rValue = transportationList.getValue().get(inNm).getCode();
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


}