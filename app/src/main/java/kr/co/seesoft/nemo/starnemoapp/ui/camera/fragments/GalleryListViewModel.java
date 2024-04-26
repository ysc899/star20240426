package kr.co.seesoft.nemo.starnemoapp.ui.camera.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

public class GalleryListViewModel extends AndroidViewModel {

//    private MutableLiveData<ArrayList<NemoHospitalSearchRO>> visitPlanHospitalList;

    private LiveData<List<PictureVO>> hospitalYmdDatas;
    private MutableLiveData<String> hospitalCd;
    private MutableLiveData<String> today;

    private MutableLiveData<String> rootDirectory;

    private Application application;

    //db 처리
    private NemoRepository pictureRepository;

    private String userId;



    public GalleryListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        this.hospitalCd = new MutableLiveData<>();
        this.today = new MutableLiveData<>();
        this.rootDirectory = new MutableLiveData<>();


//        visitPlanHospitalList = new MediatorLiveData<>();
    }

    public String getHospitalCd() {
        return hospitalCd.getValue();
    }

    public void setHospitalCd(String hospitalCd) {
        this.hospitalCd.setValue(hospitalCd);
    }

    public String getToday() {
        return today.getValue();
    }

    public void setToday(String today) {
        this.today.setValue( today);
    }

    public String getRootDirectory() {
        return rootDirectory.getValue();
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory.setValue(rootDirectory) ;
    }

    public void initDB(){
        this.pictureRepository = new NemoRepository(application,this.getHospitalCd(), this.getToday());
        this.hospitalYmdDatas = this.pictureRepository.getHospitalYmdDatas();

        AndroidUtil.log(("this.getHospitalCd() : " + this.getHospitalCd()));
        AndroidUtil.log(("this.getRegisterDateYmd() : " + this.getToday()));
    }

    public LiveData<List<PictureVO>> getHospitalYmdDatas() {
        return hospitalYmdDatas;
    }
}