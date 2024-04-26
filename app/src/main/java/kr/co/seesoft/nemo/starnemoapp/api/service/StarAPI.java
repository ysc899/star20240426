package kr.co.seesoft.nemo.starnemoapp.api.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.seesoft.nemo.starnemoapp.api.po.LoginPO;
import kr.co.seesoft.nemo.starnemoapp.api.po.SaveSpecimenHandoverPO;
import kr.co.seesoft.nemo.starnemoapp.api.po.SelectmClisMasterPO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.LoginRO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.SaveSpecimenHandoverRO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.SelectmClisMasterRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StarAPI {

//    private final String BASE_URL = "http://100.100.50.215:8001/";
    private final String BASE_URL = "http://219.252.39.216:8001/";

    private StarApiInterface apis;

    private Context context;

    private CustomProgressDialog progressDialog;

    public StarAPI(final Context context) {

        this.context = context;
        init();
    }

    private void init(){

        progressDialog = new CustomProgressDialog(context);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        apis = retrofit.create(StarApiInterface.class);


    }

    /**
     * @param param 로그인 정보
     * @param handler 콜백 핸들러
     */
    public void login(final LoginPO param, final Handler handler){

        progressDialog.show();

        Call<LoginRO> call = apis.login(param);

        call.enqueue(new Callback<LoginRO>() {
            @Override
            public void onResponse(Call<LoginRO> call, Response<LoginRO> response) {
                LoginRO result = response.body();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<LoginRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log("*************************************************");
                AndroidUtil.log(t.getMessage(), t);
                handler.sendEmptyMessage(0);

                progressDialog.dismiss();

            }
        });

    }
    /**
     * @param handler 콜백 핸들러
     */
    public void selectmCLisMaster(final SelectmClisMasterPO param, final Handler handler){


        param.setInstCd("01");
        param.setAcceptType("S");

        AndroidUtil.log("/////////////////////////");
        AndroidUtil.log(param.toString());

        Log.i("hopalt", param.toString());
        AndroidUtil.log("/////////////////////////");

        Call<SelectmClisMasterRO> call = apis.SP_SelectmCLisMasterRequest(param);

        call.enqueue(new Callback<SelectmClisMasterRO>() {
            @Override
            public void onResponse(Call<SelectmClisMasterRO> call, Response<SelectmClisMasterRO> response) {

                SelectmClisMasterRO result = response.body();

                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

//                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<SelectmClisMasterRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log("*************************************************");
                AndroidUtil.log(t.getMessage(), t);
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }
    /**
     * @param handler 콜백 핸들러
     */
    public void sendTakingOverInfo(final SaveSpecimenHandoverPO param, final Handler handler){


        Gson gson = new Gson();
        String p = gson.toJson(param);

        Call<SaveSpecimenHandoverRO> call = apis.SP_SaveSpecimenHandoverRequest(param);

        call.enqueue(new Callback<SaveSpecimenHandoverRO>() {
            @Override
            public void onResponse(Call<SaveSpecimenHandoverRO> call, Response<SaveSpecimenHandoverRO> response) {

                SaveSpecimenHandoverRO result = response.body();

                //실패 0
                
                //성공 리스트 갯수
//                AndroidUtil.log("----------------- 완료 -------------------");
//                AndroidUtil.log(result);
//                AndroidUtil.log("----------------- 완료 -------------------");


                Message msg = new Message();
                msg.what = response.code();
                msg.obj = result;

                handler.sendMessage(msg);

//                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<SaveSpecimenHandoverRO> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log("*************************************************");
                AndroidUtil.log(t.getMessage(), t);
                t.printStackTrace();
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }


}
