package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 임직원 목록 조회 PO
 */
public class NemoDepartmentContactListPO {
    /** 부서명 */
    private String deptNm;

    /** 임직원명 */
    private String userNm;

    /** 요청 페이지 */
    private int pageIndex;

    /** 요청 페이지 사이즈 */
    private int pageSize;

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
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
        return "NemoDepartmentContactListPO{" +
                "deptNm='" + deptNm + '\'' +
                ", userNm='" + userNm + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
