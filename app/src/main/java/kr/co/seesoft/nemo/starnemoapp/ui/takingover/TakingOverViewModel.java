package kr.co.seesoft.nemo.starnemoapp.ui.takingover;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.api.po.SaveSpecimenHandoverPO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.HospitalRegisterRO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.SaveSpecimenHandoverRO;
import kr.co.seesoft.nemo.starnemoapp.api.service.StarAPI;
import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.TakingOverVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class TakingOverViewModel extends AndroidViewModel {

    /** 병원 접수 정보 */
    private MutableLiveData<NemoVisitListRO> hospitalInfo;
    /** 접수시 등록할 Date */
    private MutableLiveData<Date> registerDate;

    /** 인수 인계 리스트 데이터 */
//    private LiveData<List<TakingOverVO>> takingOverList;
    /** 인수 인계용 데이터 */
    private MutableLiveData<TakingOverVO> takingOverInfo;

    /** 스캔 데이터 리스트 */
    private List<HospitalRegisterRO> scanList;
    
    /** db */
    private NemoRepository nemoRepository;

    /** context */
    private Application application;

    private StarAPI starAPI;
    private Handler starApiHandler;

    /** 전송 완료 후 화면 종료 용 */
    private MutableLiveData<Boolean> successFinsishFlag;

    
    



    public TakingOverViewModel(Application application) {
        super(application);
        this.application = application;

        this.hospitalInfo = new MutableLiveData<>();
        this.registerDate = new MutableLiveData<>();
        this.takingOverInfo = new MutableLiveData<>();

        this.successFinsishFlag = new MutableLiveData<>();
        this.successFinsishFlag.setValue(false);

        starAPI = new StarAPI(application);

        starApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:

                        SaveSpecimenHandoverRO result = (SaveSpecimenHandoverRO)msg.obj;
                        if(result.getReturnValue() > 0){
                            AndroidUtil.toast(application, "등록 되었습니다.");
                            successFinsishFlag.setValue(true);

                        }else{
                            AndroidUtil.toast(application, "잠시 후 다시 시도해 주세요.");
                        }



                        break;
                    default:

                        break;
                }

            }
        };
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

//    public void setNemoRepository(){
//        this.nemoRepository = new NemoRepository(application, this.getHospitalCd(), this.getRegisterDateYmd());
//        this.takingOverList = this.nemoRepository.getHospitalYmdTakingOver();
//    }

//    public LiveData<List<TakingOverVO>> getTakingOverList() {
//        return takingOverList;
//    }

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

    public void sendTakingOverInfo(TakingOverVO item){

        SaveSpecimenHandoverPO sendParam = new SaveSpecimenHandoverPO();

//        AndroidUtil.log("*************************************");

        sendParam.setTemperature(String.valueOf(item.temperature));
        sendParam.setSST(String.valueOf(item.sst));
        sendParam.setEDTA(String.valueOf(item.edta));
        sendParam.setUrine(String.valueOf(item.urine));
        sendParam.setTissue(String.valueOf(item.bio));
        sendParam.setOther(String.valueOf(item.other));
        sendParam.setHoComment(item.issue);
        sendParam.setSaveSpecimenHandovers(getScanList());

        byte[] fileContent = new byte[0];
        String imageBase64 = "";
        try {

            fileContent = FileUtils.readFileToByteArray(new File(item.takingOverPath));
            imageBase64 = Base64.getEncoder().encodeToString(fileContent);

            sendParam.setHosSignImage(imageBase64);

            fileContent = FileUtils.readFileToByteArray(new File(item.takeOverPath));
            imageBase64 = Base64.getEncoder().encodeToString(fileContent);
            sendParam.setUserSignImage(imageBase64);

            starAPI.sendTakingOverInfo(sendParam, starApiHandler);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<HospitalRegisterRO> getScanList() {

        this.scanList.forEach(it ->{

            it.setSeq(getHospitalCd()+"_"+String.valueOf(System.nanoTime()));
        });

        return scanList;
    }

    public void setScanList(List<HospitalRegisterRO> scanList) {
        this.scanList = scanList;
    }

    public MutableLiveData<Boolean> getSuccessFinsishFlag() {
        return successFinsishFlag;
    }
}