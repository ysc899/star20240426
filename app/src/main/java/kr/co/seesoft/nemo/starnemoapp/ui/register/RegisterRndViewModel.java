package kr.co.seesoft.nemo.starnemoapp.ui.register;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import kr.co.seesoft.nemo.starnemoapp.BuildConfig;
import kr.co.seesoft.nemo.starnemoapp.api.po.SelectmClisMasterPO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.HospitalRegisterRO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.SelectmClisMasterRO;
import kr.co.seesoft.nemo.starnemoapp.api.service.StarAPI;
import kr.co.seesoft.nemo.starnemoapp.db.repository.NemoRepository;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoRndPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerRecpInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoImageInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultImageAddRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRndRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoImageAddRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoImageInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.service.NemoAPI;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class RegisterRndViewModel extends AndroidViewModel {

    /** 병원 접수 정보 */
    private MutableLiveData<NemoCustomerInfoRndRO> hospitalInfo;

    private NemoScheduleListRO selHospital;

    /** 접수시 등록할 Date */
    private MutableLiveData<Date> registerDate;

    /** 시험 의뢰 목록 */
    private MutableLiveData<ArrayList<NemoCustomerRecpListRO>> receptionList;

    /** db */
    private NemoRepository nemoRepository;
    /** 병원에서 찍은 사진 촬영 리스트 */
    private LiveData<List<PictureVO>> hospitalDatas;

    private MutableLiveData<Boolean> progressFlag;

    private MutableLiveData<Boolean> sendEndFlag;

    private MutableLiveData<Boolean> sendErrorFlag;

    private MutableLiveData<Boolean> isSendProcess;

    /** context */
    private Application application;

    /** 총 접수 건수 */
    private MutableLiveData<Integer> registerCount;
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

    private String userId, upperDeptCd;
    private String deptCode;

    private NemoAPI api;
    private Handler apiHandler;
    private Handler detailApiHandler;
    private Handler recpInfoApiHandler;
    private Handler recpListApiHandler;

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

    /** 데이터 초기화용 플래그 */
    private MutableLiveData<Boolean> clearDataFlag;



    public RegisterRndViewModel(Application application) {
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
        this.receptionList = new MediatorLiveData<>();

        this.isSendProcess = new MediatorLiveData<>();

        this.registerCount = new MutableLiveData<>();
        this.totalCount = new MutableLiveData<>();
        this.sendCount = new MutableLiveData<>();
        this.noSendCount = new MutableLiveData<>();
        this.totalScanCount = new MutableLiveData<>();
        this.scanCount = new MutableLiveData<>();
        this.noScanCount = new MutableLiveData<>();
        this.registerCount.setValue(0);
        this.totalCount.setValue(0);
        this.sendCount.setValue(0);
        this.noSendCount.setValue(0);
        this.totalScanCount.setValue(0);
        this.scanCount.setValue(0);
        this.noScanCount.setValue(0);
        this.isSendProcess.setValue(false);

        this.sstCount = 0;
        this.edtaCount = 0;
        this.urineCount = 0;
        this.otherCount = 0;

        this.hospitalImageInfo = new MediatorLiveData<>();
        this.hospitalImageInfo.setValue(new NemoImageInfoRO());

        this.clearDataFlag = new MediatorLiveData<>();
        this.clearDataFlag.setValue(false);

        api = new NemoAPI(application);

        starAPI = new StarAPI(application);

        SharedPreferences sp = application.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");
        upperDeptCd = sp.getString(AndroidUtil.SP_LOGIN_UPPR_DEPT, "");

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

                            sendEmptyMessageDelayed(4000, 1000);

                            setSendEndFlag(true);

//                            if(partitionSendList.size() == 1) {
//                                sendEmptyMessageDelayed(4000, 1000);
//
//                                setSendEndFlag(true);
//
//                            }else{
//
//                                partitionSendList.remove(partitionSendList.toArray()[0]);
//
////                                partitionDataSend();
//                                partitionDataSendSync();
//                            }

                        }

                        break;
                    case 0:
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

        detailApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        NemoCustomerInfoRndRO result = (NemoCustomerInfoRndRO)msg.obj;
                        setHospitalInfo(result);

                        break;
                    default:

                        break;
                }

            }
        };

        recpInfoApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        NemoCustomerRecpInfoRO result = (NemoCustomerRecpInfoRO)msg.obj;

                        try
                        {
                            registerCount.setValue( Integer.parseInt( result.getRecpCnt() ));

//                            sendCount.setValue(Integer.parseInt( result.getImgCnt() ));
                        }
                        catch (Exception ex)
                        {
                            AndroidUtil.log(ex.getMessage());
                        }



                        break;
                    default:

                        break;
                }

            }
        };

        recpListApiHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                progressFlag.setValue(false);

                switch (msg.what){
                    case 200:
                    case 201:

                        ArrayList<NemoCustomerRecpListRO> result = (ArrayList<NemoCustomerRecpListRO>)msg.obj;

                        if(result.size() > 0) {

                            AtomicInteger editOrder = new AtomicInteger(1);

                            result.forEach(it -> {
                                it.setOrder(editOrder.getAndIncrement());
                            });
                        }

                        receptionList.setValue(result);

                        break;
                    default:

                        break;
                }

            }
        };


    }

    public MutableLiveData<NemoCustomerInfoRndRO> getHospitalInfo() {
        return hospitalInfo;
    }

    public void setHospitalInfo(NemoCustomerInfoRndRO hospitalInfo) {
        this.hospitalInfo.setValue(hospitalInfo);
//        this.setHospitalRegisterList();

//        setNemoRepository();
    }

    public void setHospital(NemoScheduleListRO hospitalInfo) {

        this.selHospital = hospitalInfo;

        // 고객 기본 정보
        NemoCustomerInfoRndPO param = new NemoCustomerInfoRndPO();

        param.setRndTakePlanUkeyid(hospitalInfo.getRndTakePlanUkeyid());

        progressFlag.setValue(true);

        api.getCustomerInfoRnd(param, detailApiHandler);


        setReceptionInfo(hospitalInfo);

    }

    public void setReceptionInfo(NemoScheduleListRO hospitalInfo)
    {
        // 고객 접수 정보
        NemoCustomerRecpInfoPO param = new NemoCustomerRecpInfoPO();

        param.setSmplTakePlanDt(this.getRegisterDateYmd());
        param.setCustCd(hospitalInfo.getCustCd());
        param.setTakchgrEmpNo(hospitalInfo.getTakchgrEmpNo());

        AndroidUtil.log("NemoCustomerRecpInfoPO : " + param);

        progressFlag.setValue(true);

        api.getCustomerRecpInfo(param,recpInfoApiHandler);

        setReceptionPatientList(hospitalInfo);
    }

    public void setReceptionPatientList(NemoScheduleListRO hospitalInfo)
    {
        // 고객 접수 정보
        NemoCustomerRecpInfoPO param2 = new NemoCustomerRecpInfoPO();

        param2.setSmplTakePlanDt(this.getRegisterDateYmd());
        param2.setCustCd(hospitalInfo.getCustCd());
        param2.setTakchgrEmpNo(hospitalInfo.getTakchgrEmpNo());

        api.getCustomerRecpList(param2,recpListApiHandler);
    }



    public MutableLiveData<Date> getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate.setValue(registerDate);
    }

    public String getHospitalCd() {
        return this.hospitalInfo.getValue().getCustCd();
    }


    public String getRegisterDateYmd(){
        return DateUtil.getFormatString(this.getRegisterDate().getValue(), "yyyyMMdd");
    }

    public void setNemoRepository(){
        this.nemoRepository = new NemoRepository(application, selHospital.getCustCd(), this.getRegisterDateYmd());
        this.hospitalDatas = this.nemoRepository.getHospitalYmdDatas();
        AndroidUtil.log(("this.getHospitalCd() : " + selHospital.getCustCd()));
        AndroidUtil.log(("this.getRegisterDateYmd() : " + this.getRegisterDateYmd()));
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

    public MutableLiveData<Integer> getRegisterCount() {
        return registerCount;
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

    public MutableLiveData<ArrayList<NemoCustomerRecpListRO>> getReceptionList() {
        return receptionList;
    }

    public MutableLiveData<ArrayList<HospitalRegisterRO>> getHospitalRegisterList() {
        return hospitalRegisterList;
    }

    public void scanHospitalRegisterBarcode(String barcode){


        if(barcode.length() != 12){
            //바코드가 12자리가 아닌경우 그냥 패스
            return;
        }

        //날짜 확인
        int dayCount = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

//        if(BuildConfig.DEBUG){
//            dayCount = 221;
//        }


        String barcodeDay = StringUtils.substring(barcode, 1, 4);

        int barcodeDayCount = Integer.valueOf(barcodeDay);

//        AndroidUtil.log("*******************************");
//        AndroidUtil.log("barcode : " + barcode);
//        AndroidUtil.log("barcodeDay : " + barcodeDay);
//        AndroidUtil.log("barcodeDayCount : " + barcodeDayCount);
//        AndroidUtil.log("dayCount : " + dayCount);
//        AndroidUtil.log("dayCount != barcodeDayCount : " + (dayCount != barcodeDayCount));
//        AndroidUtil.log("*******************************");

        if(dayCount != barcodeDayCount) {
            String barcodeNumber = StringUtils.substring(barcode, 5, 11);
            AndroidUtil.toast(getApplication(), "접수 일자 확인이 필요\n 접수번호 : "+ barcodeNumber);
            return;
        }

//        HospitalRegisterRO target = this.hospitalRegisterList.getValue().stream().filter(t -> {
//            return t.barcode.equals(barcode);
//        }).findFirst().orElse(null);
        AndroidUtil.log("확인 필요 : " + barcode);


        this.hospitalRegisterList.getValue().forEach(it ->{
            AndroidUtil.log(it.barcode);
        });


        this.hospitalRegisterList.getValue().stream().filter(t -> {
            return t.barcode.equals(barcode);
        }).forEach(it ->{
            it.scanFlag = true;
        });


        this.hospitalRegisterList.getValue().sort((o1, o2) -> Boolean.compare(o1.scanFlag, o2.scanFlag));

        calcBarcodeCount();

        /*
        if(target != null){
            
            if(!target.scanFlag) {



                String checkSum = StringUtils.substring(barcode, barcode.length() - 2, barcode.length() - 1);
                
                int checkNum = Integer.valueOf(checkSum);
                
                //1번은 의뢰지라 패스
                
                if(checkNum == 2){
                    sstCount++;
                }else if(checkNum == 3){
                    edtaCount++;
                }else if(checkNum == 4){
                    urineCount++;
                }else if(checkNum >= 5){
                    //5번 이상은 기타로 빼기로 함
                    otherCount++;
                }




            }

        }else{

            //TODO 없는 바코드에 대해서는 확인하는 API 가 필요함

            ArrayList<HospitalRegisterRO> dataList = this.getHospitalRegisterList().getValue();

            dataList.add(new HospitalRegisterRO(dataList.size() +1 , barcode));
        }
*/


        this.scanCount.setValue((int) this.hospitalRegisterList.getValue().stream().filter(t->{return t.scanFlag;}).count());
        this.noScanCount.setValue((int) this.hospitalRegisterList.getValue().stream().filter(t->{return !t.scanFlag;}).count());

    }


    /**
     * 검체 갯수 확인
     */
    private void calcBarcodeCount(){


        List<String> barcodes = this.hospitalRegisterList.getValue().stream().filter(it -> it.scanFlag).map(it -> it.barcode).distinct().collect(Collectors.toList());

        int sst = 0;
        int edta = 0;
        int urine = 0;
        int other = 0;

        for(String barcode : barcodes){
            String checkSum = StringUtils.substring(barcode, barcode.length() - 1, barcode.length());

            int checkNum = Integer.valueOf(checkSum);

            //1번은 의뢰지라 패스

            if(checkNum == 2){
                sst++;
            }else if(checkNum == 3){
                edta++;
            }else if(checkNum == 4){
                urine++;
            }else if(checkNum >= 5){
                //5번 이상은 기타로 빼기로 함
                other++;
            }
        }

        sstCount = sst;
        edtaCount = edta;
        urineCount = urine;
        otherCount = other;

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

            String hospitalCode = this.hospitalInfo.getValue().getCustCd();
            String ymd = this.getRegisterDateYmd();

            for (PictureVO t : sendList) {
                AndroidUtil.log(t.toString() + " : 시작");
                progressFlag.setValue(true);

                File f = new File(t.filePath);
                AndroidUtil.log(f.getName());

//                try {
//                    byte[] fileContent = FileUtils.readFileToByteArray(f);
//                    String imageBase64 = Base64.getEncoder().encodeToString(fileContent);
//
//
//                    NemoImageAddPO param = new NemoImageAddPO();
//                    param.setUserId(userId);
//                    param.setHospitalCode(hospitalCode);
//                    param.setDeptCode(deptCode);
//                    param.setYmd(ymd);
//                    param.setImageBase64(imageBase64 + "_hopalt");
//
//                    boolean isLast = false;
//                    if (sendList.indexOf(t) == sendList.size() - 1) {
//                        isLast = true;
//                    }
//
//                    api.addRegisterImage(param, t.seq, isLast, apiHandler);
//
//                } catch (IOException e) {
//                    AndroidUtil.log("에러 partitionDataSend() 에러 시작");
//                    AndroidUtil.log("", e);
//                    e.printStackTrace();
//                    AndroidUtil.log("에러 partitionDataSend() 에러 끝");
//                }

            }
        }
    }

    public void partitionDataSendSync(){

        ArrayList<List<PictureVO>> checkPartition = new ArrayList<>(partitionSendList);
        if(checkPartition.size() > 0) {

            progressFlag.setValue(true);

            isSendProcess.setValue(true);


            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    List<PictureVO> sendList = checkPartition.get(0);

                    String hospitalCode = hospitalInfo.getValue().getCustCd();
                    String ymd = getRegisterDateYmd();

                    for (PictureVO t : sendList) {
                        AndroidUtil.log(t.toString());

                        try {

                            File f = new File(t.filePath);
                            AndroidUtil.log(f.getName());

                            ArrayList<NemoImageAddPO> listParam = new ArrayList<>();

                            byte[] fileContent = FileUtils.readFileToByteArray(f);
                            String imageBase64 = Base64.getEncoder().encodeToString(fileContent);

                            NemoImageAddPO param = new NemoImageAddPO();
                            param.setUserId(userId);
                            param.setCustCd(hospitalCode);
                            param.setDeptCd(deptCode);
                            param.setSmplTakePlanDt(ymd);
                            param.setUpprDeptCd(upperDeptCd);
                            param.setImageBase64(imageBase64);

                            AndroidUtil.log("imageBase64 : " + imageBase64);
                            AndroidUtil.log("NemoImageAddPO : " + param);

                            boolean isLast = false;
                            if (sendList.indexOf(t) == sendList.size() - 1) {
                                isLast = true;
                            }

                            listParam.add(param);

                            NemoResultImageAddRO result = api.addSyncRegisterImage(listParam);

                            AndroidUtil.log("NemoResultImageAddRO : " + result);

                            String fName = "";

                            if( result != null )
                            {
                                String messageId = result.getMessageId();

                                if( "00020".equals(messageId) )
                                {
                                    ArrayList<String> arrFName = result.getResult();

                                    if( arrFName.size() > 0 )
                                    {
                                        fName = arrFName.get(0);
                                    }
                                }
                            }


                            if(StringUtils.isNotEmpty(fName)){
                                autoSendErrorCount = 0;
                                Message msg = new Message();
                                msg.what = 200;

                                NemoImageAddRO ro = new NemoImageAddRO(true, t.seq, isLast, fName);
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
                            AndroidUtil.log("전송 오류");
                            e.printStackTrace();
                        }

                    }

                    executorService.shutdown();
                }
            });
        }
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

    public MutableLiveData<Boolean> getClearDataFlag() {
        return clearDataFlag;
    }

    public MutableLiveData<Boolean> getIsSendProcess() {
        return isSendProcess;
    }

    public void setIsSendProcess(boolean isSendProcess){ this.isSendProcess.setValue(isSendProcess); }

}