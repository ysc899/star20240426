package kr.co.seesoft.nemo.starnemo.ui.visitplan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kr.co.seesoft.nemo.starnemo.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoVisitAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoVisitListPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.DateUtil;

public class VisitPlanViewModel extends AndroidViewModel {

    private MutableLiveData<String> visPlanYmd;
    private MutableLiveData<Date> visPlanDate;
    private MutableLiveData<ArrayList<NemoVisitListRO>> visitPlanList;
    private MutableLiveData<Boolean> updateFlag;

    private Application application;
    private MutableLiveData<Boolean> progressFlag;

    private String userId;
    private String deptCode;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler sendApiHandler;


    /** db */
    private NemoRepository nemoRepository;
    /** 날짜별 사진 촬영 리스트 */
    private MediatorLiveData<List<PictureVO>> pictureDatas;

    private Gson gson;

    public VisitPlanViewModel(@NonNull Application application) {
        super(application);
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

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                        ArrayList<NemoVisitListRO> vPlanList = (ArrayList<NemoVisitListRO>)msg.obj;


                        if(vPlanList.size() > 0) {
                            vPlanList.sort(NemoVisitListRO::compareTo);

                            if (vPlanList.get(0).getOrder() == 0) {

                                AtomicInteger editOrder = new AtomicInteger(1);

                                vPlanList.forEach(it -> {
                                    it.setOrder(editOrder.getAndIncrement());
                                    it.setSeq(it.getOrder());
                                });

                            }
                        }

                        visitPlanList.setValue(vPlanList);

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

                        boolean addFlag = (Boolean)msg.obj;
                        if(addFlag) {
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

    private void getAPIVisPlanListData(){

        this.progressFlag.setValue(true);
        NemoVisitListPO param = new NemoVisitListPO();
        param.setUserId(userId);

        Date d = this.visPlanDate.getValue();

        param.setYmd(DateUtil.getFormatString(d, "yyyyMMdd"));

        api.searchVisitList(param, apiHandler);
        setUpdateFlag(false);
    }

    public void setVisPlanListData(ArrayList<NemoVisitListRO> visitPlanList){
        this.visitPlanList.setValue(visitPlanList);
    }
    public void setEditVisPlanListData(ArrayList<NemoVisitListRO> visitPlanList){
        visitPlanList.sort(NemoVisitListRO::compareTo);
        int count = 1;
        for (NemoVisitListRO t : visitPlanList) {
            t.setOrder(count++);
//            t.order = ;
        }

        this.visitPlanList.setValue(visitPlanList);
    }
    public void addVisPlanListData(ArrayList<NemoVisitListRO> visitPlanAddList){
        int count = this.visitPlanList.getValue().size() +1;

        List<NemoVisitListRO> addList = visitPlanAddList.stream().filter(t -> {

            AndroidUtil.log(visitPlanList.getValue().toString());

            long cnt = visitPlanList.getValue().stream().filter(b -> {
                return b.getHospitalCode().equals(t.getHospitalCode());
            }).count();

            AndroidUtil.log("카운트 : " + cnt);
            AndroidUtil.log("카운트 : " + (cnt == 0l));

            return visitPlanList.getValue().stream().filter(b -> {
                return b.getHospitalCode().equals(t.getHospitalCode());
            }).count() == 0l;
        }).collect(Collectors.toList());

        for (NemoVisitListRO add : addList) {
            add.setOrder(count++);
        }

        this.visitPlanList.getValue().addAll(addList);
        setUpdateFlag(true);
    }

    public MutableLiveData<ArrayList<NemoVisitListRO>> getVisitPlanList() {
        return visitPlanList;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }


    public void sendPlanListData(){

        this.progressFlag.setValue(true);
        NemoVisitAddPO param = new NemoVisitAddPO();
        param.setUserId(userId);
        Date d = this.visPlanDate.getValue();
        param.setYmd(DateUtil.getFormatString(d, "yyyyMMdd"));
        param.setDeptCode(deptCode);

        ArrayList<NemoVisitAddPO.VisitInfo> visitInfos = new ArrayList<>();

        this.visitPlanList.getValue().forEach(t ->{
            visitInfos.add(new NemoVisitAddPO.VisitInfo(t.getOrder(), t.getHospitalCode()));
        });

        param.setVisitJSON(gson.toJson(visitInfos));

        api.searchVisitAdd(param, sendApiHandler);

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

        this.nemoRepository = new NemoRepository(application, ymd);
        this.nemoRepository.getHospitalYmdDatas().observe(owner, new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                pictureDatas.setValue(pictureVOS);
            }
        });

    }

    public LiveData<List<PictureVO>> getPictureDatas() {
        return pictureDatas;
    }

}