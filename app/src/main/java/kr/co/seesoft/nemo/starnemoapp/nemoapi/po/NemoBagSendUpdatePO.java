package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 행낭 발송 수정 PO
 */
public class NemoBagSendUpdatePO {
    /** 행낭발송일자 */
    private String cpbgSendDt;

    /** 행낭발송부서코드 */
    private String cpbgSendDeptCd;

    /** 행낭도착 센터코드 */
    private String cpbgArvlLocCntrCd;

    /** 행낭수량 */
    private String cpbgQty;

    /** 행낭발송특이사항 */
    private String cpbgSendCont;

    /** 행낭운송구분코드(고속버스:H ,신일통상:S) */
    private String cpbgTrptDivCd;

    /** 행낭출발일자 */
    private String cpbgDprtDt;

    /** 행낭발송시간 */
    private String cpbgSendTm;

    /** 행낭도착예정일자 */
    private String cpbgArvlPragDt;

    /** 행낭도착예정시간 */
    private String cpbgArvlPragTm;

    /** 행낭송장번호 */
    private String cpbgInvcNo;

    /** 행낭발송차량번호 */
    private String cpbgSndvhclNo;

    /** 행낭발송차량회사명(001:동양, 002:경기, 003:중앙, 004:금호, 999:기타) */
    private String cpbgSndvhclCmpNm;

    /** 행낭이동구분코드(A: 도착, N: 미발송, S: 발송) */
    private String cpbgMvntDivCd;

    /** 비고 */
    private String rm;

    /** 발송자 */
    private String userId;

    /**  */
    public String sno;

    /**  */
    public String cpbgDegCd;


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

    public String getCpbgArvlLocCntrCd() {
        return cpbgArvlLocCntrCd;
    }

    public void setCpbgArvlLocCntrCd(String cpbgArvlLocCntrCd) {
        this.cpbgArvlLocCntrCd = cpbgArvlLocCntrCd;
    }

    public String getCpbgQty() {
        return cpbgQty;
    }

    public void setCpbgQty(String cpbgQty) {
        this.cpbgQty = cpbgQty;
    }

    public String getCpbgSendCont() {
        return cpbgSendCont;
    }

    public void setCpbgSendCont(String cpbgSendCont) {
        this.cpbgSendCont = cpbgSendCont;
    }

    public String getCpbgTrptDivCd() {
        return cpbgTrptDivCd;
    }

    public void setCpbgTrptDivCd(String cpbgTrptDivCd) {
        this.cpbgTrptDivCd = cpbgTrptDivCd;
    }

    public String getCpbgDprtDt() {
        return cpbgDprtDt;
    }

    public void setCpbgDprtDt(String cpbgDprtDt) {
        this.cpbgDprtDt = cpbgDprtDt;
    }

    public String getCpbgSendTm() {
        return cpbgSendTm;
    }

    public void setCpbgSendTm(String cpbgSendTm) {
        this.cpbgSendTm = cpbgSendTm;
    }

    public String getCpbgArvlPragDt() {
        return cpbgArvlPragDt;
    }

    public void setCpbgArvlPragDt(String cpbgArvlPragDt) {
        this.cpbgArvlPragDt = cpbgArvlPragDt;
    }

    public String getCpbgArvlPragTm() {
        return cpbgArvlPragTm;
    }

    public void setCpbgArvlPragTm(String cpbgArvlPragTm) {
        this.cpbgArvlPragTm = cpbgArvlPragTm;
    }

    public String getCpbgInvcNo() {
        return cpbgInvcNo;
    }

    public void setCpbgInvcNo(String cpbgInvcNo) {
        this.cpbgInvcNo = cpbgInvcNo;
    }

    public String getCpbgSndvhclNo() {
        return cpbgSndvhclNo;
    }

    public void setCpbgSndvhclNo(String cpbgSndvhclNo) {
        this.cpbgSndvhclNo = cpbgSndvhclNo;
    }

    public String getCpbgSndvhclCmpNm() {
        return cpbgSndvhclCmpNm;
    }

    public void setCpbgSndvhclCmpNm(String cpbgSndvhclCmpNm) {
        this.cpbgSndvhclCmpNm = cpbgSndvhclCmpNm;
    }

    public String getCpbgMvntDivCd() {
        return cpbgMvntDivCd;
    }

    public void setCpbgMvntDivCd(String cpbgMvntDivCd) {
        this.cpbgMvntDivCd = cpbgMvntDivCd;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
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

    public String getCpbgDegCd() {
        return cpbgDegCd;
    }

    public void setCpbgDegCd(String cpbgDegCd) {
        this.cpbgDegCd = cpbgDegCd;
    }

    @Override
    public String toString() {
        return "NemoBagSendUpdatePO{" +
                "cpbgSendDt='" + cpbgSendDt + '\'' +
                ", cpbgSendDeptCd='" + cpbgSendDeptCd + '\'' +
                ", cpbgArvlLocCntrCd='" + cpbgArvlLocCntrCd + '\'' +
                ", cpbgQty='" + cpbgQty + '\'' +
                ", cpbgSendCont='" + cpbgSendCont + '\'' +
                ", cpbgTrptDivCd='" + cpbgTrptDivCd + '\'' +
                ", cpbgDprtDt='" + cpbgDprtDt + '\'' +
                ", cpbgSendTm='" + cpbgSendTm + '\'' +
                ", cpbgArvlPragDt='" + cpbgArvlPragDt + '\'' +
                ", cpbgArvlPragTm='" + cpbgArvlPragTm + '\'' +
                ", cpbgInvcNo='" + cpbgInvcNo + '\'' +
                ", cpbgSndvhclNo='" + cpbgSndvhclNo + '\'' +
                ", cpbgSndvhclCmpNm='" + cpbgSndvhclCmpNm + '\'' +
                ", cpbgMvntDivCd='" + cpbgMvntDivCd + '\'' +
                ", rm='" + rm + '\'' +
                ", userId='" + userId + '\'' +
                ", sno='" + sno + '\'' +
                ", cpbgDegCd='" + cpbgDegCd + '\'' +
                '}';
    }
}
