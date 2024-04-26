package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 상세 정보 조회 PO
 */
public class NemoCustomerDetailPO {
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
        return "NemoCustomerDetailPO{" +
                "custCd='" + custCd + '\'' +
                '}';
    }
}
