package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * VOC 조회 PO
 */
public class NemoCustomerVOCListPO {
    /** 고객코드 */
    private String custCd;

    /** 조회일 */
    private String searchDt;

    /** 요청 페이지 */
    private int pageIndex;

    /** 요청 페이지 사이즈 */
    private int pageSize;


    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getSearchDt() {
        return searchDt;
    }

    public void setSearchDt(String searchDt) {
        this.searchDt = searchDt;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex){
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }


    @Override
    public String toString() {
        return "NemoCustomerVOCPO{" +
                "custCd='" + custCd + '\'' +
                ", searchDt='" + searchDt + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
