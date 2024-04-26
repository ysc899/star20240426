package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoSalesApprovalListRO implements Serializable {

    private String regDtm;
    private String rgurId;
    private String updtDtm;
    private String upurId;
    private String aprvDt; /* 승인일자 approval-date */
    private String custCd; /* 고객코드 */
    private Long seqn; /* 순번 */
    private String chgrId; /* 담당자아이디 */
    private Long aprvAmt; /* 승인금액 total-amount */
    private String devcNm; /* 장비명 device-name */
    private String devcCertInfo; /* 장비인증정보 device-auth-info */
    private String devcCertVer; /* 장비인증버전 device-auth-ver */
    private String devcPrtNo; /* 장비제품번호 device-serial */
    private String afsronNm; /* 가맹점주명 business-owner-name */
    private String afsrNm; /* 가맹점명 business-name */
    private String afsrTelno; /* 가맹점전화번호 business-phone-no */
    private String afsrAddr; /* 가맹점주소 business-address */
    private String scrnMesg; /* 화면메시지 display-msg */
    private String pmntAprvCd; /* 결제승인코드 response-code */
    private String aprvNo; /* 승인번호 approval-no */
    private String crdNo; /* 카드번호 card-no */
    private String tradUniqNo; /* 거래고유번호 unique-no */
    private String ccmpnAftNo; /* 카드사가맹번호 merchant-no */
    private String aqirInfo; /* 매입사정보 acquire-info */
    private String issrInfo; /* 발급사정보 issuer-info */
    private String rciptTitl; /* 영수증제목 receipt-title */
    private String rciptCont; /* 영수증내용 receipt-msg */
    private String tradSno; /* 거래일련번호 pg-tran-seq */
    private String scrnIndiMesg; /* 화면표시메시지 display-msg */
    private String srvcOprtRslt; /* 서비스작동결과 service-result */
    private String errCont; /* 오류내용 service-description */
    private String aprvRqstSno; /* 승인요청일련번호 van-tran-seq */
    private String blkeyEftnsYn; /* 빌키유효성여부 enable-bill-key */
    private String addtInfo; /* 추가정보 filler1 */

    private String aprvRqstType;
    private String tradType;
    private Long tax;
    private Long srvchr;
    private String instmMmCnt;
    private String devc;
    private String afsr;
    private String bizrno;
    private String afsrForm;
    private String vanCmmuSetp;
    private String pgCmmuSetp;
    private String scrtyCmmuSetp;
    private String devcSwVer;
    private String sgntImgPath;
    private String sgntBmpPath;
    private String aprvTm;
    private String aprvDtm;

    private String canceled;        /* Y - 승인 취소 , N - 승인 */

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

    public String getAprvDt() {
        return aprvDt;
    }

    public void setAprvDt(String aprvDt) {
        this.aprvDt = aprvDt;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getChgrId() {
        return chgrId;
    }

    public void setChgrId(String chgrId) {
        this.chgrId = chgrId;
    }

    public String getDevcNm() {
        return devcNm;
    }

    public void setDevcNm(String devcNm) {
        this.devcNm = devcNm;
    }

    public String getDevcCertInfo() {
        return devcCertInfo;
    }

    public void setDevcCertInfo(String devcCertInfo) {
        this.devcCertInfo = devcCertInfo;
    }

    public String getDevcCertVer() {
        return devcCertVer;
    }

    public void setDevcCertVer(String devcCertVer) {
        this.devcCertVer = devcCertVer;
    }

    public String getDevcPrtNo() {
        return devcPrtNo;
    }

    public void setDevcPrtNo(String devcPrtNo) {
        this.devcPrtNo = devcPrtNo;
    }

    public String getAfsronNm() {
        return afsronNm;
    }

    public void setAfsronNm(String afsronNm) {
        this.afsronNm = afsronNm;
    }

    public String getAfsrNm() {
        return afsrNm;
    }

    public void setAfsrNm(String afsrNm) {
        this.afsrNm = afsrNm;
    }

    public String getAfsrTelno() {
        return afsrTelno;
    }

    public void setAfsrTelno(String afsrTelno) {
        this.afsrTelno = afsrTelno;
    }

    public String getAfsrAddr() {
        return afsrAddr;
    }

    public void setAfsrAddr(String afsrAddr) {
        this.afsrAddr = afsrAddr;
    }

    public String getScrnMesg() {
        return scrnMesg;
    }

    public void setScrnMesg(String scrnMesg) {
        this.scrnMesg = scrnMesg;
    }

    public String getPmntAprvCd() {
        return pmntAprvCd;
    }

    public void setPmntAprvCd(String pmntAprvCd) {
        this.pmntAprvCd = pmntAprvCd;
    }

    public String getAprvNo() {
        return aprvNo;
    }

    public void setAprvNo(String aprvNo) {
        this.aprvNo = aprvNo;
    }

    public String getCrdNo() {
        return crdNo;
    }

    public void setCrdNo(String crdNo) {
        this.crdNo = crdNo;
    }

    public String getTradUniqNo() {
        return tradUniqNo;
    }

    public void setTradUniqNo(String tradUniqNo) {
        this.tradUniqNo = tradUniqNo;
    }

    public String getCcmpnAftNo() {
        return ccmpnAftNo;
    }

    public void setCcmpnAftNo(String ccmpnAftNo) {
        this.ccmpnAftNo = ccmpnAftNo;
    }

    public String getAqirInfo() {
        return aqirInfo;
    }

    public void setAqirInfo(String aqirInfo) {
        this.aqirInfo = aqirInfo;
    }

    public String getIssrInfo() {
        return issrInfo;
    }

    public void setIssrInfo(String issrInfo) {
        this.issrInfo = issrInfo;
    }

    public String getRciptTitl() {
        return rciptTitl;
    }

    public void setRciptTitl(String rciptTitl) {
        this.rciptTitl = rciptTitl;
    }

    public String getRciptCont() {
        return rciptCont;
    }

    public void setRciptCont(String rciptCont) {
        this.rciptCont = rciptCont;
    }

    public String getTradSno() {
        return tradSno;
    }

    public void setTradSno(String tradSno) {
        this.tradSno = tradSno;
    }

    public String getScrnIndiMesg() {
        return scrnIndiMesg;
    }

    public void setScrnIndiMesg(String scrnIndiMesg) {
        this.scrnIndiMesg = scrnIndiMesg;
    }

    public String getSrvcOprtRslt() {
        return srvcOprtRslt;
    }

    public void setSrvcOprtRslt(String srvcOprtRslt) {
        this.srvcOprtRslt = srvcOprtRslt;
    }

    public String getErrCont() {
        return errCont;
    }

    public void setErrCont(String errCont) {
        this.errCont = errCont;
    }

    public String getAprvRqstSno() {
        return aprvRqstSno;
    }

    public void setAprvRqstSno(String aprvRqstSno) {
        this.aprvRqstSno = aprvRqstSno;
    }

    public String getBlkeyEftnsYn() {
        return blkeyEftnsYn;
    }

    public void setBlkeyEftnsYn(String blkeyEftnsYn) {
        this.blkeyEftnsYn = blkeyEftnsYn;
    }

    public String getAddtInfo() {
        return addtInfo;
    }

    public void setAddtInfo(String addtInfo) {
        this.addtInfo = addtInfo;
    }

    public Long getSeqn() {
        return seqn;
    }

    public void setSeqn(Long seqn) {
        this.seqn = seqn;
    }

    public Long getAprvAmt() {
        return aprvAmt;
    }

    public void setAprvAmt(Long aprvAmt) {
        this.aprvAmt = aprvAmt;
    }

    public String getAprvRqstType() {
        return aprvRqstType;
    }

    public void setAprvRqstType(String aprvRqstType) {
        this.aprvRqstType = aprvRqstType;
    }

    public String getTradType() {
        return tradType;
    }

    public void setTradType(String tradType) {
        this.tradType = tradType;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public Long getSrvchr() {
        return srvchr;
    }

    public void setSrvchr(Long srvchr) {
        this.srvchr = srvchr;
    }

    public String getInstmMmCnt() {
        return instmMmCnt;
    }

    public void setInstmMmCnt(String instmMmCnt) {
        this.instmMmCnt = instmMmCnt;
    }

    public String getDevc() {
        return devc;
    }

    public void setDevc(String devc) {
        this.devc = devc;
    }

    public String getAfsr() {
        return afsr;
    }

    public void setAfsr(String afsr) {
        this.afsr = afsr;
    }

    public String getBizrno() {
        return bizrno;
    }

    public void setBizrno(String bizrno) {
        this.bizrno = bizrno;
    }

    public String getAfsrForm() {
        return afsrForm;
    }

    public void setAfsrForm(String afsrForm) {
        this.afsrForm = afsrForm;
    }

    public String getVanCmmuSetp() {
        return vanCmmuSetp;
    }

    public void setVanCmmuSetp(String vanCmmuSetp) {
        this.vanCmmuSetp = vanCmmuSetp;
    }

    public String getPgCmmuSetp() {
        return pgCmmuSetp;
    }

    public void setPgCmmuSetp(String pgCmmuSetp) {
        this.pgCmmuSetp = pgCmmuSetp;
    }

    public String getScrtyCmmuSetp() {
        return scrtyCmmuSetp;
    }

    public void setScrtyCmmuSetp(String scrtyCmmuSetp) {
        this.scrtyCmmuSetp = scrtyCmmuSetp;
    }

    public String getDevcSwVer() {
        return devcSwVer;
    }

    public void setDevcSwVer(String devcSwVer) {
        this.devcSwVer = devcSwVer;
    }

    public String getSgntImgPath() {
        return sgntImgPath;
    }

    public void setSgntImgPath(String sgntImgPath) {
        this.sgntImgPath = sgntImgPath;
    }

    public String getSgntBmpPath() {
        return sgntBmpPath;
    }

    public void setSgntBmpPath(String sgntBmpPath) {
        this.sgntBmpPath = sgntBmpPath;
    }

    public String getAprvTm() {
        return aprvTm;
    }

    public void setAprvTm(String aprvTm) {
        this.aprvTm = aprvTm;
    }

    public String getAprvDtm() {
        return aprvDtm;
    }

    public void setAprvDtm(String aprvDtm) {
        this.aprvDtm = aprvDtm;
    }

    public String getCanceled() {
        return canceled;
    }

    public void setCanceled(String canceled) {
        this.canceled = canceled;
    }


    @Override
    public String toString() {
        return "NemoSalesApprovalListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", aprvDt='" + aprvDt + '\'' +
                ", custCd='" + custCd + '\'' +
                ", seqn='" + seqn + '\'' +
                ", chgrId='" + chgrId + '\'' +
                ", aprvAmt='" + aprvAmt + '\'' +
                ", devcNm='" + devcNm + '\'' +
                ", devcCertInfo='" + devcCertInfo + '\'' +
                ", devcCertVer='" + devcCertVer + '\'' +
                ", devcPrtNo='" + devcPrtNo + '\'' +
                ", afsronNm='" + afsronNm + '\'' +
                ", afsrNm='" + afsrNm + '\'' +
                ", afsrTelno='" + afsrTelno + '\'' +
                ", afsrAddr='" + afsrAddr + '\'' +
                ", scrnMesg='" + scrnMesg + '\'' +
                ", pmntAprvCd='" + pmntAprvCd + '\'' +
                ", aprvNo='" + aprvNo + '\'' +
                ", crdNo='" + crdNo + '\'' +
                ", tradUniqNo='" + tradUniqNo + '\'' +
                ", ccmpnAftNo='" + ccmpnAftNo + '\'' +
                ", aqirInfo='" + aqirInfo + '\'' +
                ", issrInfo='" + issrInfo + '\'' +
                ", rciptTitl='" + rciptTitl + '\'' +
                ", rciptCont='" + rciptCont + '\'' +
                ", tradSno='" + tradSno + '\'' +
                ", scrnIndiMesg='" + scrnIndiMesg + '\'' +
                ", srvcOprtRslt='" + srvcOprtRslt + '\'' +
                ", errCont='" + errCont + '\'' +
                ", aprvRqstSno='" + aprvRqstSno + '\'' +
                ", blkeyEftnsYn='" + blkeyEftnsYn + '\'' +
                ", addtInfo='" + addtInfo + '\'' +
                ", aprvRqstType='" + aprvRqstType + '\'' +
                ", tradType='" + tradType + '\'' +
                ", tax='" + tax + '\'' +
                ", srvchr='" + srvchr + '\'' +
                ", instmMmCnt='" + instmMmCnt + '\'' +
                ", devc='" + devc + '\'' +
                ", afsr='" + afsr + '\'' +
                ", bizrno='" + bizrno + '\'' +
                ", afsrForm='" + afsrForm + '\'' +
                ", vanCmmuSetp='" + vanCmmuSetp + '\'' +
                ", pgCmmuSetp='" + pgCmmuSetp + '\'' +
                ", scrtyCmmuSetp='" + scrtyCmmuSetp + '\'' +
                ", devcSwVer='" + devcSwVer + '\'' +
                ", sgntImgPath='" + sgntImgPath + '\'' +
                ", sgntBmpPath='" + sgntBmpPath + '\'' +
                ", aprvTm='" + aprvTm + '\'' +
                ", aprvDtm='" + aprvDtm + '\'' +
                ", canceled='" + canceled + '\'' +
                '}';
    }
}

