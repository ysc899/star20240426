package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 메모 코드 조회 PO
 */
public class NemoCustomerMemoCodePO {
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
        return "NemoCustomerMemoCodePO{" +
                "ctgrId='" + ctgrId + '\'' +
                '}';
    }
}
