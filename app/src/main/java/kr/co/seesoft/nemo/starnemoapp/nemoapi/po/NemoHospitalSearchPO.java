package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 병원 검색용 PO
 */
public class NemoHospitalSearchPO {
    /** id */
    private String userId;

    /** 담당자부서 */
    private String deptCd;

    /** 고객상태코드 (NORMAL:정상,REQUEST_STOP:의뢰정지,TRANSACTION_STOP:거래정지) */
    private String cstatCd;
    
    /** 구분 
     *  my: 내 담당 병원만 조회
     *  br: 지점 모든 병원 조회
     *  rnd: rnd관련
     * */
    private String type;

    /** 검색 구분
     *  custNm: 고객명
     *  custcd: 고객코드
     *  chgrId: 담당자
     * */
    private String searchType;

    /** 검색어 */
    private String searchTxt;

    /** 요청 페이지 */
    private int pageIndex;

    /** 요청 페이지 사이즈 */
    private int pageSize;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    public String getCstatCd() {
        return cstatCd;
    }

    public void setCstatCd(String cstatCd) {
        this.cstatCd = cstatCd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
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
        return "NemoHospitalSearchPO{" +
                "userId='" + userId + '\'' +
                ", deptCd='" + deptCd + '\'' +
                ", cstatCd='" + cstatCd + '\'' +
                ", type='" + type + '\'' +
                ", searchType='" + searchType + '\'' +
                ", searchTxt='" + searchTxt + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
