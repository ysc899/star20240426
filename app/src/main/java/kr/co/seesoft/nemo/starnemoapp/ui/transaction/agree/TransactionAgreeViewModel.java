package kr.co.seesoft.nemo.starnemoapp.ui.transaction.agree;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoTermsSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultImageAddRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class TransactionAgreeViewModel extends AndroidViewModel {

    /** 병원 접수 정보 */
    private MutableLiveData<NemoCustomerInfoRO> hospitalInfo;

    private NemoHospitalSearchRO selHospital;

    private MutableLiveData<Boolean> progressFlag;

    private Application application;

    private String userId, deptCode, upperDeptCd;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler apiAddHandler;


    public TransactionAgreeViewModel(@NonNull Application application) {
        super(application);

        this.application = application;

        hospitalInfo = new MediatorLiveData<>();

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

        apiAddHandler = new Handler(Looper.myLooper()){
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

                            apiCustomerInfo();
                        }
                        else
                        {
                            AndroidUtil.toast(application, result.getMessageContent());
                        }

                        break;
                    default:
                        AndroidUtil.toast(application, "전송중 에러가 발생 하였습니다. 잠시 후 다시 시도해주세요.");
                        break;
                }

            }
        };
    }

    public void setHospital(NemoHospitalSearchRO hospitalInfo)
    {
        this.selHospital = hospitalInfo;

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

    public void apiSingImageAdd(byte[] outArray)
    {
        progressFlag.setValue(true);

        Date d = new Date();

        NemoTermsSignImageAddPO param = new NemoTermsSignImageAddPO();
        param.setUserId(userId);
        param.setUpprDeptCd(upperDeptCd);
        param.setDeptCd(deptCode);
        param.setCustCd(hospitalInfo.getValue().getCustCd());
        param.setTrmsCd(hospitalInfo.getValue().getTrmsCd());

        String imageBase64 = Base64.getEncoder().encodeToString(outArray);

        param.setImageBase64(imageBase64);

        AndroidUtil.log("NemoSignImageAddPO : " + param);

        api.addTermsSignImage(param, apiAddHandler);

    }


    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

}