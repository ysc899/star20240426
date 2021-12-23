package kr.co.seesoft.nemo.starnemo.ui.camera.fragments;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.Const;

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
    }

    public LiveData<List<PictureVO>> getHospitalYmdDatas() {
        return hospitalYmdDatas;
    }
}