package kr.co.seesoft.nemo.starnemoapp.api.po;

public class SelectmClisMasterPO {

    /** 기관코드 [씨젠 01] */
    private String InstCd;
    /** 지점코드 테스트로 0001 로 설정 */
    private String BranCd;
    /** 거래처병원코드 : 36246 */
    private String HosClntCd;
    /** 검체접수일 : 20211026 */
    private String BsDd;
    /** 검체 접수 방법 구분 [S: 씨차트 R: 의뢰서 E: 엑셀] */
    private String AcceptType;


    public String getInstCd() {
        return InstCd;
    }

    public void setInstCd(String instCd) {
        InstCd = instCd;
    }

    public String getBranCd() {
        return BranCd;
    }

    public void setBranCd(String branCd) {
        BranCd = branCd;
    }

    public String getHosClntCd() {
        return HosClntCd;
    }

    public void setHosClntCd(String hosClntCd) {
        HosClntCd = hosClntCd;
    }

    public String getBsDd() {
        return BsDd;
    }

    public void setBsDd(String bsDd) {
        BsDd = bsDd;
    }

    public String getAcceptType() {
        return AcceptType;
    }

    public void setAcceptType(String acceptType) {
        AcceptType = acceptType;
    }

    @Override
    public String toString() {
        return "SelectmClisMasterPO{" +
                "InstCd='" + InstCd + '\'' +
                ", BranCd='" + BranCd + '\'' +
                ", HosClntCd='" + HosClntCd + '\'' +
                ", BsDd='" + BsDd + '\'' +
                ", AcceptType='" + AcceptType + '\'' +
                '}';
    }
}
