package kr.co.seesoft.nemo.starnemo.nemoapi.service;

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

import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoGpsAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageInfoPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoLoginPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoVisitAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoVisitListPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoImageAddRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoImageInfoRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoLoginRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NemoAPI {

//    private final String BASE_URL = "http://10.60.70.39:8080/";
//    private final String BASE_URL = "http://219.252.39.211:8080/";
    private final String BASE_URL = "http://star.seegenemedical.com/";

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

        Call<ArrayList<NemoLoginRO>> call = apis.login(param);

        call.enqueue(new Callback<ArrayList<NemoLoginRO>>() {
            @Override
            public void onResponse(Call<ArrayList<NemoLoginRO>> call, Response<ArrayList<NemoLoginRO>> response) {
                ArrayList<NemoLoginRO> result = response.body();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<NemoLoginRO>> call, Throwable t) {

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
     * @param param 병원 검색 정보
     * @param handler 콜백 핸들러
     */
    public void searchHospital(final NemoHospitalSearchPO param, final Handler handler){
        Call<ArrayList<NemoHospitalSearchRO>> call = apis.searchHospitals(param);

        call.enqueue(new Callback<ArrayList<NemoHospitalSearchRO>>() {
            @Override
            public void onResponse(Call<ArrayList<NemoHospitalSearchRO>> call, Response<ArrayList<NemoHospitalSearchRO>> response) {
                List<NemoHospitalSearchRO> result = response.body().stream().filter(t -> {return StringUtils.isNotEmpty(t.getHospitalCode());}).collect(Collectors.toList());

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<ArrayList<NemoHospitalSearchRO>> call, Throwable t) {

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
     * @param param 방문 계획 리스트 조회 정보
     * @param handler 콜백 핸들러
     */
    public void searchVisitList(final NemoVisitListPO param, final Handler handler){

        Call<ArrayList<NemoVisitListRO>> call = apis.searchVisitList(param);

        call.enqueue(new Callback<ArrayList<NemoVisitListRO>>() {
            @Override
            public void onResponse(Call<ArrayList<NemoVisitListRO>> call, Response<ArrayList<NemoVisitListRO>> response) {
                List<NemoVisitListRO> result = response.body();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<ArrayList<NemoVisitListRO>> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * @param param 방문 계획 등록
     * @param handler 콜백 핸들러
     */
    public void searchVisitAdd(final NemoVisitAddPO param, final Handler handler){

        Call<Boolean> call = apis.searchVisitAdd(param);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean result = response.body();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

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
    public void addRegisterImage(final NemoImageAddPO param, final long seq, final boolean isLast, final Handler handler){

        Call<String> call = apis.addRegisterImage(param);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();

                Message msg = new Message();
                msg.what = response.code();

                NemoImageAddRO ro = new NemoImageAddRO(StringUtils.isNotEmpty(result), seq, isLast, result);
                msg.obj = ro;

//                handler.sendMessageDelayed(msg, 3000 + ((sendCount++) * 1000));

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");

                if(!isLast) {
                    handler.sendEmptyMessage(0);
                }else{
                    handler.sendEmptyMessage(2000);
                }
            }
        });
    }



    /**
     * @param param 접수 이미지 등록 (동기식)
     */
    public String addSyncRegisterImage(final NemoImageAddPO param){

        Call<String> call = apis.addRegisterImage(param);

        //동기식으로 변경
        try {
            Response<String> response = call.execute();

            String result = response.body();

            return result;

        }catch(IOException ioe) {
            return "";
        }
    }

    /**
     * @param param 병원 접수된 카운트 조회
     * @param handler 콜백 핸들러
     */
    public void infoImage(final NemoImageInfoPO param, final Handler handler){

        Call<NemoImageInfoRO> call = apis.infoImage(param);

        call.enqueue(new Callback<NemoImageInfoRO>() {
            @Override
            public void onResponse(Call<NemoImageInfoRO> call, Response<NemoImageInfoRO> response) {
                NemoImageInfoRO result = response.body();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<NemoImageInfoRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
                handler.sendEmptyMessage(0);
            }
        });
    }


    /**
     * @param param GPS 등록
     */
    public void gpsAdd(final NemoGpsAddPO param){

        Call<Boolean> call = apis.addGps(param);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean result = response.body();

                AndroidUtil.log("gps 등록 성공 : "+ result);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log(t.getMessage(), t);
                AndroidUtil.log("*************************************************");
//                handler.sendEmptyMessage(0);
            }
        });
    }

}
