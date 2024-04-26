package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerInfoRndRO implements Serializable {

    /** 지역본부코드 */
    @SerializedName("lhqrCd")
    private String lhqrCd;

    /** 지역본부명 */
    @SerializedName("lhqrNm")
    private String lhqrNm;

    /** 지점코드 */
    @SerializedName("brncCd")
    private String brncCd;

    /** 지점명 */
    @SerializedName("brncNm")
    private String brncNm;

    /** 고객코드 */
    @SerializedName("custCd")
    private String custCd;

    /** 고객명 */
    @SerializedName("custNm")
    private String custNm;

    /** 대표전화번호 */
    @SerializedName("custTelno")
    private String custTelno;

    /** 담당자 아이디 */
    @SerializedName("chgrId")
    private String chgrId;

    /** 담당자명 */
    @SerializedName("chgrNm")
    private String chgrNm;

    /** 담당자 업무 연락처 */
    @SerializedName("chrgMoblPhno")
    private String chrgMoblPhno;

    /** 고객상태코드 */
    @SerializedName("cstatCd")
    private String cstatCd;

    /** 고객상태명 */
    @SerializedName("cstatNm")
    private String cstatNm;

    /** 사용여부 */
    @SerializedName("useYn")
    private String useYn;

    /** 전화번호 */
    @SerializedName("bizrNo")
    private String bizrNo;

    /** 주소 */
    @SerializedName("addr")
    private String addr;

    /** 주소 */
    @SerializedName("bplcAddr")
    private String bplcAddr;

    /** 대표자 */
    @SerializedName("rprsNm")
    private String rprsNm;

    /** Email */
    @SerializedName("custEmalAddr")
    private String custEmalAddr;

    /** 약관 코드 */
    @SerializedName("trmsCd")
    private String trmsCd;

    /** 약관 */
    @SerializedName("trmsCont")
    private String trmsCont;

    /**  */
    @SerializedName("imgUrl")
    private String imgUrl;

    /** 동의 일시 */
    @SerializedName("csntDtm")
    private String csntDtm;

    /** 동의 일시 */
    @SerializedName("rvsnDt")
    private String rvsnDt ;

    /**  */
    @SerializedName("prjtCd")
    private String prjtCd ;

    /**  */
    @SerializedName("prjtNm")
    private String prjtNm ;

    /**  */
    @SerializedName("uisu")
    private String uisu ;

    /**  */
    @SerializedName("trptEmpNo")
    private String trptEmpNo ;

    /**  */
    @SerializedName("takeManual")
    private String takeManual ;

    /**  */
    @SerializedName("rndTakePlanUkeyid")
    private String rndTakePlanUkeyid ;

    /**  */
    @SerializedName("prjtChgrNm")
    private String prjtChgrNm ;

    /**  */
    @SerializedName("fileId")
    private String fileId ;

    /**  */
    @SerializedName("fileNm")
    private String fileNm ;

    /**  */
    @SerializedName("fileUrl")
    private String fileUrl ;


    public String getLhqrCd() {
        return lhqrCd;
    }

    public void setLhqrCd(String lhqrCd) {
        this.lhqrCd = lhqrCd;
    }

    public String getLhqrNm() {
        return lhqrNm;
    }

    public void setLhqrNm(String lhqrNm) {
        this.lhqrNm = lhqrNm;
    }

    public String getBrncCd() {
        return brncCd;
    }

    public void setBrncCd(String brncCd) {
        this.brncCd = brncCd;
    }

    public String getBrncNm() {
        return brncNm;
    }

    public void setBrncNm(String brncNm) {
        this.brncNm = brncNm;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getCustTelno() {
        return custTelno;
    }

    public void setCustTelno(String custTelno) {
        this.custTelno = custTelno;
    }

    public String getChgrId() {
        return chgrId;
    }

    public void setChgrId(String chgrId) {
        this.chgrId = chgrId;
    }

    public String getChgrNm() {
        return chgrNm;
    }

    public void setChgrNm(String chgrNm) {
        this.chgrNm = chgrNm;
    }

    public String getChrgMoblPhno() {
        return chrgMoblPhno;
    }

    public void setChrgMoblPhno(String chrgMoblPhno) {
        this.chrgMoblPhno = chrgMoblPhno;
    }

    public String getCstatCd() {
        return cstatCd;
    }

    public void setCstatCd(String cstatCd) {
        this.cstatCd = cstatCd;
    }

    public String getCstatNm() {
        return cstatNm;
    }

    public void setCstatNm(String cstatNm) {
        this.cstatNm = cstatNm;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getBizrNo() {
        return bizrNo;
    }

    public void setBizrNo(String bizrNo) {
        this.bizrNo = bizrNo;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBplcAddr() {
        return bplcAddr;
    }

    public void setBplcAddr(String bplcAddr) {
        this.bplcAddr = bplcAddr;
    }

    public String getRprsNm() {
        return rprsNm;
    }

    public void setRprsNm(String rprsNm) {
        this.rprsNm = rprsNm;
    }

    public String getCustEmalAddr() {
        return custEmalAddr;
    }

    public void setCustEmalAddr(String custEmalAddr) {
        this.custEmalAddr = custEmalAddr;
    }

    public String getTrmsCd() {
        return trmsCd;
    }

    public void setTrmsCd(String trmsCd) {
        this.trmsCd = trmsCd;
    }

    public String getTrmsCont() {
        return trmsCont;
    }

    public void setTrmsCont(String trmsCont) {
        this.trmsCont = trmsCont;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCsntDtm() {
        return csntDtm;
    }

    public void setCsntDtm(String csntDtm) {
        this.csntDtm = csntDtm;
    }

    public String getRvsnDt() {
        return rvsnDt;
    }

    public void setRvsnDt(String rvsnDt) {
        this.rvsnDt = rvsnDt;
    }

    public String getPrjtCd() {
        return prjtCd;
    }

    public void setPrjtCd(String prjtCd) {
        this.prjtCd = prjtCd;
    }

    public String getPrjtNm() {
        return prjtNm;
    }

    public void setPrjtNm(String prjtNm) {
        this.prjtNm = prjtNm;
    }

    public String getUisu() {
        return uisu;
    }

    public void setUisu(String uisu) {
        this.uisu = uisu;
    }

    public String getTrptEmpNo() {
        return trptEmpNo;
    }

    public void setTrptEmpNo(String trptEmpNo) {
        this.trptEmpNo = trptEmpNo;
    }

    public String getTakeManual() {
        return takeManual;
    }

    public void setTakeManual(String takeManual) {
        this.takeManual = takeManual;
    }

    public String getRndTakePlanUkeyid() {
        return rndTakePlanUkeyid;
    }

    public void setRndTakePlanUkeyid(String rndTakePlanUkeyid) {
        this.rndTakePlanUkeyid = rndTakePlanUkeyid;
    }

    public String getPrjtChgrNm() {
        return prjtChgrNm;
    }

    public void setPrjtChgrNm(String prjtChgrNm) {
        this.prjtChgrNm = prjtChgrNm;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNm() {
        return fileNm;
    }

    public void setFileNm(String fileNm) {
        this.fileNm = fileNm;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }



    @Override
    public String toString() {
        return "NemoCustomerInfoRndRO{" +
                "lhqrCd='" + lhqrCd + '\'' +
                ", lhqrNm='" + lhqrNm + '\'' +
                ", brncCd='" + brncCd + '\'' +
                ", brncNm='" + brncNm + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", custTelno='" + custTelno + '\'' +
                ", chgrId='" + chgrId + '\'' +
                ", chgrNm='" + chgrNm + '\'' +
                ", chrgMoblPhno='" + chrgMoblPhno + '\'' +
                ", cstatCd='" + cstatCd + '\'' +
                ", cstatNm='" + cstatNm + '\'' +
                ", useYn='" + useYn + '\'' +
                ", bizrNo='" + bizrNo + '\'' +
                ", addr='" + addr + '\'' +
                ", bplcAddr='" + bplcAddr + '\'' +
                ", rprsNm='" + rprsNm + '\'' +
                ", custEmalAddr='" + custEmalAddr + '\'' +
                ", trmsCd='" + trmsCd + '\'' +
                ", trmsCont='" + trmsCont + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", csntDtm='" + csntDtm + '\'' +
                ", rvsnDt='" + rvsnDt + '\'' +
                ", prjtCd='" + prjtCd + '\'' +
                ", prjtNm='" + prjtNm + '\'' +
                ", uisu='" + uisu + '\'' +
                ", trptEmpNo='" + trptEmpNo + '\'' +
                ", takeManual='" + takeManual + '\'' +
                ", rndTakePlanUkeyid='" + rndTakePlanUkeyid + '\'' +
                ", prjtChgrNm='" + prjtChgrNm + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileNm='" + fileNm + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
