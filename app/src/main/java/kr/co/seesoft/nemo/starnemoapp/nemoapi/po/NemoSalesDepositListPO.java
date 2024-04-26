package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 접수 정보 조회 PO
 */
public class NemoSalesDepositListPO {

    /** 고객코드 */
    private String custCd;

    /** 조회일자 */
    private String searchYm;


    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getSearchYm() {
        return searchYm;
    }

    public void setSearchYm(String searchYm) {
        this.searchYm = searchYm;
    }


    @Override
    public String toString() {
        return "NemoSalesDepositListPO{" +
                "custCd='" + custCd + '\'' +
                "searchYm='" + searchYm + '\'' +
                '}';
    }
}
