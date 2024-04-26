package kr.co.seesoft.nemo.starnemoapp.logapi.service;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * 서버 통신 인터페이스
 */
public interface LogCollectorApiInterface {

    @Multipart
    @POST("/api/v1/log/{id}")
    Call<Boolean> send(@Path("id") String userId , @Part MultipartBody.Part file);

}
