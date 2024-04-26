package kr.co.seesoft.nemo.starnemoapp.api.po;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.api.ro.HospitalRegisterRO;

public class SaveSpecimenHandoverPO {
    @SerializedName("UserSignImage")
    private String UserSignImage;

    @SerializedName("HosSignImage")
    private String HosSignImage;

    @SerializedName("Temperature")
    private String Temperature;

    @SerializedName("SST")
    private String SST;

    @SerializedName("EDTA")
    private String EDTA;

    @SerializedName("Urine")
    private String Urine;

    @SerializedName("Tissue")
    private String Tissue;

    @SerializedName("Other")
    private String Other;

    @SerializedName("HoComment")
    private String HoComment;

    @SerializedName("SaveSpecimenHandovers")
    private List<HospitalRegisterRO> SaveSpecimenHandovers;

    public SaveSpecimenHandoverPO() {
    }

    public SaveSpecimenHandoverPO(String userSignImage, String hosSignImage, String temperature, String SST, String EDTA, String urine, String tissue, String other, String hoComment, List<HospitalRegisterRO> saveSpecimenHandovers) {
        UserSignImage = userSignImage;
        HosSignImage = hosSignImage;
        Temperature = temperature;
        this.SST = SST;
        this.EDTA = EDTA;
        Urine = urine;
        Tissue = tissue;
        Other = other;
        HoComment = hoComment;
        SaveSpecimenHandovers = saveSpecimenHandovers;
    }

    public String getUserSignImage() {
        return UserSignImage;
    }

    public void setUserSignImage(String userSignImage) {
        UserSignImage = userSignImage;
    }

    public String getHosSignImage() {
        return HosSignImage;
    }

    public void setHosSignImage(String hosSignImage) {
        HosSignImage = hosSignImage;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getSST() {
        return SST;
    }

    public void setSST(String SST) {
        this.SST = SST;
    }

    public String getEDTA() {
        return EDTA;
    }

    public void setEDTA(String EDTA) {
        this.EDTA = EDTA;
    }

    public String getUrine() {
        return Urine;
    }

    public void setUrine(String urine) {
        Urine = urine;
    }

    public String getTissue() {
        return Tissue;
    }

    public void setTissue(String tissue) {
        Tissue = tissue;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getHoComment() {
        return HoComment;
    }

    public void setHoComment(String hoComment) {
        HoComment = hoComment;
    }

    public List<HospitalRegisterRO> getSaveSpecimenHandovers() {
        return SaveSpecimenHandovers;
    }

    public void setSaveSpecimenHandovers(List<HospitalRegisterRO> saveSpecimenHandovers) {
        SaveSpecimenHandovers = saveSpecimenHandovers;
    }

    @Override
    public String toString() {
        return "SaveSpecimenHandoverPO{" +
                "UserSignImage='" + UserSignImage + '\'' +
                ", HosSignImage='" + HosSignImage + '\'' +
                ", Temperature='" + Temperature + '\'' +
                ", SST='" + SST + '\'' +
                ", EDTA='" + EDTA + '\'' +
                ", Urine='" + Urine + '\'' +
                ", Tissue='" + Tissue + '\'' +
                ", Other='" + Other + '\'' +
                ", HoComment='" + HoComment + '\'' +
                ", SaveSpecimenHandovers=" + SaveSpecimenHandovers +
                '}';
    }
}
