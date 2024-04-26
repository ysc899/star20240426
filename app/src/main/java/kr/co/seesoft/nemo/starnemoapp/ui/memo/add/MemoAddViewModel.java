package kr.co.seesoft.nemo.starnemoapp.ui.memo.add;

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
import java.util.zip.Adler32;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoCodePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoEmptyListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class MemoAddViewModel extends AndroidViewModel {

    // 코드 목록
    private MutableLiveData<ArrayList<NemoCustomerMemoCodeListRO>> codeList;

    private MutableLiveData<Date> searchDate;
    private MutableLiveData<String> searchDateYmd;

    private MutableLiveData<Boolean> progressFlag;

    private MutableLiveData<Boolean> updateFlag;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler apiHandler, codeHandler;


    public MemoAddViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        updateFlag = new MediatorLiveData<>();
        updateFlag.setValue(false);

        api = new NemoAPI(application);

        codeList = new MediatorLiveData<>();

        searchDate = new MediatorLiveData<>();
        searchDateYmd = new MediatorLiveData<>();

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

                        NemoResultRO addObj = (NemoResultRO)msg.obj;
                        if("00020".equals(addObj.getMessageId())) {
                            AndroidUtil.toast(application, "등록 되었습니다.");

                            updateFlag.setValue(true);

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

        apiCodeList();

    }

    public void apiCodeList()
    {
        NemoCustomerMemoCodePO param = new NemoCustomerMemoCodePO();

        param.setCtgrId(Const.MEMO_CTGR_ID);

        AndroidUtil.log("NemoCustomerMemoCodePO : " + param);

        this.progressFlag.setValue(true);
        api.getMemoCodeList(param,codeHandler);
    }

    public MutableLiveData<ArrayList<NemoCustomerMemoCodeListRO>> getCodeList() {
        return codeList;
    }

    public void apiSendMemo(NemoHospitalSearchRO customer,String memo)
    {
        progressFlag.setValue(true);

        NemoCustomerMemoAddPO param = new NemoCustomerMemoAddPO();

        param.setUserId(userId);
        param.setCustCd(customer.getCustCd());

        Date searchDt = this.getSearchDate().getValue();

        param.setCmplRqstDt(DateUtil.getFormatString(searchDt, "yyyyMMdd"));

        param.setMemo(memo);

        AndroidUtil.log("NemoCustomerMemoAddPO : " + param);

        api.addMemo(param,apiHandler);
    }



    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
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

    public MutableLiveData<Boolean> getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(boolean setValue)
    {
        updateFlag.setValue(setValue);
    }

    public void setClear()
    {
        this.setSearchDate(new Date());
    }

}