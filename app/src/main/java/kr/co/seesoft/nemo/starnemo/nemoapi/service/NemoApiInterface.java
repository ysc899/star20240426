package kr.co.seesoft.nemo.starnemo.nemoapi.service;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoGpsAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoHospitalSearchPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoImageInfoPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoLoginPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoVisitAddPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.po.NemoVisitListPO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoImageInfoRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoLoginRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 서버 통신 인터페이스
 */
public interface NemoApiInterface {

    @POST("login/")
    Call<ArrayList<NemoLoginRO>> login(@Body final NemoLoginPO param);

    @POST("hosptial/list")
    Call<ArrayList<NemoHospitalSearchRO>> searchHospitals(@Body final NemoHospitalSearchPO param);

    @POST("visit/list")
    Call<ArrayList<NemoVisitListRO>> searchVisitList(@Body final NemoVisitListPO param);

    @POST("visit/add")
    Call<Boolean> searchVisitAdd(@Body final NemoVisitAddPO param);

    @POST("image/addImage2Name")
    Call<String> addRegisterImage(@Body final NemoImageAddPO param);

    @POST("image/info")
    Call<NemoImageInfoRO> infoImage(@Body final NemoImageInfoPO param);


    @POST("gps/add")
    Call<Boolean> addGps(@Body final NemoGpsAddPO param);
}
