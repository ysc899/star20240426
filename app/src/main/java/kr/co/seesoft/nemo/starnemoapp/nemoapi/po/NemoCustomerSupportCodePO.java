package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 고객지원요청 코드 조회 PO
 */
public class NemoCustomerSupportCodePO {
    /** 분류코드 */
    private String ctgrId;


    public String getCtgrId() {
        return ctgrId;
    }

    public void setCtgrId(String ctgrId) {
        this.ctgrId = ctgrId;
    }


    @Override
    public String toString() {
        return "NemoCustomerSupportCodePO{" +
                "ctgrId='" + ctgrId + '\'' +
                '}';
    }
}
