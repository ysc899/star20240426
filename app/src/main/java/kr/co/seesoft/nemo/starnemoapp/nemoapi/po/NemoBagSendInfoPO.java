package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 행낭 발송 조회 PO
 */
public class NemoBagSendInfoPO {
    /** 행낭발송일자 */
    private String cpbgSendDt;

    /** 행낭발송부서코드 */
    private String cpbgSendDeptCd;

    /** 행낭차수코드(1:1차, 2:2차: 3:3차) */
    private String cpbgDegCd;

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

    @Override
    public String toString() {
        return "NemoBagSendInfoPO{" +
                "cpbgSendDt='" + cpbgSendDt + '\'' +
                ", cpbgSendDeptCd='" + cpbgSendDeptCd + '\'' +
                ", cpbgDegCd='" + cpbgDegCd + '\'' +
                '}';
    }
}
