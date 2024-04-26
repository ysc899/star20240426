package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;

/**
 * 일일 방문계획 추가 PO
 */
public class NemoScheduleAddPO {
    /** id */
    private String userId;

    /** 부서코드 */
    private String deptCd;

    /** 상위부서코드 */
    private String upprDeptCd;
    
    /** 구분 
     *  일자 ( YYYYMMDD ) - 수거 예정일
     * */
    private String smplTakePlanDt;

    /** 추가 목록 */
    private ArrayList<NemoScheduleAddListPO> customerRequestDTOList;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    public String getUpprDeptCd() {
        return upprDeptCd;
    }

    public void setUpprDeptCd(String upprDeptCd) {
        this.upprDeptCd = upprDeptCd;
    }

    public String getSmplTakePlanDt() {
        return smplTakePlanDt;
    }

    public void setSmplTakePlanDt(String smplTakePlanDt) {
        this.smplTakePlanDt = smplTakePlanDt;
    }

    public ArrayList<NemoScheduleAddListPO> getCustomerRequestDTOList()  {
        return customerRequestDTOList;
    }

    public void setCustomerRequestDTOList(ArrayList<NemoScheduleAddListPO> customerRequestDTOList){ this.customerRequestDTOList = customerRequestDTOList; }

    @Override
    public String toString() {
        return "NemoScheduleAddPO{" +
                "userId='" + userId + '\'' +
                ", deptCd='" + deptCd + '\'' +
                ", upprDeptCd='" + upprDeptCd + '\'' +
                ", smplTakePlanDt='" + smplTakePlanDt + '\'' +
                ", customerRequestDTOList='" + customerRequestDTOList + '\'' +
                '}';
    }
}
