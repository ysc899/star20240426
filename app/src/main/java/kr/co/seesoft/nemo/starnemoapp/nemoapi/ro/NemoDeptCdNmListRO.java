package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoDeptCdNmListRO implements Serializable {

    /**  */
    public String deptCd;
    /**  */
    public String deptNm;


    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }



    @Override
    public String toString() {
        return "NemoDeptCdNmListRO{" +
                "deptCd='" + deptCd + '\'' +
                ", deptNm='" + deptNm + '\'' +
                '}';
    }
}

