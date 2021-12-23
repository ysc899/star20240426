package kr.co.seesoft.nemo.starnemo.nemoapi.po;


/**
 * 로그인용 PO
 */
public class NemoLoginPO {
    /** id */
    private String userId;
    /** 패스워드 */
    private String pwd;

    /** 시큐어키 추가되어야함 */
    private String secureId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }

    @Override
    public String toString() {
        return "NemoLoginPO{" +
                "userId='" + userId + '\'' +
                ", pwd='" + pwd + '\'' +
                ", secureId='" + secureId + '\'' +
                '}';
    }
}
