package kr.co.seesoft.nemo.starnemoapp.api.ro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * 병원 환자 정보용
 */
public class HospitalRegisterRO implements Comparable<HospitalRegisterRO>, Serializable {

    @Expose(serialize = false)
    public int order;

    @Expose(serialize = false)
    public String registerId;
    @Expose(serialize = false)
    public String patientName;
    @Expose(serialize = false)
    public String specimenList;
    @Expose(serialize = false)
    public String barcode;
    @Expose(serialize = false)
    public boolean scanFlag = false;


    @SerializedName("InstCd")
    private String InstCd;

    @SerializedName("BranCd")
    private String BranCd;

    @SerializedName("HosClntCd")
    private String HosClntCd;

    @SerializedName("FINSCOR")
    private String FINSCOR;

    @SerializedName("FINNSDAT")
    private String FINSDAT;

    @SerializedName("FINSSEQ")
    private String FINSSEQ;

    @SerializedName("FINSGCD")
    private String FINSGCD;

    @SerializedName("FINSGST")
    private String FINSGST;

    @SerializedName("Seq")
    private String Seq;

    @SerializedName("UserId")
    private String UserId;

    @SerializedName("SpcNo")
    private String SpcNo;

    @SerializedName("F010FKN")
    private String F010FKN;

    @SerializedName("FINSSMP")
    private String FINSSMP;

    @SerializedName("FWRKCRT")
    private String FWRKCRT;

    @SerializedName("FWRKNAM")
    private String FWRKNAM;

    @SerializedName("FWRKBDT")
    private String FWRKBDT;

    @SerializedName("AGE")
    private String AGE;

    @SerializedName("FWRKSEX")
    private String FWRKSEX;

    @SerializedName("FWRKCBC")
    private String FWRKCBC;

    @SerializedName("FWRKJKN")
    private String FWRKJKN;

    @SerializedName("FWRKDOC")
    private String FWRKDOC;



    public HospitalRegisterRO(int order, String registerId, String patientName, String specimenList, String barcode) {
        this.order = order;
        this.registerId = registerId;
        this.patientName = patientName;
        this.specimenList = specimenList;
        this.barcode = barcode;
    }
    public HospitalRegisterRO(int order, String registerId, String patientName, String specimenList, String barcode, SelectmClisMasterRO.MClisInfo info, String userId) {
        this.order = order;
        this.registerId = registerId;
        this.patientName = patientName;
        this.specimenList = specimenList;
        this.barcode = barcode;

        this.InstCd = info.getInstCd();
        this.BranCd = info.getBranCd();
        this.HosClntCd = info.getHosClntCd();
        this.FINSCOR = info.getFINSCOR();
        this.FINSDAT = info.getFINSDAT();
        this.FINSSEQ = info.getFINSSEQ();
        this.FINSGCD = info.getFINSGCD();
        this.FINSGST = info.getFINSGST();
        this.Seq = info.getSeq();
        this.UserId = userId;
        this.SpcNo = info.getSpcNo();
        this.F010FKN = info.getF010FKN();
        this.FINSSMP = info.getFINSSMP();
        this.FWRKCRT = info.getFwrkcrt();
        this.FWRKNAM = info.getFWRKNAM();
        this.FWRKBDT = info.getFWRKBDT();
        this.AGE = info.getAge();
        this.FWRKSEX = info.getFWRKSEX();
        this.FWRKCBC = info.getFwrkcbc();
        this.FWRKJKN = info.getFwrkjkn();
        this.FWRKDOC = info.getFwrkdoc();

    }

    public HospitalRegisterRO(int order, String barcode){
        this.order = order;
        this.registerId = StringUtils.substring(barcode, 5, 10);
        this.barcode = barcode;
        this.scanFlag = true;
    }

    public void setSeq(String seq) {
        Seq = seq;
    }

    @Override
    public int compareTo(HospitalRegisterRO hospitalRegisterRO) {
        return Integer.compare(order, hospitalRegisterRO.order);
    }


    @Override
    public String toString() {
        return "HospitalRegisterRO{" +
                "order=" + order +
                ", registerId='" + registerId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", specimenList='" + specimenList + '\'' +
                ", barcode='" + barcode + '\'' +
                ", scanFlag=" + scanFlag +
                ", InstCd='" + InstCd + '\'' +
                ", BranCd='" + BranCd + '\'' +
                ", HosClntCd='" + HosClntCd + '\'' +
                ", FINSCOR='" + FINSCOR + '\'' +
                ", FINSDAT='" + FINSDAT + '\'' +
                ", FINSSEQ='" + FINSSEQ + '\'' +
                ", FINSGCD='" + FINSGCD + '\'' +
                ", FINSGST='" + FINSGST + '\'' +
                ", Seq='" + Seq + '\'' +
                ", UserId='" + UserId + '\'' +
                ", SpcNo='" + SpcNo + '\'' +
                ", F010FKN='" + F010FKN + '\'' +
                ", FINSSMP='" + FINSSMP + '\'' +
                ", FWRKCRT='" + FWRKCRT + '\'' +
                ", FWRKNAM='" + FWRKNAM + '\'' +
                ", FWRKBDT='" + FWRKBDT + '\'' +
                ", AGE='" + AGE + '\'' +
                ", FWRKSEX='" + FWRKSEX + '\'' +
                ", FWRKCBC='" + FWRKCBC + '\'' +
                ", FWRKJKN='" + FWRKJKN + '\'' +
                ", FWRKDOC='" + FWRKDOC + '\'' +
                '}';
    }
}
