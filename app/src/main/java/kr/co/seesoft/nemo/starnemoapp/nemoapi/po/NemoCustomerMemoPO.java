package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 메모 정보 조회 PO
 */
public class NemoCustomerMemoPO {
    /** 등록자 */
    private String userId;

    /** 요청일 */
    private String cmplRqstDt;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCmplRqstDt() {
        return cmplRqstDt;
    }

    public void setCmplRqstDt(String cmplRqstDt) {
        this.cmplRqstDt = cmplRqstDt;
    }


    @Override
    public String toString() {
        return "NemoCustomerMemoPO{" +
                "userId='" + userId + '\'' +
                ", cmplRqstDt='" + cmplRqstDt + '\'' +
                '}';
    }
}
