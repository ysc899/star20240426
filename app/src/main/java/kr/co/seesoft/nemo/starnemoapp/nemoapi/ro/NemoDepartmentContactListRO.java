package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoDepartmentContactListRO implements Serializable {

    /**  */
    public String userId;
    /**  */
    public String userNm;
    /**  */
    public String deptCd;
    /**  */
    public String deptNm;
    /**  */
    public String bzMoblPhno;
    /**  */
    public String moblPhno;
    /**  */
    public String offmTelno;
    /**  */
    public String offmExtTelno;
    /**  */
    public String useYn;
    /**  */
    public String deptLvlCd;
    /** 직위 */
    public String ofpoCdNm;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) { this.deptCd = deptCd; }

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }

    public String getBzMoblPhno() {
        return bzMoblPhno;
    }

    public void setBzMoblPhno(String bzMoblPhno) {
        this.bzMoblPhno = bzMoblPhno;
    }

    public String getMoblPhno() {
        return moblPhno;
    }

    public void setMoblPhno(String moblPhno) {
        this.moblPhno = moblPhno;
    }

    public String getOffmTelno() {
        return offmTelno;
    }

    public void setOffmTelno(String offmTelno) {
        this.offmTelno = offmTelno;
    }

    public String getOffmExtTelno() {
        return offmExtTelno;
    }

    public void setOffmExtTelno(String offmExtTelno) {
        this.offmExtTelno = offmExtTelno;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) { this.useYn = useYn; }

    public String getDeptLvlCd() {
        return deptLvlCd;
    }

    public void setDeptLvlCd(String deptLvlCd) { this.deptLvlCd = deptLvlCd; }

    public String getOfpoCdNm() {
        return ofpoCdNm;
    }

    public void setOfpoCdNm(String ofpoCdNm) { this.ofpoCdNm = ofpoCdNm; }

    @Override
    public String toString() {
        return "NemoDepartmentContactListRO{" +
                "userId='" + userId + '\'' +
                ", userNm='" + userNm + '\'' +
                ", deptCd='" + deptCd + '\'' +
                ", deptNm='" + deptNm + '\'' +
                ", bzMoblPhno='" + bzMoblPhno + '\'' +
                ", moblPhno='" + moblPhno + '\'' +
                ", offmTelno='" + offmTelno + '\'' +
                ", offmExtTelno='" + offmExtTelno + '\'' +
                ", useYn='" + useYn + '\'' +
                ", deptLvlCd='" + deptLvlCd + '\'' +
                ", ofpoCdNm='" + ofpoCdNm + '\'' +
                '}';
    }
}

