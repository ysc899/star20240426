package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 행낭 발송 삭제 PO
 */
public class NemoBagSendDeletePO {
    /** 행낭발송일자 */
    private String cpbgSendDt;

    /** 행낭발송부서코드 */
    private String cpbgSendDeptCd;

    /** 행낭차수코드(1:1차, 2:2차: 3:3차)  */
    public String cpbgDegCd;

    /** 발송자 */
    private String userId;

    /**  */
    public String sno;

    public String getCpbgSendDt() {
        return cpbgSendDt;
    }

    public void setCpbgSendDt(String cpbgSendDt) {
        this.cpbgSendDt = cpbgSendDt;
    }

    public String getCpbgSendDeptCd() {
        return cpbgSendDeptCd;
    }

    public void setCpbgSendDeptCd(String cpbgSendDeptCd) {
        this.cpbgSendDeptCd = cpbgSendDeptCd;
    }

    public String getCpbgDegCd() {
        return cpbgDegCd;
    }

    public void setCpbgDegCd(String cpbgDegCd) {
        this.cpbgDegCd = cpbgDegCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    @Override
    public String toString() {
        return "NemoBagSendDeletePO{" +
                "cpbgSendDt='" + cpbgSendDt + '\'' +
                ", cpbgSendDeptCd='" + cpbgSendDeptCd + '\'' +
                ", cpbgDegCd='" + cpbgDegCd + '\'' +
                ", userId='" + userId + '\'' +
                ", sno='" + sno + '\'' +
                '}';
    }
}
