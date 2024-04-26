package kr.co.seesoft.nemo.starnemoapp.ui.contact;

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

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoDepartmentContactListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDepartmentContactListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class DepartmentContactViewModel extends AndroidViewModel {


    private MutableLiveData<ArrayList<NemoDepartmentContactListRO>> departmentContactList;
    private MutableLiveData<Boolean> progressFlag;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler apiHandler;

    // 현재 페이지
    private int currentPage = 1;


    public DepartmentContactViewModel(@NonNull Application application) {
        super(application);

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

                        ArrayList<NemoDepartmentContactListRO> tempList = (ArrayList<NemoDepartmentContactListRO>)msg.obj;

                        if( currentPage > 1 )
                        {
                            // 현재 MutableLiveData의 값을 가져옴
                            ArrayList<NemoDepartmentContactListRO> currentData = departmentContactList.getValue();

                            // 가져온 데이터를 현재 데이터에 추가
                            currentData.addAll(tempList);

                            // MutableLiveData의 값을 변경하여 LiveData를 갱신
                            departmentContactList.setValue(currentData);
                        }
                        else
                        {
                            departmentContactList.setValue(tempList);
                        }

                        break;
                    default:
                        //AndroidUtil.toast(application, "등록된 정보가 없습니다.");
                        break;
                }

            }
        };


        departmentContactList = new MediatorLiveData<>();

    }


    public MutableLiveData<ArrayList<NemoDepartmentContactListRO>> getDepartmentContactList() {
        return departmentContactList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void apiDepartmentContact(String deptNm , String userNm){

        this.progressFlag.setValue(true);

        NemoDepartmentContactListPO param = new NemoDepartmentContactListPO();
        param.setDeptNm(deptNm);
        param.setUserNm(userNm);
        param.setPageIndex(currentPage);
        param.setPageSize(Const.PAGE_SIZE);

        AndroidUtil.log("NemoDepartmentContactListPO : " + param);

        api.getDepartmentContactList(param, apiHandler);
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public void setClear()
    {

        if( departmentContactList.getValue() != null && departmentContactList.getValue().size() > 0)
        {
            departmentContactList.getValue().clear();
        }

        currentPage = 1;

        apiDepartmentContact("","");
    }


}