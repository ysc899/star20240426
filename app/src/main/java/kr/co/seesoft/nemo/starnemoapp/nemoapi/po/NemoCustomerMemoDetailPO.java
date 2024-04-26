package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 메모 정보 조회 PO
 */
public class NemoCustomerMemoDetailPO {
    /**  */
    private String userId;

    /** 고객코드 */
    private String custCd;

    /**  */
    private String cmplRqstDt;

    /**  */
    private int seqn;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getCmplRqstDt() {
        return cmplRqstDt;
    }

    public void setCmplRqstDt(String cmplRqstDt) {
        this.cmplRqstDt = cmplRqstDt;
    }

    public int getSeqn() {
        return seqn;
    }

    public void setSeqn(int seqn) {
        this.seqn = seqn;
    }


    @Override
    public String toString() {
        return "NemoCustomerMemoDetailPO{" +
                "userId='" + userId + '\'' +
                ", custCd='" + custCd + '\'' +
                ", cmplRqstDt='" + cmplRqstDt + '\'' +
                ", seqn='" + seqn + '\'' +
                '}';
    }
}
