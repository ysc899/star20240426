package kr.co.seesoft.nemo.starnemoapp.ui.transaction.payment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesDepositListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesApprovalListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

public class TransactionPaymentHistoryViewModel extends AndroidViewModel {

    /** 병원 정보 */
    private NemoHospitalSearchRO selHospital;

    private MutableLiveData<ArrayList<NemoSalesApprovalListRO>> adapterlList;
    private MutableLiveData<Boolean> progressFlag;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler apiHandler, cancelApiHandler;

    private MutableLiveData<String> searchYM;


    public TransactionPaymentHistoryViewModel(@NonNull Application application) {
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

                        ArrayList<NemoSalesApprovalListRO> adapterList = (ArrayList<NemoSalesApprovalListRO>)msg.obj;
                        adapterlList.setValue(adapterList);
                        break;
                    default:
                        //AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        cancelApiHandler = new Handler(Looper.myLooper()){
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
                            AndroidUtil.toast(application, "취소 되었습니다.");

                            apiSalesApprovalList();

                        }else{
                            AndroidUtil.toast(application, "취소 등록중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        }
                        break;
                    default:
                        AndroidUtil.toast(application, "취소 등록중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        break;
                }

            }
        };

        Calendar mon = Calendar.getInstance();
        String searchYM = new java.text.SimpleDateFormat("yyyy-MM").format(mon.getTime());

        setSearchYM(searchYM);


        adapterlList = new MediatorLiveData<>();


    }

    public void setHospital(NemoHospitalSearchRO hospitalInfo)
    {
        this.selHospital = hospitalInfo;

        apiSalesApprovalList();
    }

    public void apiSalesApprovalList()
    {
        progressFlag.setValue(true);

        // List
        NemoSalesApprovalListPO param = new NemoSalesApprovalListPO();

        param.setCustCd(selHospital.getCustCd());
        param.setSearchYm(this.searchYM.getValue().replace("-",""));

        AndroidUtil.log("NemoSalesApprovalListPO : " + param);

        api.getApprovalList(param, apiHandler);
    }

    public void apiAddApproval(NemoSalesApprovalAddPO param)
    {

        param.setCustCd(selHospital.getCustCd());
        //param.setChgrId(userId);
        param.setUserId(userId);

        AndroidUtil.log("NemoSalesApprovalAddPO : " + param);

        api.addApproval(param, cancelApiHandler);
    }

    public MutableLiveData<ArrayList<NemoSalesApprovalListRO>> getAdapterlList() {
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