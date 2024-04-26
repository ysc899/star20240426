package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 행낭 발송 목록 조회 PO
 */
public class NemoBagSendListPO {
    /** id */
    private String userId;

    /**
     *  조회 일자 ( YYYYMMDD )
     * */
    private String sendDt;

    private String deptCd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSendDt() {
        return sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    @Override
    public String toString() {
        return "NemoBackSendListPO{" +
                "userId='" + userId + '\'' +
                ", sendDt='" + sendDt + '\'' +
                ", deptCd='" + deptCd + '\'' +
                '}';
    }
}
