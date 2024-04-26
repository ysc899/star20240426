package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


import java.util.ArrayList;

/**
 * 고객 정보 조회 PO
 */
public class NemoCustomerInfoPO {
    /** 고객코드 */
    private String custCd;


    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }


    @Override
    public String toString() {
        return "NemoCustomerInfoPO{" +
                "custCd='" + custCd + '\'' +
                '}';
    }
}
