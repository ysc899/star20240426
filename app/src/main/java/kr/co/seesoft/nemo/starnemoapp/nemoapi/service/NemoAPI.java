package kr.co.seesoft.nemo.starnemoapp.nemoapi.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoRndPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoCodePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportCodePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportReceiptPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerVOCCountPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerVOCDeptListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerVOCListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoDepartmentContactListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoEmptyListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendDeletePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendUpdatePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerDetailPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerMemoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerRecpInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoGpsAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoImageInfoPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoLoginPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesBillSendPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesDepositListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesTransactionListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleOrderUpdatePO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoTermsSignImageAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoVisitAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultAppInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerInfoRndRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerSupportListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerSupportReceiptRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerVOCCountRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerVOCListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultDepartmentContactListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultBagSendListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerDetailRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerRecpInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultCustomerRecpListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultHospitalSearchListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultImageAddRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultLoginRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultSalesApprovalListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.result.NemoResultScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoAppInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRndRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportReceiptRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerVOCListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDepartmentContactListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoBagSendListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerDetailRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoImageInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoLoginRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesApprovalListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class NemoAPI {

    private final String BASE_URL =  Const.API_BASE_URL;

    private NemoApiInterface apis;

    private Context context;

    private CustomProgressDialog progressDialog;

    public NemoAPI(final Context context) {

        this.context = context;
        init();
    }

    private void init(){

        progressDialog = new CustomProgressDialog(context);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectionPool(new ConnectionPool(1, 5L, TimeUnit.MINUTES))
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        apis = retrofit.create(NemoApiInterface.class);


    }

    /**
     * @param param 로그인 정보
     * @param handler 콜백 핸들러
     */
    public void login(final NemoLoginPO param, final Handler handler){

        progressDialog.show();

        Call<NemoResultLoginRO> call = apis.login(param);

        call.enqueue(new Callback<NemoResultLoginRO>() {
            @Override
            public void onResponse(Call<NemoResultLoginRO> call, Response<NemoResultLoginRO> response) {

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoResultLoginRO result = response.body();

                NemoLoginRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<NemoResultLoginRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 방문 계획 리스트 조회 정보
     * @param handler 콜백 핸들러
     */
    public void searchScheduleList(final NemoScheduleListPO param, final Handler handler){

        Call<NemoResultScheduleListRO> call = apis.searchVisitList(param);

        call.enqueue(new Callback<NemoResultScheduleListRO>() {
            @Override
            public void onResponse(Call<NemoResultScheduleListRO> call, Response<NemoResultScheduleListRO> response) {
                NemoResultScheduleListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                List<NemoScheduleListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultScheduleListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }


    /**
     * @param param 병원 검색 정보
     * @param handler 콜백 핸들러
     */
    public void searchHospital(final NemoHospitalSearchPO param, final Handler handler){
        Call<NemoResultHospitalSearchListRO> call = apis.searchHospitals(param);

        call.enqueue(new Callback<NemoResultHospitalSearchListRO>() {
            @Override
            public void onResponse(Call<NemoResultHospitalSearchListRO> call, Response<NemoResultHospitalSearchListRO> response) {

                NemoResultHospitalSearchListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                //List<NemoHospitalSearchRO> resultObj = result.getResult().stream().filter(t -> {return StringUtils.isNotEmpty(t.getCustNm());}).collect(Collectors.toList());
                List<NemoHospitalSearchRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultHospitalSearchListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 정보
     * @param handler 콜백 핸들러
     */
    public void getCustomerInfo(final NemoCustomerInfoPO param, final Handler handler){
        Call<NemoResultCustomerInfoRO> call = apis.getCustomerInfo(param);

        call.enqueue(new Callback<NemoResultCustomerInfoRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerInfoRO> call, Response<NemoResultCustomerInfoRO> response) {

                NemoResultCustomerInfoRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoCustomerInfoRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerInfoRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param RND 고객 정보
     * @param handler 콜백 핸들러
     */
    public void getCustomerInfoRnd(final NemoCustomerInfoRndPO param, final Handler handler){
        Call<NemoResultCustomerInfoRndRO> call = apis.getCustomerInfoRnd(param);

        call.enqueue(new Callback<NemoResultCustomerInfoRndRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerInfoRndRO> call, Response<NemoResultCustomerInfoRndRO> response) {

                NemoResultCustomerInfoRndRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoCustomerInfoRndRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerInfoRndRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 상세 정보
     * @param handler 콜백 핸들러
     */
    public void getCustomerDetail(final NemoCustomerDetailPO param, final Handler handler){
        Call<NemoResultCustomerDetailRO> call = apis.getCustomerDetail(param);

        call.enqueue(new Callback<NemoResultCustomerDetailRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerDetailRO> call, Response<NemoResultCustomerDetailRO> response) {

                NemoResultCustomerDetailRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoCustomerDetailRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerDetailRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 접수 정보
     * @param handler 콜백 핸들러
     */
    public void getCustomerRecpInfo(final NemoCustomerRecpInfoPO param, final Handler handler){
        Call<NemoResultCustomerRecpInfoRO> call = apis.getCustomerRecpInfo(param);

        call.enqueue(new Callback<NemoResultCustomerRecpInfoRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerRecpInfoRO> call, Response<NemoResultCustomerRecpInfoRO> response) {

                NemoResultCustomerRecpInfoRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoCustomerRecpInfoRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerRecpInfoRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 Test 의뢰 목록 정보
     * @param handler 콜백 핸들러
     */
    public void getCustomerRecpList(final NemoCustomerRecpInfoPO param, final Handler handler){
        Call<NemoResultCustomerRecpListRO> call = apis.getCustomerRecpList(param);

        call.enqueue(new Callback<NemoResultCustomerRecpListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerRecpListRO> call, Response<NemoResultCustomerRecpListRO> response) {

                NemoResultCustomerRecpListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerRecpListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerRecpListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 월 거래 대장 목록
     * @param handler 콜백 핸들러
     */
    public void getSalesTransactionList(final NemoSalesTransactionListPO param, final Handler handler){
        Call<NemoResultSalesTransactionListRO> call = apis.getSalesTransactionList(param);

        call.enqueue(new Callback<NemoResultSalesTransactionListRO>() {
            @Override
            public void onResponse(Call<NemoResultSalesTransactionListRO> call, Response<NemoResultSalesTransactionListRO> response) {

                NemoResultSalesTransactionListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoSalesTransactionListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultSalesTransactionListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 월 수금 목록
     * @param handler 콜백 핸들러
     */
    public void getSalesDepositList(final NemoSalesDepositListPO param, final Handler handler){
        Call<NemoResultSalesDepositListRO> call = apis.getSalesDepositList(param);

        call.enqueue(new Callback<NemoResultSalesDepositListRO>() {
            @Override
            public void onResponse(Call<NemoResultSalesDepositListRO> call, Response<NemoResultSalesDepositListRO> response) {

                NemoResultSalesDepositListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoSalesDepositListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultSalesDepositListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 방문 계획 등록
     * @param handler 콜백 핸들러
     */
    public void scheduleAdd(final NemoScheduleAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.scheduleAdd(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 방문 계획 순서 변경
     * @param handler 콜백 핸들러
     */
    public void scheduleOrderUpdate(final ArrayList<NemoScheduleOrderUpdatePO> param, final Handler handler){

        Call<NemoResultRO> call = apis.scheduleOrderUpdate(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 일일 방문 계획 전체 삭제
     * @param handler 콜백 핸들러
     */
    public void scheduleAllDelete(final NemoScheduleListPO param, final Handler handler){

        Call<NemoResultRO> call = apis.scheduleAllDelete(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 방문 계획 리스트 조회 정보
     * @param handler 콜백 핸들러
     */
    public void searchVisitList(final NemoScheduleListPO param, final Handler handler){

        Call<NemoResultScheduleListRO> call = apis.searchVisitList(param);

        call.enqueue(new Callback<NemoResultScheduleListRO>() {
            @Override
            public void onResponse(Call<NemoResultScheduleListRO> call, Response<NemoResultScheduleListRO> response) {
                NemoResultScheduleListRO result = response.body();

                List<NemoScheduleListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultScheduleListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 접수 이미지 등록 (비동기식)
     * @param handler 콜백 핸들러
     */
//    public void addRegisterImage(final NemoImageAddPO param, final long seq, final boolean isLast, final Handler handler){
//
//        Call<NemoResultImageAddRO> call = apis.addRegisterImage(param);
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String result = response.body();
//
//                Message msg = new Message();
//                msg.what = response.code();
//
//                NemoImageAddRO ro = new NemoImageAddRO(StringUtils.isNotEmpty(result), seq, isLast, result);
//                msg.obj = ro;
//
////                handler.sendMessageDelayed(msg, 3000 + ((sendCount++) * 1000));
//
//                handler.sendMessage(msg);
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//                AndroidUtil.log("********************에러*****************************");
//                AndroidUtil.log(t.getMessage().toString());
//                AndroidUtil.log(t.getMessage(), t);
//                AndroidUtil.log("*************************************************");
//
//                if(!isLast) {
//                    handler.sendEmptyMessage(0);
//                }else{
//                    handler.sendEmptyMessage(2000);
//                }
//            }
//        });
//    }



    /**
     * @param param 접수 이미지 등록 (동기식)
     */
    public NemoResultImageAddRO addSyncRegisterImage(final ArrayList<NemoImageAddPO> param){

        Call<NemoResultImageAddRO> call = apis.addRegisterImage(param);

        //동기식으로 변경
        try {
            Response<NemoResultImageAddRO> response = call.execute();

            NemoResultImageAddRO result = response.body();

            return result;

        }catch(IOException ioe) {
            return null;
        }
    }

    /**
     * @param param 거래 대장 전자 서명 등록 (동기식)
     */
//    public NemoResultRO addSyncSignImage(final NemoSignImageAddPO param){
//
//        Call<NemoResultRO> call = apis.addSignImage(param);
//
//        //동기식으로 변경
//        try {
//            Response<NemoResultRO> response = call.execute();
//
//            NemoResultRO result = response.body();
//
//            return result;
//
//        }catch(IOException ioe) {
//            return null;
//        }
//    }

    public void addSignImage(final NemoSignImageAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.addSignImage(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    public void addTermsSignImage(final NemoTermsSignImageAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.addTermsSignImage(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 청구서 발송
     * @param handler 콜백 핸들러
     */
    public void sendSalesBill(final NemoSalesBillSendPO param, final Handler handler){

        Call<NemoResultRO> call = apis.sendSalesBill(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }


    /**
     * @param param 행랑발송목록
     * @param handler 콜백 핸들러
     */
    public void getBagSendList(final NemoBagSendListPO param, final Handler handler){
        Call<NemoResultBagSendListRO> call = apis.getBagSendList(param);

        call.enqueue(new Callback<NemoResultBagSendListRO>() {
            @Override
            public void onResponse(Call<NemoResultBagSendListRO> call, Response<NemoResultBagSendListRO> response) {

                NemoResultBagSendListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoBagSendListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultBagSendListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 행랑지점목록
     * @param handler 콜백 핸들러
     */
    public void getBranchList(final NemoEmptyListPO param, final Handler handler){
        Call<NemoResultDeptCdNmListRO> call = apis.getBranchList(param);

        call.enqueue(new Callback<NemoResultDeptCdNmListRO>() {
            @Override
            public void onResponse(Call<NemoResultDeptCdNmListRO> call, Response<NemoResultDeptCdNmListRO> response) {

                NemoResultDeptCdNmListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoDeptCdNmListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultDeptCdNmListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 행랑센터목록
     * @param handler 콜백 핸들러
     */
    public void getCenterList(final NemoEmptyListPO param, final Handler handler){
        Call<NemoResultDeptCdNmListRO> call = apis.getCenterList(param);

        call.enqueue(new Callback<NemoResultDeptCdNmListRO>() {
            @Override
            public void onResponse(Call<NemoResultDeptCdNmListRO> call, Response<NemoResultDeptCdNmListRO> response) {

                NemoResultDeptCdNmListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoDeptCdNmListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultDeptCdNmListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 행랑운송구분목록
     * @param handler 콜백 핸들러
     */
    public void getTransportationList(final NemoEmptyListPO param, final Handler handler){
        Call<NemoResultCdNmListRO> call = apis.getTransportationList(param);

        call.enqueue(new Callback<NemoResultCdNmListRO>() {
            @Override
            public void onResponse(Call<NemoResultCdNmListRO> call, Response<NemoResultCdNmListRO> response) {

                NemoResultCdNmListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCdNmListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCdNmListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 행랑운송회사목록
     * @param handler 콜백 핸들러
     */
    public void getTransportCompanyList(final NemoEmptyListPO param, final Handler handler){
        Call<NemoResultCdNmListRO> call = apis.getTransportCompanyList(param);

        call.enqueue(new Callback<NemoResultCdNmListRO>() {
            @Override
            public void onResponse(Call<NemoResultCdNmListRO> call, Response<NemoResultCdNmListRO> response) {

                NemoResultCdNmListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCdNmListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCdNmListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 행랑발송저장
     * @param handler 콜백 핸들러
     */
    public void addBagSend(final NemoBagSendAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.addBagSend(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 행랑발송수정
     * @param handler 콜백 핸들러
     */
    public void updateBagSend(final NemoBagSendUpdatePO param, final Handler handler){

        Call<NemoResultRO> call = apis.updateBagSend(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 행랑발송삭제
     * @param handler 콜백 핸들러
     */
    public void deleteBagSend(final NemoBagSendDeletePO param, final Handler handler){

        Call<NemoResultRO> call = apis.deleteBagSend(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }


    /**
     * @param param 고객 메모 목록
     * @param handler 콜백 핸들러
     */
    public void getMemoList(final NemoCustomerMemoPO param, final Handler handler){
        Call<NemoResultCustomerMemoListRO> call = apis.getMemoList(param);

        call.enqueue(new Callback<NemoResultCustomerMemoListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerMemoListRO> call, Response<NemoResultCustomerMemoListRO> response) {

                NemoResultCustomerMemoListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerMemoListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerMemoListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 메모 코드 목록
     * @param handler 콜백 핸들러
     */
    public void getMemoCodeList(final NemoCustomerMemoCodePO param, final Handler handler){
        Call<NemoResultCustomerMemoCodeListRO> call = apis.getMemoCodeList(param);

        call.enqueue(new Callback<NemoResultCustomerMemoCodeListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerMemoCodeListRO> call, Response<NemoResultCustomerMemoCodeListRO> response) {

                NemoResultCustomerMemoCodeListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerMemoCodeListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerMemoCodeListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객 메모저장
     * @param handler 콜백 핸들러
     */
    public void addMemo(final NemoCustomerMemoAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.addMemo(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 고객 메모수정
     * @param handler 콜백 핸들러
     */
    public void updateMemo(final NemoCustomerMemoAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.updateMemo(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 고객 메모삭제
     * @param handler 콜백 핸들러
     */
    public void deleteMemo(final NemoCustomerMemoAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.deleteMemo(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 부서 임직원 목록 조회
     * @param handler 콜백 핸들러
     */
    public void getDepartmentContactList(final NemoDepartmentContactListPO param, final Handler handler){
        Call<NemoResultDepartmentContactListRO> call = apis.getDepartmentContactList(param);

        call.enqueue(new Callback<NemoResultDepartmentContactListRO>() {
            @Override
            public void onResponse(Call<NemoResultDepartmentContactListRO> call, Response<NemoResultDepartmentContactListRO> response) {

                NemoResultDepartmentContactListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoDepartmentContactListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultDepartmentContactListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 승인결과등록
     * @param handler 콜백 핸들러
     */
    public void addApproval(@Body final NemoSalesApprovalAddPO param, final Handler handler){

        Call<NemoResultRO> call = apis.addApproval(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 승인결과목록
     * @param handler 콜백 핸들러
     */
    public void getApprovalList(final NemoSalesApprovalListPO param, final Handler handler){
        Call<NemoResultSalesApprovalListRO> call = apis.getApprovalList(param);

        call.enqueue(new Callback<NemoResultSalesApprovalListRO>() {
            @Override
            public void onResponse(Call<NemoResultSalesApprovalListRO> call, Response<NemoResultSalesApprovalListRO> response) {

                NemoResultSalesApprovalListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoSalesApprovalListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultSalesApprovalListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }


    /**
     * @param param VOC 목록 조회
     * @param handler 콜백 핸들러
     */
    public void getVOCList(final NemoCustomerVOCListPO param, final Handler handler){
        Call<NemoResultCustomerVOCListRO> call = apis.getVOCList(param);

        call.enqueue(new Callback<NemoResultCustomerVOCListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerVOCListRO> call, Response<NemoResultCustomerVOCListRO> response) {

                NemoResultCustomerVOCListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerVOCListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerVOCListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param VOC 목록 조회 - 지점별
     * @param handler 콜백 핸들러
     */
    public void getVOCDeptList(final NemoCustomerVOCDeptListPO param, final Handler handler){
        Call<NemoResultCustomerVOCListRO> call = apis.getVOCDeptList(param);

        call.enqueue(new Callback<NemoResultCustomerVOCListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerVOCListRO> call, Response<NemoResultCustomerVOCListRO> response) {

                NemoResultCustomerVOCListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerVOCListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerVOCListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param VOC 목록 조회 - Count
     * @param handler 콜백 핸들러
     */
    public void getVOCCount(final NemoCustomerVOCCountPO param, final Handler handler){
        Call<NemoResultCustomerVOCCountRO> call = apis.getVOCCount(param);

        call.enqueue(new Callback<NemoResultCustomerVOCCountRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerVOCCountRO> call, Response<NemoResultCustomerVOCCountRO> response) {

                NemoResultCustomerVOCCountRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerVOCCountRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객지원요청조회
     * @param handler 콜백 핸들러
     */
    public void getCustomerSupportList(final NemoCustomerSupportListPO param, final Handler handler){
        Call<NemoResultCustomerSupportListRO> call = apis.getCustomerSupportList(param);

        call.enqueue(new Callback<NemoResultCustomerSupportListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerSupportListRO> call, Response<NemoResultCustomerSupportListRO> response) {

                NemoResultCustomerSupportListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerSupportListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerSupportListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객지원요청 - 코드 목록 조회
     * @param handler 콜백 핸들러
     */
    public void getCustomerSupportCodeList(final NemoCustomerSupportCodePO param, final Handler handler){
        Call<NemoResultCustomerMemoCodeListRO> call = apis.getCustomerSupportCodeList(param);

        call.enqueue(new Callback<NemoResultCustomerMemoCodeListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerMemoCodeListRO> call, Response<NemoResultCustomerMemoCodeListRO> response) {

                NemoResultCustomerMemoCodeListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerMemoCodeListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerMemoCodeListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }


    /**
     * @param param 고객지원요청 - 접수정보 조회
     * @param handler 콜백 핸들러
     */
    public void getCustomerSupportReceiptInfo(final NemoCustomerSupportReceiptPO param, final Handler handler){
        Call<NemoResultCustomerSupportReceiptRO> call = apis.getCustomerSupportReceiptInfo(param);

        call.enqueue(new Callback<NemoResultCustomerSupportReceiptRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerSupportReceiptRO> call, Response<NemoResultCustomerSupportReceiptRO> response) {

                NemoResultCustomerSupportReceiptRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoCustomerSupportReceiptRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerSupportReceiptRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param 고객지원요청 - 등록 , 수정 , 삭제
     * @param handler 콜백 핸들러
     */
    public void setCustomerSupport(final NemoCustomerSupportPO param, final Handler handler){

        Call<NemoResultRO> call = apis.setCustomerSupport(param);

        call.enqueue(new Callback<NemoResultRO>() {
            @Override
            public void onResponse(Call<NemoResultRO> call, Response<NemoResultRO> response) {
                NemoResultRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoResultRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 메인 메뉴 코드 목록
     * @param handler 콜백 핸들러
     */
    public void getMenuList(final NemoCustomerMemoCodePO param, final Handler handler){
        Call<NemoResultCustomerMemoCodeListRO> call = apis.getMemoCodeList(param);

        call.enqueue(new Callback<NemoResultCustomerMemoCodeListRO>() {
            @Override
            public void onResponse(Call<NemoResultCustomerMemoCodeListRO> call, Response<NemoResultCustomerMemoCodeListRO> response) {

                NemoResultCustomerMemoCodeListRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                ArrayList<NemoCustomerMemoCodeListRO> resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultCustomerMemoCodeListRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }

    /**
     * @param param APP 버범 확인
     * @param handler 콜백 핸들러
     */
    public void getAppInfo(final NemoEmptyListPO param, final Handler handler){
        Call<NemoResultAppInfoRO> call = apis.getAppInfo(param);

        call.enqueue(new Callback<NemoResultAppInfoRO>() {
            @Override
            public void onResponse(Call<NemoResultAppInfoRO> call, Response<NemoResultAppInfoRO> response) {

                NemoResultAppInfoRO result = response.body();

                AndroidUtil.log("===> CODE : " + response.code());
                AndroidUtil.log("===> MSG : " + response.body());

                NemoAppInfoRO resultObj = result.getResult();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = resultObj;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NemoResultAppInfoRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }










//    /**
//     * @param param GPS 등록
//     */
//    public void gpsAdd(final NemoGpsAddPO param){
//
//        Call<Boolean> call = apis.addGps(param);
//
//        call.enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                boolean result = response.body();
//
//                AndroidUtil.log("gps 등록 성공 : "+ result);
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//
//                AndroidUtil.log("********************에러*****************************");
//                AndroidUtil.log(t.getMessage().toString());
//                AndroidUtil.log(t.getMessage(), t);
//                AndroidUtil.log("*************************************************");
////                handler.sendEmptyMessage(0);
//            }
//        });
//    }

}
