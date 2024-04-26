package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerDetailListRO implements Serializable {

    /**  */
    @SerializedName("regDtm")
    private String regDtm;

    /**  */
    @SerializedName("rgurId")
    private String rgurId;

    /**  */
    @SerializedName("updtDtm")
    private String updtDtm;

    /**  */
    @SerializedName("upurId")
    private String upurId;

    /**  */
    @SerializedName("salsMstRefrUkeyid")
    private String salsMstRefrUkeyid;

    /**  */
    @SerializedName("wrtnDtm")
    private String wrtnDtm;

    /** 성명 */
    @SerializedName("cmjprsnNm")
    private String cmjprsnNm;

    /**  */
    @SerializedName("cmjprsnDeptNm")
    private String cmjprsnDeptNm;

    /** 직급 */
    @SerializedName("ccomOfpoNm")
    private String ccomOfpoNm;

    /** 연락처 */
    @SerializedName("cinfo")
    private String cinfo;

    /** Email */
    @SerializedName("custEmalAddr")
    private String custEmalAddr;

    /**  */
    @SerializedName("grdtSchlNm")
    private String grdtSchlNm;

    /**  */
    @SerializedName("anvrDivNm")
    private String anvrDivNm;

    /**  */
    @SerializedName("anvrDd")
    private String anvrDd;
    /**  */
    @SerializedName("cntcMbyYn")
    private String cntcMbyYn;
    /**  */
    @SerializedName("rm")
    private String rm;
    /**  */
    @SerializedName("useYn")
    private String useYn;
    /**  */
    @SerializedName("custCd")
    private String custCd;
    /**  */
    @SerializedName("careInstUkeyid")
    private String careInstUkeyid;
    /**  */
    @SerializedName("careInstNo")
    private String careInstNo;



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

    public String getSalsMstRefrUkeyid() {
        return salsMstRefrUkeyid;
    }

    public void setSalsMstRefrUkeyid(String salsMstRefrUkeyid) {
        this.salsMstRefrUkeyid = salsMstRefrUkeyid;
    }

    public String getWrtnDtm() {
        return wrtnDtm;
    }

    public void setWrtnDtm(String wrtnDtm) {
        this.wrtnDtm = wrtnDtm;
    }

    public String getCmjprsnNm() {
        return cmjprsnNm;
    }

    public void setCmjprsnNm(String cmjprsnNm) {
        this.cmjprsnNm = cmjprsnNm;
    }

    public String getCmjprsnDeptNm() {
        return cmjprsnDeptNm;
    }

    public void setCmjprsnDeptNm(String cmjprsnDeptNm) {
        this.cmjprsnDeptNm = cmjprsnDeptNm;
    }

    public String getCcomOfpoNm() {
        return ccomOfpoNm;
    }

    public void setCcomOfpoNm(String ccomOfpoNm) {
        this.ccomOfpoNm = ccomOfpoNm;
    }

    public String getCinfo() {
        return cinfo;
    }

    public void setCinfo(String cinfo) {
        this.cinfo = cinfo;
    }

    public String getCustEmalAddr() {
        return custEmalAddr;
    }

    public void setCustEmalAddr(String custEmalAddr) {
        this.custEmalAddr = custEmalAddr;
    }

    public String getGrdtSchlNm() {
        return grdtSchlNm;
    }

    public void setGrdtSchlNm(String grdtSchlNm) {
        this.grdtSchlNm = grdtSchlNm;
    }

    public String getAnvrDivNm() {
        return anvrDivNm;
    }

    public void setAnvrDivNm(String anvrDivNm) {
        this.anvrDivNm = anvrDivNm;
    }

    public String getAnvrDd() {
        return anvrDd;
    }

    public void setAnvrDd(String anvrDd) {
        this.anvrDd = anvrDd;
    }

    public String getCntcMbyYn() {
        return cntcMbyYn;
    }

    public void setCntcMbyYn(String cntcMbyYn) {
        this.cntcMbyYn = cntcMbyYn;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getCareInstUkeyid() {
        return careInstUkeyid;
    }

    public void setCareInstUkeyid(String careInstUkeyid) {
        this.careInstUkeyid = careInstUkeyid;
    }

    public String getCareInstNo() {
        return careInstNo;
    }

    public void setCareInstNo(String careInstNo) {
        this.careInstNo = careInstNo;
    }



    @Override
    public String toString() {
        return "NemoCustomerDetailListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", salsMstRefrUkeyid='" + salsMstRefrUkeyid + '\'' +
                ", wrtnDtm='" + wrtnDtm + '\'' +
                ", cmjprsnNm='" + cmjprsnNm + '\'' +
                ", cmjprsnDeptNm='" + cmjprsnDeptNm + '\'' +
                ", ccomOfpoNm='" + ccomOfpoNm + '\'' +
                ", cinfo='" + cinfo + '\'' +
                ", custEmalAddr='" + custEmalAddr + '\'' +
                ", grdtSchlNm='" + grdtSchlNm + '\'' +
                ", anvrDivNm='" + anvrDivNm + '\'' +
                ", anvrDd='" + anvrDd + '\'' +
                ", cntcMbyYn='" + cntcMbyYn + '\'' +
                ", rm='" + rm + '\'' +
                ", useYn='" + useYn + '\'' +
                ", custCd='" + custCd + '\'' +
                ", careInstUkeyid='" + careInstUkeyid + '\'' +
                ", careInstNo='" + careInstNo + '\'' +
                '}';
    }
}
