package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * RND 고객 정보 조회 PO
 */
public class NemoCustomerInfoRndPO {
    /**  */
    private String rndTakePlanUkeyid;


    public String getRndTakePlanUkeyid() {
        return rndTakePlanUkeyid;
    }

    public void setRndTakePlanUkeyid(String rndTakePlanUkeyid) {
        this.rndTakePlanUkeyid = rndTakePlanUkeyid;
    }


    @Override
    public String toString() {
        return "NemoCustomerInfoRndPO{" +
                "rndTakePlanUkeyid='" + rndTakePlanUkeyid + '\'' +
                '}';
    }
}
