package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoCustomerSupportReceiptRO implements Serializable {

    /**  */
    public String recpDt;

    /**  */
    public String recpNo;

    /**  */
    public String custCd;

    /**  */
    public String custNm;

    /**  */
    public String patnNm;

    /**  */
    public String brncCd;

    /**  */
    public String brncNm;


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

    public String getBrncNm() {
        return brncNm;
    }

    public void setBrncNm(String brncNm) {
        this.brncNm = brncNm;
    }


    @Override
    public String toString() {
        return "NemoCustomerSupportReceiptRO{" +
                "recpDt='" + recpDt + '\'' +
                ", recpNo='" + recpNo + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", patnNm='" + patnNm + '\'' +
                ", brncCd='" + brncCd + '\'' +
                ", brncNm='" + brncNm + '\'' +
                '}';
    }
}
