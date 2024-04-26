package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerDetailNoticeListRO implements Serializable {

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
    @SerializedName("uisuDivCd")
    private String uisuDivCd;

    /**  */
    @SerializedName("uisuDivNm")
    private String uisuDivNm;

    /**  */
    @SerializedName("uisu")
    private String uisu;

    /**  */
    @SerializedName("useYn")
    private String useYn;


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

    public String getUisuDivCd() {
        return uisuDivCd;
    }

    public void setUisuDivCd(String uisuDivCd) {
        this.uisuDivCd = uisuDivCd;
    }

    public String getUisuDivNm() {
        return uisuDivNm;
    }

    public void setUisuDivNm(String uisuDivNm) {
        this.uisuDivNm = uisuDivNm;
    }

    public String getUisu() {
        return uisu;
    }

    public void setUisu(String uisu) {
        this.uisu = uisu;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }



    @Override
    public String toString() {
        return "NemoCustomerDetailNoticeListRO{" +
                "regDtm='" + regDtm + '\'' +
                ", rgurId='" + rgurId + '\'' +
                ", updtDtm='" + updtDtm + '\'' +
                ", upurId='" + upurId + '\'' +
                ", custCd='" + custCd + '\'' +
                ", uisuDivCd='" + uisuDivCd + '\'' +
                ", uisuDivNm='" + uisuDivNm + '\'' +
                ", uisu='" + uisu + '\'' +
                ", useYn='" + useYn + '\'' +
                '}';
    }
}
