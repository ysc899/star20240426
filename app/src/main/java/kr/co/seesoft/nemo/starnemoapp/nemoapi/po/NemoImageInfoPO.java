package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoImageInfoPO {

    private String hospitalCode;
    private String ymd;

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    @Override
    public String toString() {
        return "NemoImageInfoPO{" +
                "hospitalCode='" + hospitalCode + '\'' +
                ", ymd='" + ymd + '\'' +
                '}';
    }
}
