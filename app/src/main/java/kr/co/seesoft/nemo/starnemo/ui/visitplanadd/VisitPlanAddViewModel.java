package kr.co.seesoft.nemo.starnemo.ui.visitplanadd;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.Const;

public class VisitPlanAddViewModel extends AndroidViewModel {

    private Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    Gson gson = new Gson();

    //    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanHospitalList;
    private MutableLiveData<ArrayList<NemoHospitalSearchRO>> visitPlanHospitalList;
    private MutableLiveData<Boolean> progressFlag;
    private ArrayList<NemoHospitalSearchRO> baseHospitalList;

    private Application application;

    private String userId;

    private NemoAPI api;
    private Handler apiHandler;


    public VisitPlanAddViewModel(@NonNull Application application) {
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
                        ArrayList<NemoHospitalSearchRO> visitPlanList = (ArrayList<NemoHospitalSearchRO>)msg.obj;
                        visitPlanHospitalList.setValue(visitPlanList);

                        baseHospitalList = visitPlanListCopy(visitPlanList);
                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };


        visitPlanHospitalList = new MediatorLiveData<>();
        setMyHospitals();
    }


    public MutableLiveData<ArrayList<NemoHospitalSearchRO>> getVisitPlanHospitalList() {
        return visitPlanHospitalList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void setMyHospitals(){

        this.progressFlag.setValue(true);

        NemoHospitalSearchPO param = new NemoHospitalSearchPO();
        param.setUserId(userId);
        param.setType("1");

        api.searchHospital(param, apiHandler);
    }

    public void setAllHospitals(){

        this.progressFlag.setValue(true);

        NemoHospitalSearchPO param = new NemoHospitalSearchPO();
        param.setUserId(userId);
        param.setType("2");

        api.searchHospital(param, apiHandler);
    }

    public void searchHospitals(final int searchType, final String searchKeyWord){


        if(StringUtils.isEmpty(searchKeyWord)){
            this.getVisitPlanHospitalList().setValue(visitPlanListCopy(baseHospitalList));
        }else{
            ArrayList<NemoHospitalSearchRO> visitPlanList = new ArrayList<>();

            if(searchType == Const.SEARCH_NAME){
                //병원명
                baseHospitalList.stream().filter(t -> {return t.getHospitalName().contains(searchKeyWord);}).forEach( t->{
                    visitPlanList.add(t);
                });
            }else if(searchType == Const.SEARCH_CODE){
                //코드
                baseHospitalList.stream().filter(t -> {return t.getHospitalCode().contains(searchKeyWord);}).forEach( t->{
                    visitPlanList.add(t);
                });
            }else if(searchType == Const.SEARCH_MANAGER){
                baseHospitalList.stream().filter(t -> {return t.getSaleManName().contains(searchKeyWord);}).forEach( t->{
                    visitPlanList.add(t);
                });
            }

            if(visitPlanList.size() == 0){
                AndroidUtil.toast(application, "검색 결과가 없습니다.");
            }
            this.getVisitPlanHospitalList().setValue(visitPlanList);

        }


    }

    private ArrayList<NemoHospitalSearchRO> visitPlanListCopy(ArrayList<NemoHospitalSearchRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoHospitalSearchRO> toList = gson.fromJson(copyJSON, visitPlanListType);
        return toList;
    }

}