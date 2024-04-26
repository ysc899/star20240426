package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class NemoCustomerSupportListRO implements Serializable {

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

    @SerializedName("totalDataCount")
    public int totalDataCount;

    /** 등록일 */
    @SerializedName("regDt")
    public String regDt;

    /** 접수일 */
    @SerializedName("recpDt")
    public String recpDt;

    /** 접수번호 */
    @SerializedName("recpNo")
    public String recpNo;

    /**  */
    @SerializedName("recpSno")
    public String recpSno;

    /**  */
    @SerializedName("custSuptUpdtRqstTypeCd")
    public String custSuptUpdtRqstTypeCd;

    /**  */
    @SerializedName("custSuptPrcsDtm")
    public String custSuptPrcsDtm;

    /**  */
    @SerializedName("custRqstSno")
    public String custRqstSno;

    /**  */
    @SerializedName("rgurDeptCd")
    public String rgurDeptCd;

    /**  */
    @SerializedName("rgurDeptNm")
    public String rgurDeptNm;

    /**  */
    @SerializedName("custSuptUpdtRqstStatCd")
    public String custSuptUpdtRqstStatCd;

    /**  */
    @SerializedName("custSuptUpdtRqstStatNm")
    public String custSuptUpdtRqstStatNm;

    /**  */
    @SerializedName("custSuptPrcsUserId")
    public String custSuptPrcsUserId;

    /**  */
    @SerializedName("custSuptPrcsUserNm")
    public String custSuptPrcsUserNm;

    /**  */
    @SerializedName("custRqstIssu")
    public String custRqstIssu;

    /**  */
    @SerializedName("custSuptPrcsIssu")
    public String custSuptPrcsIssu;

    /**  */
    @SerializedName("custCd")
    public String custCd;

    /**  */
    @SerializedName("custNm")
    public String custNm;

    /**  */
    @SerializedName("patnNm")
    public String patnNm;

    /**  */
    @SerializedName("brncCd")
    public String brncCd;


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

    public int getTotalDataCount() { return totalDataCount; }

    public void setTotalDataCount(int totalDataCount) { this.totalDataCount = totalDataCount; }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getRecpDt() {
        return recpDt;
    }

    public void setRecpDt(String recpDt) {
        this.recpDt = recpDt;
    }

    public String getRecpNo() {
        return recpNo;
    }

    public void setRecpNo(String recpNo) {
        this.recpNo = recpNo;
    }

    public String getRecpSno() {
        return recpSno;
    }

    public void setRecpSno(String recpSno) {
        this.recpSno = recpSno;
    }

    public String getCustSuptUpdtRqstTypeCd() {
        return custSuptUpdtRqstTypeCd;
    }

    public void setCustSuptUpdtRqstTypeCd(String custSuptUpdtRqstTypeCd) {
        this.custSuptUpdtRqstTypeCd = custSuptUpdtRqstTypeCd;
    }

    public String getCustSuptPrcsDtm() {
        return custSuptPrcsDtm;
    }

    public void setCustSuptPrcsDtm(String custSuptPrcsDtm) {
        this.custSuptPrcsDtm = custSuptPrcsDtm;
    }

    public String getCustRqstSno() {
        return custRqstSno;
    }

    public void setCustRqstSno(String custRqstSno) {
        this.custRqstSno = custRqstSno;
    }

    public String getRgurDeptCd() {
        return rgurDeptCd;
    }

    public void setRgurDeptCd(String rgurDeptCd) {
        this.rgurDeptCd = rgurDeptCd;
    }

    public String getRgurDeptNm() {
        return rgurDeptNm;
    }

    public void setRgurDeptNm(String rgurDeptNm) {
        this.rgurDeptNm = rgurDeptNm;
    }

    public String getCustSuptUpdtRqstStatCd() {
        return custSuptUpdtRqstStatCd;
    }

    public void setCustSuptUpdtRqstStatCd(String custSuptUpdtRqstStatCd) {
        this.custSuptUpdtRqstStatCd = custSuptUpdtRqstStatCd;
    }

    public String getCustSuptUpdtRqstStatNm() {
        return custSuptUpdtRqstStatNm;
    }

    public void setCustSuptUpdtRqstStatNm(String custSuptUpdtRqstStatNm) {
        this.custSuptUpdtRqstStatNm = custSuptUpdtRqstStatNm;
    }

    public String getCustSuptPrcsUserId() {
        return custSuptPrcsUserId;
    }

    public void setCustSuptPrcsUserId(String custSuptPrcsUserId) {
        this.custSuptPrcsUserId = custSuptPrcsUserId;
    }

    public String getCustSuptPrcsUserNm() {
        return custSuptPrcsUserNm;
    }

    public void setCustSuptPrcsUserNm(String custSuptPrcsUserNm) {
        this.custSuptPrcsUserNm = custSuptPrcsUserNm;
    }

    public String getCustRqstIssu() {
        return custRqstIssu;
    }

    public void setCustRqstIssu(String custRqstIssu) {
        this.custRqstIssu = custRqstIssu;
    }

    public String getCustSuptPrcsIssu() {
        return custSuptPrcsIssu;
    }

    public void setCustSuptPrcsIssu(String custSuptPrcsIssu) {
        this.custSuptPrcsIssu = custSuptPrcsIssu;
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

    public String getPatnNm() {
        return patnNm;
    }

    public void setPatnNm(String patnNm) {
        this.patnNm = patnNm;
    }

    public String getBrncCd() {
        return brncCd;
    }

    public void setBrncCd(String brncCd) {
        this.brncCd = brncCd;
    }


    @Override
    public String toString() {
        return "NemoCustomerSupportListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", totalDataCount='" + totalDataCount + '\'' +
                ", recpDt='" + recpDt + '\'' +
                ", recpNo='" + recpNo + '\'' +
                ", recpSno='" + recpSno + '\'' +
                ", custSuptUpdtRqstTypeCd='" + custSuptUpdtRqstTypeCd + '\'' +
                ", custSuptPrcsDtm='" + custSuptPrcsDtm + '\'' +
                ", custRqstSno='" + custRqstSno + '\'' +
                ", rgurDeptCd='" + rgurDeptCd + '\'' +
                ", rgurDeptNm='" + rgurDeptNm + '\'' +
                ", custSuptUpdtRqstStatCd='" + custSuptUpdtRqstStatCd + '\'' +
                ", custSuptUpdtRqstStatNm='" + custSuptUpdtRqstStatNm + '\'' +
                ", custSuptPrcsUserId='" + custSuptPrcsUserId + '\'' +
                ", custSuptPrcsUserNm='" + custSuptPrcsUserNm + '\'' +
                ", custRqstIssu='" + custRqstIssu + '\'' +
                ", custSuptPrcsIssu='" + custSuptPrcsIssu + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", patnNm='" + patnNm + '\'' +
                ", brncCd='" + brncCd + '\'' +
                '}';
    }
}

