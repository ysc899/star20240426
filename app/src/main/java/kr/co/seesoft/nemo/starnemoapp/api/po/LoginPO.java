package kr.co.seesoft.nemo.starnemoapp.api.po;


/**
 * 로그인용 PO
 */
public class LoginPO {
    /** id */
    private String UserId;
    /** 패스워드 */
    private String Password;
    /** 병원코드? */
    private String InstCd;
    /** 시큐어키 추가되어야함 */
//    private String secure;


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getInstCd() {
        return InstCd;
    }

    public void setInstCd(String instCd) {
        InstCd = instCd;
    }

    @Override
    public String toString() {
        return "LoginPO{" +
                "UserId='" + UserId + '\'' +
                ", Password='" + Password + '\'' +
                ", InstCd='" + InstCd + '\'' +
                '}';
    }
}
