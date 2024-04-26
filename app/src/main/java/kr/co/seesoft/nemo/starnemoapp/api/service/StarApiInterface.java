package kr.co.seesoft.nemo.starnemoapp.api.service;

import kr.co.seesoft.nemo.starnemoapp.api.po.LoginPO;
import kr.co.seesoft.nemo.starnemoapp.api.po.SaveSpecimenHandoverPO;
import kr.co.seesoft.nemo.starnemoapp.api.po.SelectmClisMasterPO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.LoginRO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.SaveSpecimenHandoverRO;
import kr.co.seesoft.nemo.starnemoapp.api.ro.SelectmClisMasterRO;
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

    /** 인수 인계 작성 저장 */
    @POST("Star.Server.Spc/SpecimenAccept/SP_SaveSpecimenHandoverRequest")
    Call<SaveSpecimenHandoverRO> SP_SaveSpecimenHandoverRequest(@Body final SaveSpecimenHandoverPO param);
//    Call<Integer> SP_SaveSpecimenHandoverRequest(@Body final String param);



}
