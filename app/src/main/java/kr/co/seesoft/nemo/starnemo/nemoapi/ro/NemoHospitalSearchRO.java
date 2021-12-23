package kr.co.seesoft.nemo.starnemo.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoHospitalSearchRO implements Serializable {

    /** 담당자 명 */
    @SerializedName("saleManName")
    private String saleManName;

    /** 병원 담당자 사번번 */
    @SerializedName("salemanCode")
    private String salemanCode;

    /** 병원 이름 */
    @SerializedName("hospitalName")
    private String hospitalName;

    /** 병원 전화번호 */
    @SerializedName("hospitalTel")
    private String hospitalTel;

    /** 병원 전화번호 */
    @SerializedName("hospitalAdr")
    private String hospitalAdr;

    /** 병원 코드 */
    @SerializedName("hospitalCode")
    private String hospitalCode;

    public String getSaleManName() {
        return saleManName;
    }

    public void setSaleManName(String saleManName) {
        this.saleManName = saleManName;
    }

    public String getSalemanCode() {
        return salemanCode;
    }

    public void setSalemanCode(String salemanCode) {
        this.salemanCode = salemanCode;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalTel() {
        return hospitalTel;
    }

    public void setHospitalTel(String hospitalTel) {
        this.hospitalTel = hospitalTel;
    }

    public String getHospitalAdr() {
        return hospitalAdr;
    }

    public void setHospitalAdr(String hospitalAdr) {
        this.hospitalAdr = hospitalAdr;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    @Override
    public String toString() {
        return "NemoHospitalSearchRO{" +
                "saleManName='" + saleManName + '\'' +
                ", salemanCode='" + salemanCode + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalTel='" + hospitalTel + '\'' +
                ", hospitalAdr='" + hospitalAdr + '\'' +
                ", hospitalCode='" + hospitalCode + '\'' +
                '}';
    }
}
