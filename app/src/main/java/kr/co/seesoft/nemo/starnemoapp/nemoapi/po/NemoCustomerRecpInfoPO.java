package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객 접수 정보 조회 PO
 */
public class NemoCustomerRecpInfoPO {
    /** 조회일자 */
    private String smplTakePlanDt;

    /** 고객코드 */
    private String custCd;

    /** 수거담당자사원번호 */
    private String takchgrEmpNo;


    public String getSmplTakePlanDt() {
        return smplTakePlanDt;
    }

    public void setSmplTakePlanDt(String smplTakePlanDt) {
        this.smplTakePlanDt = smplTakePlanDt;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getTakchgrEmpNo() {
        return takchgrEmpNo;
    }

    public void setTakchgrEmpNo(String takchgrEmpNo) {
        this.takchgrEmpNo = takchgrEmpNo;
    }


    @Override
    public String toString() {
        return "NemoCustomerRecpInfoPO{" +
                "smplTakePlanDt='" + smplTakePlanDt + '\'' +
                "custCd='" + custCd + '\'' +
                "takchgrEmpNo='" + takchgrEmpNo + '\'' +
                '}';
    }
}
