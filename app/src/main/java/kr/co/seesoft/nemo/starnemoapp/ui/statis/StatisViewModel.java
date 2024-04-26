package kr.co.seesoft.nemo.starnemoapp.ui.statis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.IntStream;

import kr.co.seesoft.nemo.starnemoapp.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class StatisViewModel extends ViewModel {

    private MutableLiveData<String> visPlanYmd;
    private MutableLiveData<Date> visPlanDate;
    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanList;

    public StatisViewModel() {

        visPlanDate = new MediatorLiveData<>();
        visPlanYmd = new MutableLiveData<>();
        visitPlanList = new MediatorLiveData<>();

        this.setVisPlanDate(new Date());
    }

    public LiveData<String> getVisPlanYmd() {
        return visPlanYmd;
    }

    public void setVisPlanDate(Date visPlanDate) {
        this.visPlanDate.setValue(visPlanDate);
        this.visPlanYmd.setValue(DateUtil.getFormatString(this.visPlanDate.getValue(), "yyyy년MM월dd일"));
        this.getAPIVisPlanListData();
    }

    public void calcVisPlanDate(int day){
        this.setVisPlanDate(DateUtil.addDay(this.visPlanDate.getValue(), day));
    }

    public MutableLiveData<Date> getVisPlanDate() {
        return visPlanDate;
    }

    private void getAPIVisPlanListData(){

        ArrayList<VisitPlanRO> visitPlanList = new ArrayList<>();
        IntStream.range(1, 20).forEach(t -> {
            visitPlanList.add(new VisitPlanRO(t, DateUtil.getFormatString(this.visPlanDate.getValue(), "yyMMdd") + String.valueOf(t), "씨소프트 병원 " + String.valueOf(t), t));
        });

        this.visitPlanList.setValue(visitPlanList);

    }

    public void setEditVisPlanListData(ArrayList<VisitPlanRO> visitPlanList){
        visitPlanList.sort(VisitPlanRO::compareTo);
        int count = 1;
        for (VisitPlanRO t : visitPlanList) {
            t.order = count++;
        }

        this.visitPlanList.setValue(visitPlanList);
    }
    public void addVisPlanListData(ArrayList<VisitPlanRO> visitPlanAddList){
        int count = this.visitPlanList.getValue().size();

        for (VisitPlanRO add : visitPlanAddList) {
            add.order = count++;
        }

        this.visitPlanList.getValue().addAll(visitPlanAddList);
    }

    public MutableLiveData<ArrayList<VisitPlanRO>> getVisitPlanList() {
        return visitPlanList;
    }
}