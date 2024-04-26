package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerMemoListRO implements Serializable {

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
    @SerializedName("custCd")
    private String custCd;

    /**  */
    @SerializedName("custNm")
    private String custNm;

    /**  */
    @SerializedName("cmplRqstDt")
    private String cmplRqstDt;

    /**  */
    @SerializedName("seqn")
    private int seqn;

    /**  */
    @SerializedName("memo")
    private String memo;


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

    public String getCmplRqstDt() {
        return cmplRqstDt;
    }

    public void setCmplRqstDt(String cmplRqstDt) {
        this.cmplRqstDt = cmplRqstDt;
    }

    public int getSeqn() {
        return seqn;
    }

    public void setSeqn(int seqn) {
        this.seqn = seqn;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }



    @Override
    public String toString() {
        return "NemoCustomerMemoListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", cmplRqstDt='" + cmplRqstDt + '\'' +
                ", seqn='" + seqn + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
