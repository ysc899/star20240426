package kr.co.seesoft.nemo.starnemoapp.logapi.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;

import kr.co.seesoft.nemo.starnemoapp.logapi.po.SendLogoPO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogCollectorAPI {

    //여기좀 참고
    //https://velog.io/@dev_thk28/Android-Retrofit2-Multipart%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-Java
    
    
//    private final String BASE_URL = "http://192.168.20.2:8080/";
    private final String BASE_URL = "https://starinfo.seegenemedical.com/";
//    private final String BASE_URL = "http://smf-star.seesoft.co.kr/";

    private LogCollectorApiInterface apis;

    private Context context;

    private CustomProgressDialog progressDialog;

    public LogCollectorAPI(final Context context) {

        this.context = context;
        init();
    }

    private void init(){

        progressDialog = new CustomProgressDialog(context);

//        OkHttpClient client = new OkHttpClient.Builder().build();

        OkHttpClient client = TrustOkHttpClientUtil.getUnsafeOkHttpClient().build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        apis = retrofit.create(LogCollectorApiInterface.class);


    }

    /**
     * @param param 로그인 정보
     * @param handler 콜백 핸들러
     */
    public void sendLog(final SendLogoPO param, final Handler handler) throws IOException {

//        progressDialog.show();

        RequestBody fileBody = RequestBody.create(MediaType.parse(Files.probeContentType(param.getLogFile().toPath())), param.getLogFile());

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", param.getLogFile().getName(), fileBody);

        Call<Boolean> call = apis.send(param.getUserId(), filePart);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                LoginRO result = response.body();


                Message msg = new Message();
                msg.what = response.code();
//                msg.obj = result;

                handler.sendMessage(msg);

//                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                AndroidUtil.log("********************에러*****************************");
                AndroidUtil.log(t.getMessage().toString());
                AndroidUtil.log("*************************************************");
                AndroidUtil.log(t.getMessage(), t);
                handler.sendEmptyMessage(0);

//                progressDialog.dismiss();

            }
        });

    }


}
