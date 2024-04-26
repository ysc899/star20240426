package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoLoginRO implements Serializable {

    /** id */
    @SerializedName("userId")
    private String userId;

    /** 담당자명 */
    @SerializedName("userNm")
    private String userNm;

    /** 부서코드 */
    @SerializedName("deptCd")
    private String deptCd;

    /** 부서명 */
    @SerializedName("deptNm")
    private String deptNm;

    /** 상위부서코드 */
    @SerializedName("upprDeptCd")
    private String upprDeptCd;

    /** 상위부서명  */

    @SerializedName("fullDeptNm")
    private String fullDeptNm;

    @SerializedName("ver")
    private String ver;

    @SerializedName("delImgDt")
    private String delImgDt;

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

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }

    public String getUpprDeptCd() {
        return upprDeptCd;
    }

    public void setUpprDeptCd(String upprDeptCd) {
        this.upprDeptCd = upprDeptCd;
    }

    public String getFullDeptNm() {
        return fullDeptNm;
    }

    public void setFullDeptNm(String fullDeptNm) {
        this.fullDeptNm = fullDeptNm;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDelImgDt() {
        return delImgDt;
    }

    public void setDelImgDt(String delImgDt) {
        this.delImgDt = delImgDt;
    }

    @Override
    public String toString() {
        return "NemoLoginRO{" +
                "userId=" + userId +
                ", userNm='" + userNm + '\'' +
                ", deptCd='" + deptCd + '\'' +
                ", deptNm='" + deptNm + '\'' +
                ", upprDeptCd='" + upprDeptCd + '\'' +
                ", fullDeptNm='" + fullDeptNm + '\'' +
                ", ver='" + ver + '\'' +
                ", delImgDt='" + delImgDt + '\'' +
                '}';
    }
}
