package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객지원 조회 PO
 */
public class NemoCustomerSupportListPO {
    /** 부서코드 */
    private String deptCd;

    /** 조회일 */
    private String searchDt;

    /** 요청 페이지 */
    private int pageIndex;

    /** 요청 페이지 사이즈 */
    private int pageSize;


    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
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
        return "NemoCustomerSupportListPO{" +
                "deptCd='" + deptCd + '\'' +
                ", searchDt='" + searchDt + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
