package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerMemoCodeListRO implements Serializable {

    /**  */
    @SerializedName("ctgrId")
    private String ctgrId;

    /**  */
    @SerializedName("cmmnCd")
    private String cmmnCd;

    /**  */
    @SerializedName("cmmnCdNm")
    private String cmmnCdNm;

    /**  */
    @SerializedName("cdAbbrNm")
    private String cdAbbrNm;

    /**  */
    @SerializedName("upprCd")
    private String upprCd;

    /**  */
    @SerializedName("sortSeq")
    private String sortSeq;

    /**  */
    @SerializedName("useYn")
    private String useYn;

    /**  */
    @SerializedName("cmmnCdNmCont")
    private String cmmnCdNmCont;

    /**  */
    @SerializedName("itemCd01")
    private String itemCd01;

    /**  */
    @SerializedName("itemCd02")
    private String itemCd02;

    /**  */
    @SerializedName("itemCd03")
    private String itemCd03;

    /**  */
    @SerializedName("itemCd04")
    private String itemCd04;


    public String getCtgrId() {
        return ctgrId;
    }

    public void setCtgrId(String ctgrId) {
        this.ctgrId = ctgrId;
    }

    public String getCmmnCd() {
        return cmmnCd;
    }

    public void setCmmnCd(String cmmnCd) {
        this.cmmnCd = cmmnCd;
    }

    public String getCmmnCdNm() {
        return cmmnCdNm;
    }

    public void setCmmnCdNm(String cmmnCdNm) {
        this.cmmnCdNm = cmmnCdNm;
    }

    public String getCdAbbrNm() {
        return cdAbbrNm;
    }

    public void setCdAbbrNm(String cdAbbrNm) {
        this.cdAbbrNm = cdAbbrNm;
    }

    public String getUpprCd() {
        return upprCd;
    }

    public void setUpprCd(String upprCd) {
        this.upprCd = upprCd;
    }

    public String getSortSeq() {
        return sortSeq;
    }

    public void setSortSeq(String sortSeq) {
        this.sortSeq = sortSeq;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getCmmnCdNmCont() {
        return cmmnCdNmCont;
    }

    public void setCmmnCdNmCont(String cmmnCdNmCont) {
        this.cmmnCdNmCont = cmmnCdNmCont;
    }

    public String getItemCd01() {
        return itemCd01;
    }

    public void setItemCd01(String itemCd01) {
        this.itemCd01 = itemCd01;
    }

    public String getItemCd02() {
        return itemCd02;
    }

    public void setItemCd02(String itemCd02) {
        this.itemCd02 = itemCd02;
    }

    public String getItemCd03() {
        return itemCd03;
    }

    public void setItemCd03(String itemCd03) {
        this.itemCd03 = itemCd03;
    }

    public String getItemCd04() {
        return itemCd04;
    }

    public void setItemCd04(String itemCd04) {
        this.itemCd04 = itemCd04;
    }



    @Override
    public String toString() {
        return "NemoCustomerMemoCodeListRO{" +
                "ctgrId='" + ctgrId + '\'' +
                ", cmmnCd='" + cmmnCd + '\'' +
                ", cmmnCdNm='" + cmmnCdNm + '\'' +
                ", cdAbbrNm='" + cdAbbrNm + '\'' +
                ", upprCd='" + upprCd + '\'' +
                ", sortSeq='" + sortSeq + '\'' +
                ", useYn='" + useYn + '\'' +
                ", cmmnCdNmCont='" + cmmnCdNmCont + '\'' +
                ", itemCd01='" + itemCd01 + '\'' +
                ", itemCd01='" + itemCd02 + '\'' +
                ", itemCd01='" + itemCd03 + '\'' +
                ", itemCd01='" + itemCd04 + '\'' +
                '}';
    }
}
