package kr.co.seesoft.nemo.starnemoapp.ui.visitplan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleAddListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleOrderUpdatePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoVisitAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class VisitPlanViewModel extends AndroidViewModel {

    private MutableLiveData<String> visPlanYmd;
    private MutableLiveData<Date> visPlanDate;
    private MutableLiveData<ArrayList<NemoScheduleListRO>> visitPlanList;
    private MutableLiveData<Boolean> updateFlag;

    private Application application;
    private MutableLiveData<Boolean> progressFlag;

    private String userId;
    private String deptCode;
    private String upprDeptCode;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler sendApiHandler;
    private Handler updateApiHandler;
    private Handler deleteApiHandler;

    private boolean isBeforeDelete = false;


    /** db */
    private NemoRepository nemoRepository;
    /** 날짜별 사진 촬영 리스트 */
    private MediatorLiveData<List<PictureVO>> pictureDatas;

    private Gson gson;

    public VisitPlanViewModel(@NonNull Application application) {
        super(application);

        AndroidUtil.log("===============> VisitPlanViewModel()");

        this.application = application;

        gson = new Gson();

        visPlanDate = new MediatorLiveData<>();
        visPlanYmd = new MutableLiveData<>();
        visitPlanList = new MediatorLiveData<>();
        updateFlag = new MediatorLiveData<>();
        updateFlag.setValue(false);
        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);

        pictureDatas = new MediatorLiveData<>();
        pictureDatas.setValue(new ArrayList<>());

        api = new NemoAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");
        upprDeptCode = sp.getString(AndroidUtil.SP_LOGIN_UPPR_DEPT, "");

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        ArrayList<NemoScheduleListRO> vPlanList = (ArrayList<NemoScheduleListRO>)msg.obj;


                        if(vPlanList.size() > 0) {
                            vPlanList.sort(NemoScheduleListRO::compareTo);

                            AtomicInteger editOrder = new AtomicInteger(1);

                            vPlanList.forEach(it -> {
                                it.setOrder(editOrder.getAndIncrement());
                            });
                        }


                        visitPlanList.setValue(vPlanList);

                        dispList();

                        break;
                    default:
                        AndroidUtil.toast(application, "등록된 병원 정보가 없습니다.");
                        break;
                }

            }
        };

        sendApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        NemoResultRO addObj = (NemoResultRO)msg.obj;
                        if("00020".equals(addObj.getMessageId())) {
                            AndroidUtil.toast(application, "등록 되었습니다.");
                            getAPIVisPlanListData();

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

        updateApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        NemoResultRO addObj = (NemoResultRO)msg.obj;
                        if("00030".equals(addObj.getMessageId())) {
                            AndroidUtil.toast(application, "수정 되었습니다.");
                            getAPIVisPlanListData();

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

        deleteApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:
                        NemoResultRO addObj = (NemoResultRO)msg.obj;
                        if("00040".equals(addObj.getMessageId())) {
                            AndroidUtil.toast(application, "삭제 되었습니다.");

                            if( isBeforeDelete )
                            {
                                sendPlanListData();

                                isBeforeDelete = false;
                            }
                            else
                            {
                                getAPIVisPlanListData();
                            }

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


        this.setVisPlanDate(new Date());
    }

    public LiveData<String> getVisPlanYmd() {
        return visPlanYmd;
    }

    public void setVisPlanDate(Date visPlanDate) {
        this.visPlanDate.setValue(visPlanDate);
        this.visPlanYmd.setValue(DateUtil.getFormatString(this.visPlanDate.getValue(), "yyyy년MM월dd일"));
        this.getAPIVisPlanListData();
//        this.setNemoRepository();
    }

    public void calcVisPlanDate(int day){
        this.setVisPlanDate(DateUtil.addDay(this.visPlanDate.getValue(), day));
    }

    public MutableLiveData<Date> getVisPlanDate() {
        return visPlanDate;
    }

    public void getAPIVisPlanListData(){

        this.progressFlag.setValue(true);
        NemoScheduleListPO param = new NemoScheduleListPO();
        param.setUserId(userId);

        Date d = this.visPlanDate.getValue();

        param.setSmplTakePlanDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        api.searchScheduleList(param, apiHandler);
        setUpdateFlag(false);
    }

    public void setVisPlanListData(ArrayList<NemoScheduleListRO> visitPlanList){
        this.visitPlanList.setValue(visitPlanList);
    }

    public void setEditVisPlanListData(ArrayList<NemoScheduleListRO> visitPlanList){
        visitPlanList.sort(NemoScheduleListRO::compareTo);
        int count = 1;
        for (NemoScheduleListRO t : visitPlanList) {
            t.setOrder(count++);
        }

        this.visitPlanList.setValue(visitPlanList);
    }

    public void updatePlanListData(){

        if( this.visitPlanList.getValue().size() > 0 )
        {
            ArrayList<NemoScheduleOrderUpdatePO> objParam = new ArrayList<>();

            for (NemoScheduleListRO t : this.visitPlanList.getValue()) {

                NemoScheduleOrderUpdatePO param = new NemoScheduleOrderUpdatePO(t.getSmplTakePlanDt(),t.getTakchgrEmpNo(),t.getSeqn(),t.getRndTakePlanUkeyid());

                objParam.add(param);

            }

            AndroidUtil.log("NemoScheduleOrderUpdatePO : " + objParam);

            api.scheduleOrderUpdate(objParam,updateApiHandler);
        }
        // 전체 삭제 처리
        else
        {
            apiDeleteAll();
        }


    }

    private void apiDeleteAll()
    {
        NemoScheduleListPO param = new NemoScheduleListPO();

        param.setUserId(userId);

        Date d = this.visPlanDate.getValue();

        param.setSmplTakePlanDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        AndroidUtil.log("NemoScheduleListPO : " + param);

        api.scheduleAllDelete(param,deleteApiHandler);
    }

    public void addVisPlanListData(ArrayList<NemoScheduleAddListPO> visitPlanAddList){

        NemoScheduleAddPO param = new NemoScheduleAddPO();
        param.setUserId(userId);
        param.setDeptCd(deptCode);
        param.setUpprDeptCd(upprDeptCode);

        Date d = this.visPlanDate.getValue();

        param.setSmplTakePlanDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        param.setCustomerRequestDTOList(visitPlanAddList);

        AndroidUtil.log("NemoScheduleAddPO : " + param);

        api.scheduleAdd(param,sendApiHandler);

    }

//    public void addVisPlanListData(ArrayList<NemoScheduleListRO> visitPlanAddList){
//        int count = this.visitPlanList.getValue().size() +1;
//
//        List<NemoScheduleListRO> addList = visitPlanAddList.stream().filter(t -> {
//
//            AndroidUtil.log(visitPlanList.getValue().toString());
//
//            long cnt = visitPlanList.getValue().stream().filter(b -> {
//                return b.getCustCd().equals(t.getCustCd());
//            }).count();
//
//            AndroidUtil.log("카운트 : " + cnt);
//            AndroidUtil.log("카운트 : " + (cnt == 0l));
//
//            return visitPlanList.getValue().stream().filter(b -> {
//                return b.getCustCd().equals(t.getCustCd());
//            }).count() == 0l;
//        }).collect(Collectors.toList());
//
//        for (NemoScheduleListRO add : addList) {
//            add.setSeqn(count++);
//        }
//
//        this.visitPlanList.getValue().addAll(addList);
//        setUpdateFlag(true);
//    }

    public MutableLiveData<ArrayList<NemoScheduleListRO>> getVisitPlanList() {
        return visitPlanList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }

    public void sendBeforeDeleteAll()
    {
        isBeforeDelete = true;

        apiDeleteAll();

    }

    public void sendPlanListData(){

        this.progressFlag.setValue(true);
        NemoScheduleAddPO param = new NemoScheduleAddPO();
        param.setUserId(userId);
        param.setDeptCd(deptCode);
        param.setUpprDeptCd(upprDeptCode);
        Date d = this.visPlanDate.getValue();
        param.setSmplTakePlanDt(DateUtil.getFormatString(d, "yyyyMMdd"));

        ArrayList<NemoScheduleAddListPO> visitInfos = new ArrayList<>();

        this.visitPlanList.getValue().forEach(t ->{
            visitInfos.add(new NemoScheduleAddListPO(t.getCustCd(), t.getCustNm(), t.getPrjtCd()));
        });

        param.setCustomerRequestDTOList(visitInfos);

        AndroidUtil.log("NemoScheduleAddPO : " + param);

        api.scheduleAdd(param, sendApiHandler);

    }

    public MutableLiveData<Boolean> getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag.setValue( updateFlag);
    }

    public void setNemoRepository(LifecycleOwner owner){

        Date d = this.visPlanDate.getValue();

        String ymd = DateUtil.getFormatString(d, "yyyyMMdd");

        AndroidUtil.log("ymd : " + ymd);

        this.nemoRepository = new NemoRepository(application, ymd);
        this.nemoRepository.getHospitalYmdDatas().observe(owner, new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {

//                AndroidUtil.log("pictureVOS : " + pictureVOS);

                pictureDatas.setValue(pictureVOS);
            }
        });

    }

    public LiveData<List<PictureVO>> getPictureDatas() {
        return pictureDatas;
    }

    public void dispList()
    {
        ArrayList<NemoScheduleListRO> vPlanList = visitPlanList.getValue();

        List<PictureVO> pDatas = getPictureDatas().getValue();

        for(int i = 0 ; i < vPlanList.size() ; i++)
        {
            NemoScheduleListRO t = vPlanList.get(i);

            List<PictureVO> hList = pDatas.stream().filter(it -> {
                return it.hospitalKey.equals(t.getCustCd());
            }).collect(Collectors.toList());

            if(hList.size() > 0){
                int noSendCount = (int)hList.stream().filter(it ->{return !it.sendFlag;}).count();
                if(noSendCount == 0){
                    t.setOrder(10000);
                }
            }
        }

        if(vPlanList.size() > 0) {
            vPlanList.sort(NemoScheduleListRO::compareTo);

            AtomicInteger editOrder = new AtomicInteger(1);

            vPlanList.forEach(it -> {
                it.setOrder(editOrder.getAndIncrement());
            });
        }

        visitPlanList.setValue(vPlanList);

    }


    public void setClear()
    {
        this.setVisPlanDate(this.visPlanDate.getValue());
    }

}