package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 일일 방문계획 조회 PO
 */
public class NemoScheduleListPO {
    /** id */
    private String userId;
    
    /** 구분 
     *  조회 일자 ( YYYYMMDD ) - 수거 예정일
     * */
    private String smplTakePlanDt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSmplTakePlanDt() {
        return smplTakePlanDt;
    }

    public void setSmplTakePlanDt(String smplTakePlanDt) {
        this.smplTakePlanDt = smplTakePlanDt;
    }

    @Override
    public String toString() {
        return "NemoScheduleListPO{" +
                "userId='" + userId + '\'' +
                ", smplTakePlanDt='" + smplTakePlanDt + '\'' +
                '}';
    }
}
