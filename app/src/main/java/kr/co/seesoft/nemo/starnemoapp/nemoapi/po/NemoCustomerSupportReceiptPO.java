package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객지원 접수정보 조회 PO
 */
public class NemoCustomerSupportReceiptPO {
    /** 접수번호 */
    private String recpNo;

    /** 접수일 */
    private String recpDt;


    public String getRecpNo() {
        return recpNo;
    }

    public void setRecpNo(String recpNo) {
        this.recpNo = recpNo;
    }

    public String getRecpDt() {
        return recpDt;
    }

    public void setRecpDt(String recpDt) {
        this.recpDt = recpDt;
    }


    @Override
    public String toString() {
        return "NemoCustomerSupportReceiptPO{" +
                "recpNo='" + recpNo + '\'' +
                ", recpDt='" + recpDt + '\'' +
                '}';
    }
}
