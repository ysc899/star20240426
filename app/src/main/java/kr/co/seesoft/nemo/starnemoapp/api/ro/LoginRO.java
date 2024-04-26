package kr.co.seesoft.nemo.starnemoapp.api.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LoginRO implements Serializable {


    @SerializedName("IsLoginSuccess")
    private boolean isLoginSuccess;
    @SerializedName("Info")
    private List<UserInfo> infos;

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public List<UserInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<UserInfo> infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        return "LoginRO{" +
                "isLoginSuccess=" + isLoginSuccess +
                ", infos=" + infos +
                '}';
    }

    public class UserInfo implements Serializable{


        @SerializedName("InstCd")
        private String instCd;
        @SerializedName("BranCd")
        private String branCd;
        @SerializedName("HosClntCd")
        private String hosClntCd;
        @SerializedName("UserId")
        private String userId;
        @SerializedName("UserNm")
        private String userNm;
        @SerializedName("Password")
        private String password;
        @SerializedName("DeptCd")
        private String deptCd;
        @SerializedName("DeptKorNm")
        private String deptKorNm;
        @SerializedName("DeptEngNm")
        private String deptEngNm;
        @SerializedName("DeptLoc")
        private Object deptLoc;
        @SerializedName("HosNo")
        private String hosNo;
        @SerializedName("HosNm")
        private String hosNm;
        @SerializedName("PosDeptCd")
        private String posDeptCd;
        @SerializedName("DeptEngAbbr")
        private Object deptEngAbbr;
        @SerializedName("MenuGrCd")
        private String menuGrCd;
        @SerializedName("MenuGrNm")
        private String menuGrNm;
        @SerializedName("HosIpAddr")
        private String hosIpAddr;
        @SerializedName("IpAdress")
        private String ipAdress;
        @SerializedName("ReqDeptNo")
        private String reqDeptNo;


        public String getInstCd() {
            return instCd;
        }

        public void setInstCd(String instCd) {
            this.instCd = instCd;
        }

        public String getBranCd() {
            return branCd;
        }

        public void setBranCd(String branCd) {
            this.branCd = branCd;
        }

        public String getHosClntCd() {
            return hosClntCd;
        }

        public void setHosClntCd(String hosClntCd) {
            this.hosClntCd = hosClntCd;
        }

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDeptCd() {
            return deptCd;
        }

        public void setDeptCd(String deptCd) {
            this.deptCd = deptCd;
        }

        public String getDeptKorNm() {
            return deptKorNm;
        }

        public void setDeptKorNm(String deptKorNm) {
            this.deptKorNm = deptKorNm;
        }

        public String getDeptEngNm() {
            return deptEngNm;
        }

        public void setDeptEngNm(String deptEngNm) {
            this.deptEngNm = deptEngNm;
        }

        public Object getDeptLoc() {
            return deptLoc;
        }

        public void setDeptLoc(Object deptLoc) {
            this.deptLoc = deptLoc;
        }

        public String getHosNo() {
            return hosNo;
        }

        public void setHosNo(String hosNo) {
            this.hosNo = hosNo;
        }

        public String getHosNm() {
            return hosNm;
        }

        public void setHosNm(String hosNm) {
            this.hosNm = hosNm;
        }

        public String getPosDeptCd() {
            return posDeptCd;
        }

        public void setPosDeptCd(String posDeptCd) {
            this.posDeptCd = posDeptCd;
        }

        public Object getDeptEngAbbr() {
            return deptEngAbbr;
        }

        public void setDeptEngAbbr(Object deptEngAbbr) {
            this.deptEngAbbr = deptEngAbbr;
        }

        public String getMenuGrCd() {
            return menuGrCd;
        }

        public void setMenuGrCd(String menuGrCd) {
            this.menuGrCd = menuGrCd;
        }

        public String getMenuGrNm() {
            return menuGrNm;
        }

        public void setMenuGrNm(String menuGrNm) {
            this.menuGrNm = menuGrNm;
        }

        public String getHosIpAddr() {
            return hosIpAddr;
        }

        public void setHosIpAddr(String hosIpAddr) {
            this.hosIpAddr = hosIpAddr;
        }

        public String getIpAdress() {
            return ipAdress;
        }

        public void setIpAdress(String ipAdress) {
            this.ipAdress = ipAdress;
        }

        public String getReqDeptNo() {
            return reqDeptNo;
        }

        public void setReqDeptNo(String reqDeptNo) {
            this.reqDeptNo = reqDeptNo;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "instCd='" + instCd + '\'' +
                    ", branCd='" + branCd + '\'' +
                    ", hosClntCd='" + hosClntCd + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userNm='" + userNm + '\'' +
                    ", password='" + password + '\'' +
                    ", deptCd='" + deptCd + '\'' +
                    ", deptKorNm='" + deptKorNm + '\'' +
                    ", deptEngNm='" + deptEngNm + '\'' +
                    ", deptLoc=" + deptLoc +
                    ", hosNo='" + hosNo + '\'' +
                    ", hosNm='" + hosNm + '\'' +
                    ", posDeptCd='" + posDeptCd + '\'' +
                    ", deptEngAbbr=" + deptEngAbbr +
                    ", menuGrCd='" + menuGrCd + '\'' +
                    ", menuGrNm='" + menuGrNm + '\'' +
                    ", hosIpAddr='" + hosIpAddr + '\'' +
                    ", ipAdress='" + ipAdress + '\'' +
                    ", reqDeptNo='" + reqDeptNo + '\'' +
                    '}';
        }
    }
}
