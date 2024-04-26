package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 결제 내역 조회 PO
 */
public class NemoSalesApprovalListPO {

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
        return "NemoSalesApprovalListPO{" +
                "custCd='" + custCd + '\'' +
                "searchYm='" + searchYm + '\'' +
                '}';
    }
}
