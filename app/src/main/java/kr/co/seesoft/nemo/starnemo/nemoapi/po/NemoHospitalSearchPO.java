package kr.co.seesoft.nemo.starnemo.nemoapi.po;


/**
 * 병원 검색용 PO
 */
public class NemoHospitalSearchPO {
    /** id */
    private String userId;
    
    /** 구분 
     *  1 : 내 담당 병원만 조회
     *  2 : 지점 모든 병원 조회
     * */
    private String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NemoHospitalSearchPO{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
