package kr.co.seesoft.nemo.starnemoapp.api.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SelectmClisMasterRO {

    @SerializedName("mClisLists")
    private List<MClisInfo> mClisLists;

    public List<MClisInfo> getmClisLists() {
        return mClisLists;
    }

    public void setmClisLists(List<MClisInfo> mClisLists) {
        this.mClisLists = mClisLists;
    }

    @Override
    public String toString() {
        return "SelectmClisMasterRO{" +
                "mClisLists=" + mClisLists +
                '}';
    }

    public class MClisInfo implements Serializable {


        @SerializedName("InstCd")
        private String instCd;
        @SerializedName("BranCd")
        private String branCd;
        @SerializedName("HosClntCd")
        private String hosClntCd;
        @SerializedName("Seq")
        private String Seq;
        @SerializedName("FINSCOR")
        private String FINSCOR;
        @SerializedName("FINSDAT")
        private String FINSDAT;
        @SerializedName("FINSSEQ")
        private String FINSSEQ;
        @SerializedName("FINSGCD")
        private String FINSGCD;
        @SerializedName("F010FKN")
        private String F010FKN;
        @SerializedName("FINSSMP")
        private String FINSSMP;
        @SerializedName("FINSTVL")
        private String FINSTVL;
        @SerializedName("FINSSDT")
        private String FINSSDT;
        @SerializedName("FINSSTM")
        private String FINSSTM;
        @SerializedName("FINSINS")
        private String FINSINS;
        @SerializedName("FINSSND")
        private String FINSSND;
        @SerializedName("FINSSNT")
        private String FINSSNT;
        @SerializedName("FINSSTS")
        private String FINSSTS;
        @SerializedName("FINSGST")
        private String FINSGST;
        @SerializedName("FINSFL1")
        private String FINSFL1;
        @SerializedName("FINSFL2")
        private String FINSFL2;
        @SerializedName("FINSFL3")
        private String FINSFL3;
        @SerializedName("FWRKJN")
        private String FWRKJN;
        @SerializedName("FWRKNAM")
        private String FWRKNAM;
        @SerializedName("FWRKSEX")
        private String FWRKSEX;
        @SerializedName("FWRKBDT")
        private String FWRKBDT;
        @SerializedName("FWRKBDC")
        private String FWRKBDC;
        @SerializedName("FWRKAGE")
        private String FWRKAGE;
        @SerializedName("FWRKADR")
        private String FWRKADR;
        @SerializedName("FWRKHGT")
        private String FWRKHGT;
        @SerializedName("FWRKWGT")
        private String FWRKWGT;
        @SerializedName("FWRKNAT")
        private String FWRKNAT;
        @SerializedName("SpecimenTrack")
        private String specimenTrack;
        @SerializedName("Barcode")

        private String barcode;
        @SerializedName("FWRKCRT")
        private String fwrkcrt;
        @SerializedName("AGE")
        private String age;
        @SerializedName("FWRKCBC")
        private String fwrkcbc;
        @SerializedName("FWRKJKN")
        private String fwrkjkn;
        @SerializedName("FWRKDOC")
        private String fwrkdoc;
        @SerializedName("SpcNo")
        private String spcNo;

        @SerializedName("Cmt")
        private String Cmt;



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

        public String getSeq() {
            return Seq;
        }

        public void setSeq(String seq) {
            Seq = seq;
        }

        public String getFINSCOR() {
            return FINSCOR;
        }

        public void setFINSCOR(String FINSCOR) {
            this.FINSCOR = FINSCOR;
        }

        public String getFINSDAT() {
            return FINSDAT;
        }

        public void setFINSDAT(String FINSDAT) {
            this.FINSDAT = FINSDAT;
        }

        public String getFINSSEQ() {
            return FINSSEQ;
        }

        public void setFINSSEQ(String FINSSEQ) {
            this.FINSSEQ = FINSSEQ;
        }

        public String getFINSGCD() {
            return FINSGCD;
        }

        public void setFINSGCD(String FINSGCD) {
            this.FINSGCD = FINSGCD;
        }

        public String getF010FKN() {
            return F010FKN;
        }

        public void setF010FKN(String f010FKN) {
            F010FKN = f010FKN;
        }

        public String getFINSSMP() {
            return FINSSMP;
        }

        public void setFINSSMP(String FINSSMP) {
            this.FINSSMP = FINSSMP;
        }

        public String getFINSTVL() {
            return FINSTVL;
        }

        public void setFINSTVL(String FINSTVL) {
            this.FINSTVL = FINSTVL;
        }

        public String getFINSSDT() {
            return FINSSDT;
        }

        public void setFINSSDT(String FINSSDT) {
            this.FINSSDT = FINSSDT;
        }

        public String getFINSSTM() {
            return FINSSTM;
        }

        public void setFINSSTM(String FINSSTM) {
            this.FINSSTM = FINSSTM;
        }

        public String getFINSINS() {
            return FINSINS;
        }

        public void setFINSINS(String FINSINS) {
            this.FINSINS = FINSINS;
        }

        public String getFINSSND() {
            return FINSSND;
        }

        public void setFINSSND(String FINSSND) {
            this.FINSSND = FINSSND;
        }

        public String getFINSSNT() {
            return FINSSNT;
        }

        public void setFINSSNT(String FINSSNT) {
            this.FINSSNT = FINSSNT;
        }

        public String getFINSSTS() {
            return FINSSTS;
        }

        public void setFINSSTS(String FINSSTS) {
            this.FINSSTS = FINSSTS;
        }

        public String getFINSGST() {
            return FINSGST;
        }

        public void setFINSGST(String FINSGST) {
            this.FINSGST = FINSGST;
        }

        public String getFINSFL1() {
            return FINSFL1;
        }

        public void setFINSFL1(String FINSFL1) {
            this.FINSFL1 = FINSFL1;
        }

        public String getFINSFL2() {
            return FINSFL2;
        }

        public void setFINSFL2(String FINSFL2) {
            this.FINSFL2 = FINSFL2;
        }

        public String getFINSFL3() {
            return FINSFL3;
        }

        public void setFINSFL3(String FINSFL3) {
            this.FINSFL3 = FINSFL3;
        }

        public String getFWRKJN() {
            return FWRKJN;
        }

        public void setFWRKJN(String FWRKJN) {
            this.FWRKJN = FWRKJN;
        }

        public String getFWRKNAM() {
            return FWRKNAM;
        }

        public void setFWRKNAM(String FWRKNAM) {
            this.FWRKNAM = FWRKNAM;
        }

        public String getFWRKSEX() {
            return FWRKSEX;
        }

        public void setFWRKSEX(String FWRKSEX) {
            this.FWRKSEX = FWRKSEX;
        }

        public String getFWRKBDT() {
            return FWRKBDT;
        }

        public void setFWRKBDT(String FWRKBDT) {
            this.FWRKBDT = FWRKBDT;
        }

        public String getFWRKBDC() {
            return FWRKBDC;
        }

        public void setFWRKBDC(String FWRKBDC) {
            this.FWRKBDC = FWRKBDC;
        }

        public String getFWRKAGE() {
            return FWRKAGE;
        }

        public void setFWRKAGE(String FWRKAGE) {
            this.FWRKAGE = FWRKAGE;
        }

        public String getFWRKADR() {
            return FWRKADR;
        }

        public void setFWRKADR(String FWRKADR) {
            this.FWRKADR = FWRKADR;
        }

        public String getFWRKHGT() {
            return FWRKHGT;
        }

        public void setFWRKHGT(String FWRKHGT) {
            this.FWRKHGT = FWRKHGT;
        }

        public String getFWRKWGT() {
            return FWRKWGT;
        }

        public void setFWRKWGT(String FWRKWGT) {
            this.FWRKWGT = FWRKWGT;
        }

        public String getFWRKNAT() {
            return FWRKNAT;
        }

        public void setFWRKNAT(String FWRKNAT) {
            this.FWRKNAT = FWRKNAT;
        }

        public String getSpecimenTrack() {
            return specimenTrack;
        }

        public void setSpecimenTrack(String specimenTrack) {
            this.specimenTrack = specimenTrack;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getFwrkcrt() {
            return fwrkcrt;
        }

        public void setFwrkcrt(String fwrkcrt) {
            this.fwrkcrt = fwrkcrt;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getFwrkcbc() {
            return fwrkcbc;
        }

        public void setFwrkcbc(String fwrkcbc) {
            this.fwrkcbc = fwrkcbc;
        }

        public String getFwrkjkn() {
            return fwrkjkn;
        }

        public void setFwrkjkn(String fwrkjkn) {
            this.fwrkjkn = fwrkjkn;
        }

        public String getFwrkdoc() {
            return fwrkdoc;
        }

        public void setFwrkdoc(String fwrkdoc) {
            this.fwrkdoc = fwrkdoc;
        }

        public String getSpcNo() {
            return spcNo;
        }

        public void setSpcNo(String spcNo) {
            this.spcNo = spcNo;
        }

        public String getCmt() {
            return Cmt;
        }

        public void setCmt(String cmt) {
            Cmt = cmt;
        }

        @Override
        public String toString() {
            return "MClisInfo{" +
                    "instCd='" + instCd + '\'' +
                    ", branCd='" + branCd + '\'' +
                    ", hosClntCd='" + hosClntCd + '\'' +
                    ", Seq='" + Seq + '\'' +
                    ", FINSCOR='" + FINSCOR + '\'' +
                    ", FINSDAT='" + FINSDAT + '\'' +
                    ", FINSSEQ='" + FINSSEQ + '\'' +
                    ", FINSGCD='" + FINSGCD + '\'' +
                    ", F010FKN='" + F010FKN + '\'' +
                    ", FINSSMP='" + FINSSMP + '\'' +
                    ", FINSTVL='" + FINSTVL + '\'' +
                    ", FINSSDT='" + FINSSDT + '\'' +
                    ", FINSSTM='" + FINSSTM + '\'' +
                    ", FINSINS='" + FINSINS + '\'' +
                    ", FINSSND='" + FINSSND + '\'' +
                    ", FINSSNT='" + FINSSNT + '\'' +
                    ", FINSSTS='" + FINSSTS + '\'' +
                    ", FINSGST='" + FINSGST + '\'' +
                    ", FINSFL1='" + FINSFL1 + '\'' +
                    ", FINSFL2='" + FINSFL2 + '\'' +
                    ", FINSFL3='" + FINSFL3 + '\'' +
                    ", FWRKJN='" + FWRKJN + '\'' +
                    ", FWRKNAM='" + FWRKNAM + '\'' +
                    ", FWRKSEX='" + FWRKSEX + '\'' +
                    ", FWRKBDT='" + FWRKBDT + '\'' +
                    ", FWRKBDC='" + FWRKBDC + '\'' +
                    ", FWRKAGE='" + FWRKAGE + '\'' +
                    ", FWRKADR='" + FWRKADR + '\'' +
                    ", FWRKHGT='" + FWRKHGT + '\'' +
                    ", FWRKWGT='" + FWRKWGT + '\'' +
                    ", FWRKNAT='" + FWRKNAT + '\'' +
                    ", specimenTrack='" + specimenTrack + '\'' +
                    ", barcode='" + barcode + '\'' +
                    ", fwrkcrt='" + fwrkcrt + '\'' +
                    ", age='" + age + '\'' +
                    ", fwrkcbc='" + fwrkcbc + '\'' +
                    ", fwrkjkn='" + fwrkjkn + '\'' +
                    ", fwrkdoc='" + fwrkdoc + '\'' +
                    ", spcNo='" + spcNo + '\'' +
                    ", Cmt='" + Cmt + '\'' +
                    '}';
        }
    }
}
