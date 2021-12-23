package kr.co.seesoft.nemo.starnemo.api.service;

import kr.co.seesoft.nemo.starnemo.api.po.LoginPO;
import kr.co.seesoft.nemo.starnemo.api.po.SelectmClisMasterPO;
import kr.co.seesoft.nemo.starnemo.api.ro.LoginRO;
import kr.co.seesoft.nemo.starnemo.api.ro.SelectmClisMasterRO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 서버 통신 인터페이스
 */
public interface StarApiInterface {

    @POST("Star.Server.Com/Login/LoginRequest")
    Call<LoginRO> login(@Body final LoginPO param);

    
    /** 접수된 검체목록 조회 */
    @POST("Star.Server.Spc/SpecimenReception/SP_SelectmCLisMasterRequest")
    Call<SelectmClisMasterRO> SP_SelectmCLisMasterRequest(@Body final SelectmClisMasterPO param);
}
