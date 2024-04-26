package kr.co.seesoft.nemo.starnemoapp.ui.account;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.stream.IntStream;

import kr.co.seesoft.nemo.starnemoapp.api.ro.VisitPlanRO;

public class AccountViewModel extends ViewModel {


    private MutableLiveData<ArrayList<VisitPlanRO>> visitPlanHospitalList;

    public AccountViewModel() {

        visitPlanHospitalList = new MediatorLiveData<>();
        setMyHospitals();
    }


    public MutableLiveData<ArrayList<VisitPlanRO>> getVisitPlanHospitalList() {
        return visitPlanHospitalList;
    }

    public void setMyHospitals(){

        ArrayList<VisitPlanRO> visitPlanList = new ArrayList<>();
        IntStream.range(1, 20).forEach(t -> {
            visitPlanList.add(new VisitPlanRO(t, "11111" + String.valueOf(t), "내 병원 " + String.valueOf(t), 0));
        });

        this.visitPlanHospitalList.setValue(visitPlanList);

    }

    public void setAllHospitals(){
        ArrayList<VisitPlanRO> visitPlanList = new ArrayList<>();
        IntStream.range(1, 20).forEach(t -> {
            visitPlanList.add(new VisitPlanRO(t, "22222" + String.valueOf(t), "전체 병원 " + String.valueOf(t), 0));
        });

        this.visitPlanHospitalList.setValue(visitPlanList);
    }

    public void searchHospitals(int searchMode, int searchType, String searchKeyWord){
        ArrayList<VisitPlanRO> visitPlanList = new ArrayList<>();
        IntStream.range(1, 20).forEach(t -> {
            visitPlanList.add(new VisitPlanRO(t, "33333" + String.valueOf(t), searchKeyWord+" 병원 " + String.valueOf(t), 0));
        });

        this.visitPlanHospitalList.setValue(visitPlanList);


    }


}