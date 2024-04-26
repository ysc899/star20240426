package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


import java.util.ArrayList;

/**
 * 청구서 발송 PO
 */
public class NemoSalesBillSendPO {
    /** id */
    private String userId;

    /** 고객코드 */
    private String custCd;

    /** 발송 월 */
    private String jobYm;

    /** 발송 구분 */
    private String docDivCd;

    /** Email */
    private String custEmalAddr;


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

    public String getJobYm() {
        return jobYm;
    }

    public void setJobYm(String jobYm) {
        this.jobYm = jobYm;
    }

    public String getDocDivCd() {
        return docDivCd;
    }

    public void setDocDivCd(String docDivCd) {
        this.docDivCd = docDivCd;
    }

    public String getCustEmalAddr() {
        return custEmalAddr;
    }

    public void setCustEmalAddr(String custEmalAddr) {
        this.custEmalAddr = custEmalAddr;
    }

    @Override
    public String toString() {
        return "NemoSalesBillSendPO{" +
                "userId='" + userId + '\'' +
                ", custCd='" + custCd + '\'' +
                ", jobYm='" + jobYm + '\'' +
                ", docDivCd='" + docDivCd + '\'' +
                ", custEmalAddr='" + custEmalAddr + '\'' +
                '}';
    }
}
