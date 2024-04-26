package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerVOCListRO implements Serializable {

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
    @SerializedName("custCnslNo")
    private String custCnslNo;

    /** 고객코드 */
    @SerializedName("custCd")
    private String custCd;

    /** 고객명 */
    @SerializedName("custNm")
    private String custNm;

    /**  */
    @SerializedName("custCnslDtm")
    private String custCnslDtm;

    /**  */
    @SerializedName("cnsrEmpNo")
    private String cnsrEmpNo;

    /**  */
    @SerializedName("cnsrNm")
    private String cnsrNm;

    /**  */
    @SerializedName("custInqrCont")
    private String custInqrCont;

    /**  */
    @SerializedName("cllrAnswCont")
    private String cllrAnswCont;



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

    public String getCustCnslNo() {
        return custCnslNo;
    }

    public void setCustCnslNo(String custCnslNo) {
        this.custCnslNo = custCnslNo;
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

    public String getCustCnslDtm() {
        return custCnslDtm;
    }

    public void setCustCnslDtm(String custCnslDtm) {
        this.custCnslDtm = custCnslDtm;
    }

    public String getCnsrEmpNo() {
        return cnsrEmpNo;
    }

    public void setCnsrEmpNo(String cnsrEmpNo) {
        this.cnsrEmpNo = cnsrEmpNo;
    }

    public String getCnsrNm() {
        return cnsrNm;
    }

    public void setCnsrNm(String cnsrNm) {
        this.cnsrNm = cnsrNm;
    }

    public String getCustInqrCont() {
        return custInqrCont;
    }

    public void setCustInqrCont(String custInqrCont) {
        this.custInqrCont = custInqrCont;
    }

    public String getCllrAnswCont() {
        return cllrAnswCont;
    }

    public void setCllrAnswCont(String cllrAnswCont) {
        this.cllrAnswCont = cllrAnswCont;
    }


    @Override
    public String toString() {
        return "NemoCustomerVOCListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", custCnslNo='" + custCnslNo + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", custCnslDtm='" + custCnslDtm + '\'' +
                ", cnsrEmpNo='" + cnsrEmpNo + '\'' +
                ", cnsrNm='" + cnsrNm + '\'' +
                ", custInqrCont='" + custInqrCont + '\'' +
                ", cllrAnswCont='" + cllrAnswCont + '\'' +
                '}';
    }
}
