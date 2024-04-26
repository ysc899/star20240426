package kr.co.seesoft.nemo.starnemoapp.ui.customer;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class CustomerViewModel extends AndroidViewModel {

    private Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    Gson gson = new Gson();

    //    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanHospitalList;
    private MutableLiveData<ArrayList<NemoHospitalSearchRO>> visitPlanHospitalList;
    private MutableLiveData<Boolean> progressFlag;
    private ArrayList<NemoHospitalSearchRO> baseHospitalList;

    private Application application;

    private String userId, userDeptCd;

    private NemoAPI api;
    private Handler apiHandler;

    // 현재 페이지
    private int currentPage;

    // 기본 Select 구분
    // my: 내 담당 병원만 조회
    // br: 지점 모든 병원 조회
    // rnd: rnd관련
    private MutableLiveData<String> selectType;

    private MutableLiveData<Integer> choiceType;
    private MutableLiveData<String> choiceText;


    public CustomerViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        api = new NemoAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        userDeptCd = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        ArrayList<NemoHospitalSearchRO> visitPlanList = (ArrayList<NemoHospitalSearchRO>)msg.obj;

                        if( currentPage > 1 )
                        {
                            // 현재 MutableLiveData의 값을 가져옴
                            ArrayList<NemoHospitalSearchRO> currentData = visitPlanHospitalList.getValue();

                            // 가져온 데이터를 현재 데이터에 추가
                            currentData.addAll(visitPlanList);

                            // MutableLiveData의 값을 변경하여 LiveData를 갱신
                            visitPlanHospitalList.setValue(currentData);
                        }
                        else
                        {
                            visitPlanHospitalList.setValue(visitPlanList);
                        }

//                        visitPlanHospitalList.setValue(visitPlanList);

//                        baseHospitalList = visitPlanListCopy(visitPlanList);
                        break;
                    default:
                        //AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };


        visitPlanHospitalList = new MediatorLiveData<>();

        selectType = new MediatorLiveData<>();
        choiceType = new MediatorLiveData<>();
        choiceText = new MediatorLiveData<>();

    }


    public MutableLiveData<ArrayList<NemoHospitalSearchRO>> getVisitPlanHospitalList() {
        return visitPlanHospitalList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setHospitals(){

        this.progressFlag.setValue(true);

        NemoHospitalSearchPO param = new NemoHospitalSearchPO();
        param.setUserId(userId);
        param.setDeptCd(userDeptCd);
        param.setType(selectType.getValue());
        param.setPageIndex(currentPage);
        param.setPageSize(Const.PAGE_SIZE);
        param.setCstatCd("");

        AndroidUtil.log("NemoHospitalSearchPO : " + param);

        api.searchHospital(param, apiHandler);
    }

    public void searchHospitals(final int searchType, final String searchKeyWord){

        this.progressFlag.setValue(true);

        NemoHospitalSearchPO param = new NemoHospitalSearchPO();
        param.setUserId(userId);
        param.setDeptCd(userDeptCd);
        param.setType(selectType.getValue());
        param.setPageIndex(currentPage);
        param.setPageSize(Const.PAGE_SIZE);
        param.setCstatCd("");

        param.setSearchType(AndroidUtil.getSearchHospitalType(searchType));
        param.setSearchTxt(searchKeyWord);

        AndroidUtil.log("NemoHospitalSearchPO : " + param);

        AndroidUtil.log("===> searchType : " + searchType);
        AndroidUtil.log("===> searchHospitals : " + param);

        setChoiceType(searchType);
        setChoiceText(searchKeyWord);

        api.searchHospital(param, apiHandler);

    }

    private ArrayList<NemoHospitalSearchRO> visitPlanListCopy(ArrayList<NemoHospitalSearchRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoHospitalSearchRO> toList = gson.fromJson(copyJSON, visitPlanListType);
        return toList;
    }

    public void setSelectType(String selectType){

        this.selectType.setValue(selectType);
    }

    public MutableLiveData<String> getSelectType() { return this.selectType; }

    public void setChoiceType(int choiceType){

        this.choiceType.setValue(choiceType);
    }

    public MutableLiveData<Integer> getChoiceType() { return this.choiceType; }

    public void setChoiceText(String choiceText){

        this.choiceText.setValue(choiceText);
    }

    public MutableLiveData<String> getChoiceText() { return this.choiceText; }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public void setClear()
    {

        if( visitPlanHospitalList.getValue() != null && visitPlanHospitalList.getValue().size() > 0)
        {
            visitPlanHospitalList.getValue().clear();
        }

        selectType.setValue("my");
        currentPage = 1;
        setChoiceType(0);
        setChoiceText("");
        setHospitals();
    }

}