package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 메모 등록 PO
 */
public class NemoCustomerMemoAddPO {
    /**  */
    private String userId;

    /** 고객코드 */
    private String custCd;

    /**  */
    private String cmplRqstDt;

    /**  */
    private int seqn;

    /**  */
    private String memo;


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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    @Override
    public String toString() {
        return "NemoCustomerMemoAddPO{" +
                "userId='" + userId + '\'' +
                ", custCd='" + custCd + '\'' +
                ", cmplRqstDt='" + cmplRqstDt + '\'' +
                ", seqn='" + seqn + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
