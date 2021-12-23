package kr.co.seesoft.nemo.starnemo.ui.register;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.util.TimeUtils;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemo.BuildConfig;
import kr.co.seesoft.nemo.starnemo.api.po.SelectmClisMasterPO;
import kr.co.seesoft.nemo.starnemo.api.ro.HospitalRegisterRO;
import kr.co.seesoft.nemo.starnemo.api.ro.SelectmClisMasterRO;
import kr.co.seesoft.nemo.starnemo.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemo.api.service.StarAPI;
import kr.co.seesoft.nemo.starnemo.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageInfoPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoImageAddRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoImageInfoRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.DateUtil;

public class RegisterViewModel extends AndroidViewModel {

    /** 병원 접수 정보 */
    private MutableLiveData<NemoVisitListRO> hospitalInfo;
    /** 접수시 등록할 Date */
    private MutableLiveData<Date> registerDate;

    /** db */
    private NemoRepository nemoRepository;
    /** 병원에서 찍은 사진 촬여 ㅇ리스트 */
    private LiveData<List<PictureVO>> hospitalDatas;

    private MutableLiveData<Boolean> progressFlag;

    private MutableLiveData<Boolean> sendEndFlag;

    private MutableLiveData<Boolean> sendErrorFlag;

    /** context */
    private Application application;

    /** 총 촬영 건수 */
    private MutableLiveData<Integer> totalCount;
    /** 전송 카운트 */
    private MutableLiveData<Integer> sendCount;
    /** 미전송 카운트 */
    private MutableLiveData<Integer> noSendCount;

    /** 총 스캔 해야 할 건수 */
    private MutableLiveData<Integer> totalScanCount;
    /** 스캔 건수 */
    private MutableLiveData<Integer> scanCount;
    /** 미스캔 건수 */
    private MutableLiveData<Integer> noScanCount;


    /** 바코드 스캔 sstCount */
    private int sstCount;
    /** 바코드 스캔 edtaCount */
    private int edtaCount;
    /** 바코드 스캔 urineCount */
    private int urineCount;
    /** 바코드 스캔 otherCount */
    private int otherCount;

    private String userId;
    private String deptCode;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler infoApiHandler;

    private StarAPI starAPI;
    private Handler starApiHandler;

    /** 병원 촬영 정보 데이터 */
    private MutableLiveData<NemoImageInfoRO> hospitalImageInfo;

    /** 병원에서 접수된 환자 및 검체들 정보 */
    private MutableLiveData<ArrayList<HospitalRegisterRO>> hospitalRegisterList;

    //전송할 임시 변수
    private Collection<List<PictureVO>> partitionSendList;

    /** 에러시 자동 재전송 유무 */
    SharedPreferences sp;
    private boolean autoSendFlag;
    private int autoSendErrorCount;





    public RegisterViewModel(Application application) {
        super(application);
        this.application = application;

        progressFlag = new MediatorLiveData<>();
        progressFlag.setValue(false);
        sendEndFlag = new MediatorLiveData<>();
        sendEndFlag.setValue(false);
        sendErrorFlag = new MediatorLiveData<>();
        sendErrorFlag.setValue(false);

        sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        autoSendFlag = sp.getBoolean(AndroidUtil.SP_ERROR_AUTO_SEND, true);
        autoSendErrorCount = 0;

        this.hospitalInfo = new MutableLiveData<>();
        this.registerDate = new MutableLiveData<>();
        this.hospitalRegisterList = new MediatorLiveData<>();

        this.totalCount = new MutableLiveData<>();
        this.sendCount = new MutableLiveData<>();
        this.noSendCount = new MutableLiveData<>();
        this.totalScanCount = new MutableLiveData<>();
        this.scanCount = new MutableLiveData<>();
        this.noScanCount = new MutableLiveData<>();
        this.totalCount.setValue(0);
        this.sendCount.setValue(0);
        this.noSendCount.setValue(0);
        this.totalScanCount.setValue(0);
        this.scanCount.setValue(0);
        this.noScanCount.setValue(0);

        this.sstCount = 0;
        this.edtaCount = 0;
        this.urineCount = 0;
        this.otherCount = 0;

        this.hospitalImageInfo = new MediatorLiveData<>();
        this.hospitalImageInfo.setValue(new NemoImageInfoRO());

        api = new NemoAPI(application);

        starAPI = new StarAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");

        apiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:

                        NemoImageAddRO result = (NemoImageAddRO)msg.obj;

                        PictureVO pictureVO = getHospitalDatas().getValue().stream().filter(t -> {
                            return t.seq == result.getSeq();
                        }).findFirst().orElse(null);

                        if(pictureVO != null) {
                            pictureVO.sendFlag = true;
                            pictureVO.saveFileName = result.getServerFileName();
                            updateHospitalPicData(pictureVO);
                            
                            AndroidUtil.log(pictureVO.toString() + " 전송 완료");
                            
                        }
                        if(result.isLastFlag()) {

                            if(partitionSendList.size() == 1) {
                                sendEmptyMessageDelayed(4000, 1000);

                                setSendEndFlag(true);

                            }else{

                                partitionSendList.remove(partitionSendList.toArray()[0]);

//                                partitionDataSend();
                                partitionDataSendSync();
                            }

                        }

                        break;
                    case 2000:
                    case 4000:
                        progressFlag.setValue(false);
//                        AndroidUtil.log("여기 왔니???");
                        if(noSendCount.getValue() > 0){
                            sendData();
                        }

                        break;

                    case 9999:
                        progressFlag.setValue(false);

                        sendErrorFlag.setValue(true);

                        break;
                    default:

                        break;
                }

            }
        };

        infoApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:

                        NemoImageInfoRO result = (NemoImageInfoRO)msg.obj;
                        setHospitalImageInfo(result);

                        break;
                    default:

                        break;
                }

            }
        };


        starApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 200:

                        SelectmClisMasterRO result = (SelectmClisMasterRO)msg.obj;

//                        AndroidUtil.log("----------------------- 결과11 ------------------------");
//                        AndroidUtil.log(result);
//                        AndroidUtil.log("----------------------- 결과 ------------------------");

                        if(result.getmClisLists() != null && result.getmClisLists().size() > 0){
                            ArrayList<HospitalRegisterRO> dataList = hospitalRegisterList.getValue();

                            result.getmClisLists().forEach(System.out::println);

                            int order = dataList.size() + 1;

                            for (SelectmClisMasterRO.MClisInfo it : result.getmClisLists()) {

                                dataList.add(new HospitalRegisterRO(order++, it.getFINSSEQ() , it.getFWRKNAM(), it.getF010FKN(), it.getBarcode()));

                            }

                            hospitalRegisterList.setValue(dataList);

                            totalScanCount.setValue(dataList.size());
                            noScanCount.setValue(dataList.size());

                        }


                        //
//            datas.add(new HospitalRegisterRO(order++, "00000633" , "임병철", Arrays.asList("Plain tube"), "012999500140"));
//            datas.add(new HospitalRegisterRO(order++, "00000633" , "임병철", Arrays.asList("NaF 용기"), "012999500157"));
//            datas.add(new HospitalRegisterRO(order++, "00000633" , "임병철", Arrays.asList("SST tube"), "012999500126"));
//            datas.add(new HospitalRegisterRO(order++, "00000633" , "임병철", Arrays.asList("EDTA tube"), "012999500133"));

//                }



//                this.hospitalRegisterList.setValue(datas);

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
//        this.setHospitalRegisterList();
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

    public MutableLiveData<Integer> getTotalScanCount() {
        return totalScanCount;
    }

    public MutableLiveData<Integer> getScanCount() {
        return scanCount;
    }

    public MutableLiveData<Integer> getNoScanCount() {
        return noScanCount;
    }

    public MutableLiveData<ArrayList<HospitalRegisterRO>> getHospitalRegisterList() {
        return hospitalRegisterList;
    }

    public void scanHospitalRegisterBarcode(String barcode){

        AndroidUtil.log("barcode : " + barcode);


        HospitalRegisterRO target = this.hospitalRegisterList.getValue().stream().filter(t -> {
            return t.barcode.equals(barcode);
        }).findFirst().orElse(null);

        if(target != null){
            target.scanFlag = true;

            String checkSum = StringUtils.substring(barcode, barcode.length()-2, barcode.length()-1);

            switch (checkSum){
                case "2":
                    sstCount++;
                    break;
                case "3":
                    edtaCount++;
                    break;
                case "4":
                    urineCount++;
                    break;
                case "5":
                    otherCount++;
                    break;
                default:
                    break;
            }

        }else{
            ArrayList<HospitalRegisterRO> dataList = this.getHospitalRegisterList().getValue();

            dataList.add(new HospitalRegisterRO(dataList.size() +1 , barcode));
        }



        this.scanCount.setValue((int) this.hospitalRegisterList.getValue().stream().filter(t->{return t.scanFlag;}).count());
        this.noScanCount.setValue((int) this.hospitalRegisterList.getValue().stream().filter(t->{return !t.scanFlag;}).count());

    }

    public void setHospitalRegisterList() {

        if(this.getHospitalRegisterList().getValue() != null && this.getHospitalRegisterList().getValue().size() > 0){
            return;
        }

        ArrayList<HospitalRegisterRO> datas = new ArrayList<>();


        if(this.getHospitalCd().equals("36246") && BuildConfig.DEBUG) {


            SelectmClisMasterPO starParam = new SelectmClisMasterPO();
            starParam.setBranCd(deptCode);
            starParam.setHosClntCd(this.getHospitalCd());
            starParam.setBsDd(getRegisterDateYmd());

            AndroidUtil.log("------------------- STAR 호출 옴------------------");
            starAPI.selectmCLisMaster(starParam, starApiHandler);

        }



        this.hospitalRegisterList.setValue(datas);
        this.totalScanCount.setValue(this.hospitalRegisterList.getValue().size());
        this.noScanCount.setValue(this.hospitalRegisterList.getValue().size());
    }

    public int getSstCount() {
        return sstCount;
    }

    public int getEdtaCount() {
        return edtaCount;
    }

    public int getUrineCount() {
        return urineCount;
    }

    public int getOtherCount() {
        return otherCount;
    }

    public MutableLiveData<Boolean> getProgressFlag() {
        return progressFlag;
    }


    public void sendData(){
        List<PictureVO> list = this.getHospitalDatas().getValue();

        if (list != null) {

//            String hospitalCode = this.hospitalInfo.getValue().getHospitalCode();
//            String ymd = this.getRegisterDateYmd();

            long sendCount = list.stream().filter(t -> {
                return !t.sendFlag;
            }).count();

            if(sendCount == 0L){
                AndroidUtil.toast(getApplication(), "전송할 사진이 없습니다.");
                return;
            }


            AndroidUtil.log("총 전송할 리스트");
            list.stream().filter(t -> {
                return !t.sendFlag;
            }).collect(Collectors.toList()).forEach(it ->{
                AndroidUtil.log(it);
            });
            AndroidUtil.log("총 전송할 리스트 끝");



            AtomicInteger counter = new AtomicInteger();


//            List<PictureVO> sendList = list.stream().filter(t -> {
//                return !t.sendFlag;
//            }).collect(Collectors.toList());

            partitionSendList = list.stream().filter(t -> {
                return !t.sendFlag;
            }).sorted().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 100)).values();

//            partitionDataSend();

            partitionDataSendSync();



        }
    }


    /** 비 동기식 데이터 전송 */
    public void partitionDataSend(){

        ArrayList<List<PictureVO>> checkPartition = new ArrayList<>(partitionSendList);
        if(checkPartition.size() > 0) {

            List<PictureVO> sendList = checkPartition.get(0);

            String hospitalCode = this.hospitalInfo.getValue().getHospitalCode();
            String ymd = this.getRegisterDateYmd();

            for (PictureVO t : sendList) {
                AndroidUtil.log(t.toString() + " : 시작");
                progressFlag.setValue(true);

                File f = new File(t.filePath);
                AndroidUtil.log(f.getName());

                try {
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String imageBase64 = Base64.getEncoder().encodeToString(fileContent);


                    NemoImageAddPO param = new NemoImageAddPO();
                    param.setUserId(userId);
                    param.setHospitalCode(hospitalCode);
                    param.setDeptCode(deptCode);
                    param.setYmd(ymd);
                    param.setImageBase64(imageBase64 + "_hopalt");

                    boolean isLast = false;
                    if (sendList.indexOf(t) == sendList.size() - 1) {
                        isLast = true;
                    }

                    api.addRegisterImage(param, t.seq, isLast, apiHandler);

                } catch (IOException e) {
                    AndroidUtil.log("에러 partitionDataSend() 에러 시작");
                    AndroidUtil.log("", e);
                    e.printStackTrace();
                    AndroidUtil.log("에러 partitionDataSend() 에러 끝");
                }

            }
        }
    }

    public void partitionDataSendSync(){

        ArrayList<List<PictureVO>> checkPartition = new ArrayList<>(partitionSendList);
        if(checkPartition.size() > 0) {

            progressFlag.setValue(true);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    List<PictureVO> sendList = checkPartition.get(0);

                    String hospitalCode = hospitalInfo.getValue().getHospitalCode();
                    String ymd = getRegisterDateYmd();

                    for (PictureVO t : sendList) {
                        AndroidUtil.log(t.toString());

                        File f = new File(t.filePath);
                        AndroidUtil.log(f.getName());

                        try {
                            byte[] fileContent = FileUtils.readFileToByteArray(f);
                            String imageBase64 = Base64.getEncoder().encodeToString(fileContent);


                            NemoImageAddPO param = new NemoImageAddPO();
                            param.setUserId(userId);
                            param.setHospitalCode(hospitalCode);
                            param.setDeptCode(deptCode);
                            param.setYmd(ymd);
                            param.setImageBase64(imageBase64 + "_hopalt");

                            boolean isLast = false;
                            if (sendList.indexOf(t) == sendList.size() - 1) {
                                isLast = true;
                            }


                            String result = api.addSyncRegisterImage(param);

                            if(StringUtils.isNotEmpty(result)){
                                autoSendErrorCount = 0;
                                Message msg = new Message();
                                msg.what = 200;

                                NemoImageAddRO ro = new NemoImageAddRO(true, t.seq, isLast, result);
                                msg.obj = ro;
                                apiHandler.sendMessage(msg);
                            }else{

                                //결과가 없는 경우
                                if(autoSendFlag) {
                                    autoSendErrorCount++;

                                    if(autoSendErrorCount < 10) {
                                        if (!isLast) {
                                            apiHandler.sendEmptyMessage(0);
                                        } else {
                                            apiHandler.sendEmptyMessage(2000);
                                        }
                                    }else{
                                        apiHandler.sendEmptyMessage(9999);
                                        executorService.shutdown();
                                    }
                                }else{
                                    apiHandler.sendEmptyMessage(9999);
                                    executorService.shutdown();
                                }
                            }


                        } catch (IOException e) {
                            AndroidUtil.log("에러에러");
                            e.printStackTrace();
                        }

                    }
                    executorService.shutdown();
                }
            });
        }
    }



    public void getApiHospitalImageInfo(){


        String hospitalCode = this.hospitalInfo.getValue().getHospitalCode();
        String ymd = this.getRegisterDateYmd();

        NemoImageInfoPO param = new NemoImageInfoPO();
        param.setYmd(ymd);
        param.setHospitalCode(hospitalCode);

        api.infoImage(param, infoApiHandler);

    }


    public MutableLiveData<NemoImageInfoRO> getHospitalImageInfo() {
        return hospitalImageInfo;
    }



    public void setHospitalImageInfo(NemoImageInfoRO hospitalImageInfo) {
        this.hospitalImageInfo.setValue(hospitalImageInfo);
    }

//    public void setProgressFlag(boolean progressFlag) {
//        this.progressFlag.setValue(progressFlag);
//    }


    public MutableLiveData<Boolean> getSendEndFlag() {
        return sendEndFlag;
    }

    public void setSendEndFlag(boolean sendEndFlag) {
        this.sendEndFlag.setValue(sendEndFlag);
    }

    public MutableLiveData<Boolean> getSendErrorFlag() {
        return sendErrorFlag;
    }

    public void setSendErrorFlag(Boolean sendErrorFlag) {
        this.sendErrorFlag.setValue(sendErrorFlag);
    }

    public void setAutoSendErrorCount(int autoSendErrorCount) {
        this.autoSendErrorCount = autoSendErrorCount;
    }
}