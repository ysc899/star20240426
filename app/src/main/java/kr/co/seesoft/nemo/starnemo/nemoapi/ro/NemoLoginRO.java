package kr.co.seesoft.nemo.starnemo.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoLoginRO implements Serializable {

    @SerializedName("delImageDay")
    private int delImageDay;
    @SerializedName("ver")
    private String ver;
    @SerializedName("dpt")
    private String dpt;

    public int getDelImageDay() {
        return delImageDay;
    }

    public void setDelImageDay(int delImageDay) {
        this.delImageDay = delImageDay;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDpt() {
        return dpt;
    }

    public void setDpt(String dpt) {
        this.dpt = dpt;
    }

    @Override
    public String toString() {
        return "NemoLoginRO{" +
                "delImageDay=" + delImageDay +
                ", ver='" + ver + '\'' +
                ", dpt='" + dpt + '\'' +
                '}';
    }
}
