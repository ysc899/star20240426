package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


/**
 * VOC 조회 PO
 */
public class NemoCustomerVOCCountPO {
    /** 부서코드 */
    private String deptCd;

    /** 조회일 */
    private String vocCfmDtm;


    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    public String getVocCfmDtm() {
        return vocCfmDtm;
    }

    public void setVocCfmDtm(String vocCfmDtm) {
        this.vocCfmDtm = vocCfmDtm;
    }


    @Override
    public String toString() {
        return "NemoCustomerVOCCountPO{" +
                "deptCd='" + deptCd + '\'' +
                ", vocCfmDtm='" + vocCfmDtm + '\'' +
                '}';
    }
}
