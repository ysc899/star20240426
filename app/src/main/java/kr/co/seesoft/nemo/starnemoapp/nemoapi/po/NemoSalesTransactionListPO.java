package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 접수 정보 조회 PO
 */
public class NemoSalesTransactionListPO {

    /** 고객코드 */
    private String custCd;

    /** 조회일자 Start */
    private String jobYmFrom;

    /** 조회일자 End */
    private String jobYmTo;


    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getJobYmFrom() {
        return jobYmFrom;
    }

    public void setJobYmFrom(String jobYmFrom) {
        this.jobYmFrom = jobYmFrom;
    }

    public String getJobYmTo() {
        return jobYmTo;
    }

    public void setJobYmTo(String jobYmTo) {
        this.jobYmTo = jobYmTo;
    }


    @Override
    public String toString() {
        return "NemoCustomerRecpInfoPO{" +
                "custCd='" + custCd + '\'' +
                ",jobYmFrom='" + jobYmFrom + '\'' +
                ",jobYmTo='" + jobYmTo + '\'' +
                '}';
    }
}
