package kr.co.seesoft.nemo.starnemo.ui.takingover;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

import kr.co.seesoft.nemo.starnemo.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemo.db.vo.TakingOverVO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.util.DateUtil;

public class TakingOverViewModel extends AndroidViewModel {

    /** 병원 접수 정보 */
    private MutableLiveData<NemoVisitListRO> hospitalInfo;
    /** 접수시 등록할 Date */
    private MutableLiveData<Date> registerDate;

    /** 인수 인계 리스트 데이터 */
    private LiveData<List<TakingOverVO>> takingOverList;
    /** 인수 인계용 데이터 */
    private MutableLiveData<TakingOverVO> takingOverInfo;


    /** db */
    private NemoRepository nemoRepository;

    /** context */
    private Application application;



    public TakingOverViewModel(Application application) {
        super(application);
        this.application = application;

        this.hospitalInfo = new MutableLiveData<>();
        this.registerDate = new MutableLiveData<>();
        this.takingOverInfo = new MutableLiveData<>();
    }

    public MutableLiveData<NemoVisitListRO> getHospitalInfo() {
        return hospitalInfo;
    }

    public void setHospitalInfo(NemoVisitListRO hospitalInfo) {
        this.hospitalInfo.setValue(hospitalInfo);
    }

    public MutableLiveData<Date> getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate.setValue(registerDate);
    }

    public String getHospitalCd() {
        return this.hospitalInfo.getValue().getHospitalCode();
    }


    public String getRegisterDateYmd(){
        return DateUtil.getFormatString(this.getRegisterDate().getValue(), "yyyyMMdd");
    }

    public void setNemoRepository(){
        this.nemoRepository = new NemoRepository(application, this.getHospitalCd(), this.getRegisterDateYmd());
        this.takingOverList = this.nemoRepository.getHospitalYmdTakingOver();
    }

    public LiveData<List<TakingOverVO>> getTakingOverList() {
        return takingOverList;
    }

    public MutableLiveData<TakingOverVO> getTakingOverInfo() {
        return takingOverInfo;
    }


    public void setTakingOverInfo(TakingOverVO takingOverInfo) {
        this.takingOverInfo.setValue(takingOverInfo);
    }

    public TakingOverVO getTakingOverVO(){
        return this.takingOverInfo.getValue();
    }

    public void setTakingSignFile(String filePath){
        this.takingOverInfo.getValue().takingOverPath = filePath;
    }

    public void setTakeSignFile(String filePath){
        this.takingOverInfo.getValue().takeOverPath = filePath;
    }

    public void saveTakingOverInfo(TakingOverVO item){
        item.date = new Date();
        if(item.seq == 0){
            item.ymd = this.getRegisterDateYmd();
            item.hospitalKey = this.getHospitalCd();
            this.nemoRepository.insert(item);
        }else{
            this.nemoRepository.update(item);
        }
    }
}