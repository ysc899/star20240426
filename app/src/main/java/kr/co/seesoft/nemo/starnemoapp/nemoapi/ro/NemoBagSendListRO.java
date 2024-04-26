package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoBagSendListRO implements Serializable {

    /**  */
    public String regDtm;
    /**  */
    public String rgurId;
    /**  */
    public String updtDtm;
    /**  */
    public String upurId;
    /**  */
    public String sno;
    /** 행낭발송일자 */
    public String cpbgSendDt;
    /** 행낭발송부서코드 */
    public String cpbgSendDeptCd;
    /** 행낭차수코드(1:1차, 2:2차: 3:3차)  */
    public String cpbgDegCd;
    /** 행낭발송지역센터코드 */
    public String cpbgSendLocCntrCd;
    /** 행낭도착지역센터코드 */
    public String cpbgArvlLocCntrCd;
    /** 행낭도착지역센터명 */
    public String cpbgArvlLocCntrNm;
    /** 행낭발송확인자아이디 */
    public String cpbgSendCnurId;
    /** 행낭발송확인자명 */
    public String senderNm;
    /** 행낭운송구분코드(고속버스:H ,신일통상:S) */
    public String cpbgTrptDivCd;
    /**  */
    public String cpbgTrptDivNm;
    /** 행낭수량 */
    public String cpbgQty;
    /** 행낭발송특이사항 */
    public String cpbgSendCont;
    /** 행낭출발일자 */
    public String cpbgDprtDt;
    /** 행낭발송시간 */
    public String cpbgSendTm;
    /** 행낭도착예정일자 */
    public String cpbgArvlPragDt;
    /** 행낭도착예정시간 */
    public String cpbgArvlPragTm;
    /** 행낭발송차량회사명(001:동양, 002:경기, 003:중앙, 004:금호, 999:기타) */
    public String cpbgSndvhclCmpNm;
    /** 행낭발송차량번호 */
    public String cpbgSndvhclNo;
    /** 행낭송장번호 */
    public String cpbgInvcNo;
    /** 행낭도착확인부서코드 */
    public String cpbgArvlCnfmDeptCd;
    /** 행낭도착확인자아이디 */
    public String cpbgArvlCnurId;
    /** 행낭도착확인자명 */
    public String rcverNm;
    /** 행낭도착일자 */
    public String cpbgArvlDt;
    /** 행낭도착시간 */
    public String cpbgArvlTm;
    /** 행낭도착온도 */
    public String cpbgArvlTprt;
    /** 행낭이동구분코드(A: 도착, N: 미발송, S: 발송) */
    public String cpbgMvntDivCd;
    /**  */
    public String cpbgMvntDivNm;
    /** 비고 */
    public String rm;
    /**  */
    public String majrTrstInstYn;
    /**  */
    public String majrAllowYn;
    /**  */
    public String deptCd;


    public String getRegDtm() {
        return regDtm;
    }

    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }

    public String getRgurId() {
        return rgurId;
    }

    public void setRgurId(String rgurId) {
        this.rgurId = rgurId;
    }

    public String getUpdtDtm() {
        return updtDtm;
    }

    public void setUpdtDtm(String updtDtm) {
        this.updtDtm = updtDtm;
    }

    public String getUpurId() {
        return upurId;
    }

    public void setUpurId(String upurId) {
        this.upurId = upurId;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

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

    public String getCpbgSendLocCntrCd() {
        return cpbgSendLocCntrCd;
    }

    public void setCpbgSendLocCntrCd(String cpbgSendLocCntrCd) { this.cpbgSendLocCntrCd = cpbgSendLocCntrCd; }

    public String getCpbgArvlLocCntrCd() {
        return cpbgArvlLocCntrCd;
    }

    public void setCpbgArvlLocCntrCd(String cpbgArvlLocCntrCd) { this.cpbgArvlLocCntrCd = cpbgArvlLocCntrCd; }

    public String getCpbgArvlLocCntrNm() {
        return cpbgArvlLocCntrNm;
    }

    public void setCpbgArvlLocCntrNm(String cpbgArvlLocCntrNm) { this.cpbgArvlLocCntrNm = cpbgArvlLocCntrNm; }

    public String getCpbgSendCnurId() {
        return cpbgSendCnurId;
    }

    public void setCpbgSendCnurId(String cpbgSendCnurId) { this.cpbgSendCnurId = cpbgSendCnurId; }

    public String getSenderNm() {
        return senderNm;
    }

    public void setSenderNm(String senderNm) { this.senderNm = senderNm; }

    public String getCpbgTrptDivCd() {
        return cpbgTrptDivCd;
    }

    public void setCpbgTrptDivCd(String cpbgTrptDivCd) { this.cpbgTrptDivCd = cpbgTrptDivCd; }

    public String getCpbgTrptDivNm() {
        return cpbgTrptDivNm;
    }

    public void setCpbgTrptDivNm(String cpbgTrptDivNm) { this.cpbgTrptDivNm = cpbgTrptDivNm; }

    public String getCpbgQty() {
        return cpbgQty;
    }

    public void setCpbgQty(String cpbgQty) { this.cpbgQty = cpbgQty; }

    public String getCpbgSendCont() {
        return cpbgSendCont;
    }

    public void setCpbgSendCont(String cpbgSendCont) { this.cpbgSendCont = cpbgSendCont; }

    public String getCpbgDprtDt() {
        return cpbgDprtDt;
    }

    public void setCpbgDprtDt(String cpbgDprtDt) { this.cpbgDprtDt = cpbgDprtDt; }

    public String getCpbgSendTm() {
        return cpbgSendTm;
    }

    public void setCpbgSendTm(String cpbgSendTm) { this.cpbgSendTm = cpbgSendTm; }

    public String getCpbgArvlPragDt() {
        return cpbgArvlPragDt;
    }

    public void setCpbgArvlPragDt(String cpbgArvlPragDt) { this.cpbgArvlPragDt = cpbgArvlPragDt; }

    public String getCpbgArvlPragTm() {
        return cpbgArvlPragTm;
    }

    public void setCpbgArvlPragTm(String cpbgArvlPragTm) { this.cpbgArvlPragTm = cpbgArvlPragTm; }

    public String getCpbgSndvhclCmpNm() {
        return cpbgSndvhclCmpNm;
    }

    public void setCpbgSndvhclCmpNm(String cpbgSndvhclCmpNm) { this.cpbgSndvhclCmpNm = cpbgSndvhclCmpNm; }

    public String getCpbgSndvhclNo() {
        return cpbgSndvhclNo;
    }

    public void setCpbgSndvhclNo(String cpbgSndvhclNo) { this.cpbgSndvhclNo = cpbgSndvhclNo; }

    public String getCpbgInvcNo() {
        return cpbgInvcNo;
    }

    public void setCpbgInvcNo(String cpbgInvcNo) { this.cpbgInvcNo = cpbgInvcNo; }

    public String getCpbgArvlCnfmDeptCd() {
        return cpbgArvlCnfmDeptCd;
    }

    public void setCpbgArvlCnfmDeptCd(String cpbgArvlCnfmDeptCd) { this.cpbgArvlCnfmDeptCd = cpbgArvlCnfmDeptCd; }

    public String getCpbgArvlCnurId() {
        return cpbgArvlCnurId;
    }

    public void setCpbgArvlCnurId(String cpbgArvlCnurId) { this.cpbgArvlCnurId = cpbgArvlCnurId; }

    public String getRcverNm() {
        return rcverNm;
    }

    public void setRcverNm(String rcverNm) { this.rcverNm = rcverNm; }

    public String getCpbgArvlDt() {
        return cpbgArvlDt;
    }

    public void setCpbgArvlDt(String cpbgArvlDt) { this.cpbgArvlDt = cpbgArvlDt; }

    public String getCpbgArvlTm() {
        return cpbgArvlTm;
    }

    public void setCpbgArvlTm(String cpbgArvlTm) { this.cpbgArvlTm = cpbgArvlTm; }

    public String getCpbgArvlTprt() {
        return cpbgArvlTprt;
    }

    public void setCpbgArvlTprt(String cpbgArvlTprt) { this.cpbgArvlTprt = cpbgArvlTprt; }

    public String getCpbgMvntDivCd() {
        return cpbgMvntDivCd;
    }

    public void setCpbgMvntDivCd(String cpbgMvntDivCd) { this.cpbgMvntDivCd = cpbgMvntDivCd; }

    public String getCpbgMvntDivNm() {
        return cpbgMvntDivNm;
    }

    public void setCpbgMvntDivNm(String cpbgMvntDivNm) { this.cpbgMvntDivNm = cpbgMvntDivNm; }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) { this.rm = rm; }

    public String getMajrTrstInstYn() {
        return majrTrstInstYn;
    }

    public void setMajrTrstInstYn(String majrTrstInstYn) { this.majrTrstInstYn = majrTrstInstYn; }

    public String getMajrAllowYn() {
        return majrAllowYn;
    }

    public void setMajrAllowYn(String majrAllowYn) { this.majrAllowYn = majrAllowYn; }

    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) { this.deptCd = deptCd; }




    @Override
    public String toString() {
        return "NemoBackSendListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", sno='" + sno + '\'' +
                ", cpbgSendDt='" + cpbgSendDt + '\'' +
                ", cpbgSendDeptCd='" + cpbgSendDeptCd + '\'' +
                ", cpbgDegCd='" + cpbgDegCd + '\'' +
                ", cpbgSendLocCntrCd='" + cpbgSendLocCntrCd + '\'' +
                ", cpbgArvlLocCntrCd='" + cpbgArvlLocCntrCd + '\'' +
                ", cpbgArvlLocCntrNm='" + cpbgArvlLocCntrNm + '\'' +
                ", cpbgSendCnurId='" + cpbgSendCnurId + '\'' +
                ", senderNm='" + senderNm + '\'' +
                ", cpbgTrptDivCd='" + cpbgTrptDivCd + '\'' +
                ", cpbgTrptDivNm='" + cpbgTrptDivNm + '\'' +
                ", cpbgQty='" + cpbgQty + '\'' +
                ", cpbgSendCont='" + cpbgSendCont + '\'' +
                ", cpbgDprtDt='" + cpbgDprtDt + '\'' +
                ", cpbgSendTm='" + cpbgSendTm + '\'' +
                ", cpbgArvlPragDt='" + cpbgArvlPragDt + '\'' +
                ", cpbgArvlPragTm='" + cpbgArvlPragTm + '\'' +
                ", cpbgSndvhclCmpNm='" + cpbgSndvhclCmpNm + '\'' +
                ", cpbgSndvhclNo='" + cpbgSndvhclNo + '\'' +
                ", cpbgInvcNo='" + cpbgInvcNo + '\'' +
                ", cpbgArvlCnfmDeptCd='" + cpbgArvlCnfmDeptCd + '\'' +
                ", cpbgArvlCnurId='" + cpbgArvlCnurId + '\'' +
                ", rcverNm='" + rcverNm + '\'' +
                ", cpbgArvlDt='" + cpbgArvlDt + '\'' +
                ", cpbgArvlTm='" + cpbgArvlTm + '\'' +
                ", cpbgArvlTprt='" + cpbgArvlTprt + '\'' +
                ", cpbgMvntDivCd='" + cpbgMvntDivCd + '\'' +
                ", cpbgMvntDivNm='" + cpbgMvntDivNm + '\'' +
                ", rm='" + rm + '\'' +
                ", majrTrstInstYn='" + majrTrstInstYn + '\'' +
                ", majrAllowYn='" + majrAllowYn + '\'' +
                ", deptCd='" + deptCd + '\'' +
                '}';
    }
}

