package kr.co.seesoft.nemo.starnemoapp.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;

public class DashboardViewModel extends AndroidViewModel {

    private String hospitalCd;
    private String today;

    private NemoRepository nemoRepository;
    private LiveData<List<PictureVO>> hospitalDatas;

    private Application application;

    private MutableLiveData<Integer> totalCount;
    private MutableLiveData<Integer> sendCount;
    private MutableLiveData<Integer> noSendCount;


    public DashboardViewModel(Application application) {
        super(application);
        this.application = application;

        this.totalCount = new MutableLiveData<>();
        this.sendCount = new MutableLiveData<>();
        this.noSendCount = new MutableLiveData<>();
        this.totalCount.setValue(0);
        this.sendCount.setValue(0);
        this.noSendCount.setValue(0);

    }

    public String getHospitalCd() {
        return hospitalCd;
    }

    public void setHospitalCd(String hospitalCd) {
        this.hospitalCd = hospitalCd;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public void setNemoRepository(){
        this.nemoRepository = new NemoRepository(application, this.hospitalCd, this.today);
        this.hospitalDatas = this.nemoRepository.getHospitalYmdDatas();
        this.setCount();
    }

    public void updateHospitalPicData(PictureVO item){
        this.nemoRepository.update(item);
    }

    public LiveData<List<PictureVO>> getHospitalDatas() {
        return hospitalDatas;
    }

    public void setCount(){
        if(this.getHospitalDatas().getValue() != null) {
            List<PictureVO> pictureVOS = this.getHospitalDatas().getValue();
            this.totalCount.setValue(pictureVOS.size());
            this.sendCount.setValue((int) pictureVOS.stream().filter(t -> {
                return t.sendFlag;
            }).count());
            this.noSendCount.setValue((int) pictureVOS.stream().filter(t -> {
                return !t.sendFlag;
            }).count());
        }
    }


    public MutableLiveData<Integer> getTotalCount() {
        return totalCount;
    }

    public MutableLiveData<Integer> getSendCount() {
        return sendCount;
    }

    public MutableLiveData<Integer> getNoSendCount() {
        return noSendCount;
    }
}