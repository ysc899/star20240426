package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객지원요청 등록 , 수정 , 삭제 PO
 */
public class NemoCustomerSupportPO {
    /**  */
    private String userId;

    /** 접수일 */
    private String recpDt;

    /** 접수번호 */
    private String recpNo;

    /**  */
    private String recpSno;

    /** 요청구분 */
    private String custSuptUpdtRqstTypeCd;

    /** 고객요청 */
    private String custRqstIssu;

    /**  */
    private String custSuptPrcsUserId;

    /**  */
    private String custSuptPrcsDtm;

    /** 요청상태 */
    private String custSuptUpdtRqstStatCd;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecpDt() {
        return recpDt;
    }

    public void setRecpDt(String recpDt) {
        this.recpDt = recpDt;
    }

    public String getRecpNo() {
        return recpNo;
    }

    public void setRecpNo(String recpNo) {
        this.recpNo = recpNo;
    }

    public String getRecpSno() {
        return recpSno;
    }

    public void setRecpSno(String recpSno) {
        this.recpSno = recpSno;
    }

    public String getCustSuptUpdtRqstTypeCd() {
        return custSuptUpdtRqstTypeCd;
    }

    public void setCustSuptUpdtRqstTypeCd(String custSuptUpdtRqstTypeCd) {
        this.custSuptUpdtRqstTypeCd = custSuptUpdtRqstTypeCd;
    }

    public String getCustRqstIssu() {
        return custRqstIssu;
    }

    public void setCustRqstIssu(String custRqstIssu) {
        this.custRqstIssu = custRqstIssu;
    }

    public String getCustSuptPrcsUserId() {
        return custSuptPrcsUserId;
    }

    public void setCustSuptPrcsUserId(String custSuptPrcsUserId) {
        this.custSuptPrcsUserId = custSuptPrcsUserId;
    }

    public String getCustSuptPrcsDtm() {
        return custSuptPrcsDtm;
    }

    public void setCustSuptPrcsDtm(String custSuptPrcsDtm) {
        this.custSuptPrcsDtm = custSuptPrcsDtm;
    }

    public String getCustSuptUpdtRqstStatCd() {
        return custSuptUpdtRqstStatCd;
    }

    public void setCustSuptUpdtRqstStatCd(String custSuptUpdtRqstStatCd) {
        this.custSuptUpdtRqstStatCd = custSuptUpdtRqstStatCd;
    }


    @Override
    public String toString() {
        return "NemoCustomerSupportPO{" +
                "userId='" + userId + '\'' +
                ", recpDt='" + recpDt + '\'' +
                ", recpNo='" + recpNo + '\'' +
                ", recpSno='" + recpSno + '\'' +
                ", custSuptUpdtRqstTypeCd='" + custSuptUpdtRqstTypeCd + '\'' +
                ", custRqstIssu='" + custRqstIssu + '\'' +
                ", custSuptPrcsUserId='" + custSuptPrcsUserId + '\'' +
                ", custSuptPrcsDtm='" + custSuptPrcsDtm + '\'' +
                ", custSuptUpdtRqstStatCd='" + custSuptUpdtRqstStatCd + '\'' +
                '}';
    }
}
