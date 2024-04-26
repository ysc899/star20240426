package kr.co.seesoft.nemo.starnemoapp.nemoapi.service;

import java.util.ArrayList;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoImageInfoRO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 서버 통신 인터페이스
 */
public interface NemoApiInterface {

    /** 로그인 */
    @POST("login/user-login")
    Call<NemoResultLoginRO> login(@Body final NemoLoginPO param);

    /** 일일 방문 계획 목록 조회 */
    @POST("schedule/list")
    Call<NemoResultScheduleListRO> searchVisitList(@Body final NemoScheduleListPO param);

    /** 일일 방문 계획 추가 */
    @POST("schedule/save")
    Call<NemoResultRO> scheduleAdd(@Body final NemoScheduleAddPO param);

    /** 일일 방문 계획 순서 변경 */
    @POST("schedule/update-order")
    Call<NemoResultRO> scheduleOrderUpdate(@Body final ArrayList<NemoScheduleOrderUpdatePO> param);

    /** 일일 방문 계획 전체 삭제 */
    @POST("schedule/delete-list")
    Call<NemoResultRO> scheduleAllDelete(@Body final NemoScheduleListPO param);

    /** 고객 목록 조회 */
    @POST("customer/list")
    Call<NemoResultHospitalSearchListRO> searchHospitals(@Body final NemoHospitalSearchPO param);

    /** 고객 정보 조회 */
    @POST("customer/info")
    Call<NemoResultCustomerInfoRO> getCustomerInfo(@Body final NemoCustomerInfoPO param);

    /** RND 고객 정보 조회 */
    @POST("customer/rnd-info")
    Call<NemoResultCustomerInfoRndRO> getCustomerInfoRnd(@Body final NemoCustomerInfoRndPO param);

    /** 고객 정보 상세 조회 */
    @POST("customer/detail")
    Call<NemoResultCustomerDetailRO> getCustomerDetail(@Body final NemoCustomerDetailPO param);

    /** 고객 접수 정보 조회 */
    @POST("reception/cust-recp-info")
    Call<NemoResultCustomerRecpInfoRO> getCustomerRecpInfo(@Body final NemoCustomerRecpInfoPO param);

    /** 고객 Test 의뢰 목록 조회 */
    @POST("reception/patient-list")
    Call<NemoResultCustomerRecpListRO> getCustomerRecpList(@Body final NemoCustomerRecpInfoPO param);

    /** 고객 월 거래 대장 목록 조회 */
    @POST("sales/cust-month-trade-list")
    Call<NemoResultSalesTransactionListRO> getSalesTransactionList(@Body final NemoSalesTransactionListPO param);

    /** 고객 월 수금 목록 조회 */
    @POST("sales/cust-month-deposit-list")
    Call<NemoResultSalesDepositListRO> getSalesDepositList(@Body final NemoSalesDepositListPO param);

    /** 이미지 등록 */
    @POST("reception/save-images")
    Call<NemoResultImageAddRO> addRegisterImage(@Body final ArrayList<NemoImageAddPO> param);

    /** 거래 대장 전자 서명 */
    @POST("sales/save-cust-sgnt")
    Call<NemoResultRO> addSignImage(@Body final NemoSignImageAddPO param);

    /** 약관 동의 전자 서명 */
    @POST("terms/save-trms-csnt")
    Call<NemoResultRO> addTermsSignImage(@Body final NemoTermsSignImageAddPO param);

    /** 청구서 발송 */
    @POST("sales/bill-send-request")
    Call<NemoResultRO> sendSalesBill(@Body final NemoSalesBillSendPO param);

    /** 행랑발송목록 */
    @POST("nttm-cpbg/list")
    Call<NemoResultBagSendListRO> getBagSendList(@Body final NemoBagSendListPO param);

    /** 행랑지점목록 */
    @POST("nttm-cpbg/brnc-list")
    Call<NemoResultDeptCdNmListRO> getBranchList(@Body final NemoEmptyListPO param);

    /** 행랑센터목록 */
    @POST("nttm-cpbg/lhqr-list")
    Call<NemoResultDeptCdNmListRO> getCenterList(@Body final NemoEmptyListPO param);

    /** 행랑운송구분목록 */
    @POST("nttm-cpbg/cpbg-trpt-list")
    Call<NemoResultCdNmListRO> getTransportationList(@Body final NemoEmptyListPO param);

    /** 행랑운송회사목록 */
    @POST("nttm-cpbg/cpbg-sndvhcl-list")
    Call<NemoResultCdNmListRO> getTransportCompanyList(@Body final NemoEmptyListPO param);

    /** 행랑발송저장 */
    @POST("nttm-cpbg/save")
    Call<NemoResultRO> addBagSend(@Body final NemoBagSendAddPO param);

    /** 행랑발송수정 */
    @POST("nttm-cpbg/update")
    Call<NemoResultRO> updateBagSend(@Body final NemoBagSendUpdatePO param);

    /** 행랑발송삭제 */
    @POST("nttm-cpbg/delete")
    Call<NemoResultRO> deleteBagSend(@Body final NemoBagSendDeletePO param);

    /** 고객 메모 목록 조회 */
    @POST("customer/cust-memo-list")
    Call<NemoResultCustomerMemoListRO> getMemoList(@Body final NemoCustomerMemoPO param);

    /** 고객 메모 코드 목록 조회 */
    @POST("customer/memo-code-list")
    Call<NemoResultCustomerMemoCodeListRO> getMemoCodeList(@Body final NemoCustomerMemoCodePO param);

    /** 고객 메모저장 */
    @POST("customer/insert-cust-memo")
    Call<NemoResultRO> addMemo(@Body final NemoCustomerMemoAddPO param);

    /** 고객 메모수정 */
    @POST("customer/update-cust-memo")
    Call<NemoResultRO> updateMemo(@Body final NemoCustomerMemoAddPO param);

    /** 고객 메모삭제 */
    @POST("customer/delete-cust-memo")
    Call<NemoResultRO> deleteMemo(@Body final NemoCustomerMemoAddPO param);

    /** 부서 임직원 목록 조회 */
    @POST("user/dept-user-list")
    Call<NemoResultDepartmentContactListRO> getDepartmentContactList(@Body final NemoDepartmentContactListPO param);

    /** 승인결과등록 */
    @POST("sales/save-aprv-rqst-rslt")
    Call<NemoResultRO> addApproval(@Body final NemoSalesApprovalAddPO param);

    /** 승인결과목록 */
    @POST("sales/aprv-rqst-rslt-list")
    Call<NemoResultSalesApprovalListRO> getApprovalList(@Body final NemoSalesApprovalListPO param);

    /** VOC 목록 조회 - 고객별 */
    @POST("customer/cust-cnsl-list")
    Call<NemoResultCustomerVOCListRO> getVOCList(@Body final NemoCustomerVOCListPO param);

    /** VOC 목록 조회 - 지점별 */
    @POST("customer/brnc-cust-cnsl-list")
    Call<NemoResultCustomerVOCListRO> getVOCDeptList(@Body final NemoCustomerVOCDeptListPO param);

    /** VOC 목록 Count - 미확인 */
    @POST("customer/brnc-cust-cnsl-unread-cnt")
    Call<NemoResultCustomerVOCCountRO> getVOCCount(@Body final NemoCustomerVOCCountPO param);

    /** 고객지원요청조회 */
    @POST("customer/cust-supt-list")
    Call<NemoResultCustomerSupportListRO> getCustomerSupportList(@Body final NemoCustomerSupportListPO param);

    /** 고객지원요청 - 코드 목록 조회 */
    @POST("customer/cust-supt-code-list")
    Call<NemoResultCustomerMemoCodeListRO> getCustomerSupportCodeList(@Body final NemoCustomerSupportCodePO param);

    /** 고객지원요청 - 접수정보 조회 */
    @POST("customer/recp-info")
    Call<NemoResultCustomerSupportReceiptRO> getCustomerSupportReceiptInfo(@Body final NemoCustomerSupportReceiptPO param);

    /** 고객지원요청 - 등록 , 수정 , 삭제 */
    @POST("customer/save-cust-supt")
    Call<NemoResultRO> setCustomerSupport(@Body final NemoCustomerSupportPO param);

    /** 메인 메뉴 조회 */
    @POST("login/menu-list")
    Call<NemoResultCustomerMemoCodeListRO> getMenuList(@Body final NemoCustomerMemoCodePO param);

    /** app version 확인 */
    @POST("login/app-info")
    Call<NemoResultAppInfoRO> getAppInfo(@Body final NemoEmptyListPO param);

}
