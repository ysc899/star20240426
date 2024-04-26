package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * 일일 방문계획 추가 병원 목록 PO
 */
public class NemoScheduleAddListPO {
    /** 고객 코드 */
    private String custCd;

    /** 고객명 */
    private String custNm;

    /**  */
    private String prjtCd;

    public NemoScheduleAddListPO(String custCd , String custNm, String prjtCd)
    {
        this.custCd = custCd;
        this.custNm = custNm;
        this.prjtCd = prjtCd;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getPrjtCd() {
        return prjtCd;
    }

    public void setPrjtCd(String prjtCd) {
        this.prjtCd = prjtCd;
    }

    @Override
    public String toString() {
        return "NemoScheduleAddListPO{" +
                "custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", prjtCd='" + prjtCd + '\'' +
                '}';
    }
}
