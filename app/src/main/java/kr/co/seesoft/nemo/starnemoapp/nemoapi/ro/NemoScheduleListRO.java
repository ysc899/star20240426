package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoScheduleListRO implements Comparable<NemoScheduleListRO>, Serializable {

    /** 고객코드 */
    @SerializedName("custCd")
    private String custCd;

    /** 고객명 */
    @SerializedName("custNm")
    private String custNm;

    /** 수거계획일자 */
    @SerializedName("smplTakePlanDt")
    private String smplTakePlanDt;

    /** 순번 */
    @SerializedName("seqn")
    private int seqn;

    /** 수거담당자사원번호 */
    @SerializedName("takchgrEmpNo")
    private String takchgrEmpNo;

    /** 수거완료여부 */
    @SerializedName("takeCmplYn")
    private String takeCmplYn;

    /** 사용여부 */
    @SerializedName("useYn")
    private String useYn;

    /** 출력순서 - 앱에서 사용 */
    @SerializedName("order")
    private int order;

    /**  */
    @SerializedName("takchgrDivCd")
    private String takchgrDivCd;

    /**  */
    @SerializedName("lhqrCd")
    private String lhqrCd;

    /**  */
    @SerializedName("brncCd")
    private String brncCd;

    /**  */
    @SerializedName("takePragDtm")
    private String takePragDtm;

    /**  */
    @SerializedName("takeDtm")
    private String takeDtm;

    /**  */
    @SerializedName("rndYn")
    private String rndYn;

    /**  */
    @SerializedName("prjtCd")
    private String prjtCd;

    /**  */
    @SerializedName("rndTakePlanUkeyid")
    private String rndTakePlanUkeyid;

    /**  */
    @SerializedName("imgTrnsDtm")
    private String imgTrnsDtm;

    /**  */
    @SerializedName("imgCnt")
    private int imgCnt;

    /**  */
    @SerializedName("recpCnt")
    private int recpCnt;


    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getCustNm() { return custNm; }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getSmplTakePlanDt() {
        return smplTakePlanDt;
    }

    public void setSmplTakePlanDt(String smplTakePlanDt) {
        this.smplTakePlanDt = smplTakePlanDt;
    }

    public int getSeqn() {
        return seqn;
    }

    public void setSeqn(int seqn) {
        this.seqn = seqn;
    }

    public String getTakchgrEmpNo() {
        return takchgrEmpNo;
    }

    public void setTakchgrEmpNo(String takchgrEmpNo) {
        this.takchgrEmpNo = takchgrEmpNo;
    }

    public String getTakeCmplYn() {
        return takeCmplYn;
    }

    public void setTakeCmplYn(String takeCmplYn) {
        this.takeCmplYn = takeCmplYn;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTakchgrDivCd() {
        return takchgrDivCd;
    }

    public void setTakchgrDivCd(String takchgrDivCd) {
        this.takchgrDivCd = takchgrDivCd;
    }

    public String getLhqrCd() {
        return lhqrCd;
    }

    public void setLhqrCd(String lhqrCd) {
        this.lhqrCd = lhqrCd;
    }

    public String getBrncCd() {
        return brncCd;
    }

    public void setBrncCd(String brncCd) {
        this.brncCd = brncCd;
    }

    public String getTakePragDtm() {
        return takePragDtm;
    }

    public void setTakePragDtm(String takePragDtm) {
        this.takePragDtm = takePragDtm;
    }

    public String getTakeDtm() {
        return takeDtm;
    }

    public void setTakeDtm(String takeDtm) {
        this.takeDtm = takeDtm;
    }

    public String getRndYn() {
        return rndYn;
    }

    public void setRndYn(String rndYn) {
        this.rndYn = rndYn;
    }

    public String getPrjtCd() {
        return prjtCd;
    }

    public void setPrjtCd(String prjtCd) {
        this.prjtCd = prjtCd;
    }

    public String getRndTakePlanUkeyid() {
        return rndTakePlanUkeyid;
    }

    public void setRndTakePlanUkeyid(String rndTakePlanUkeyid) {
        this.rndTakePlanUkeyid = rndTakePlanUkeyid;
    }

    public String getImgTrnsDtm() {
        return imgTrnsDtm;
    }

    public void setImgTrnsDtm(String imgTrnsDtm) {
        this.imgTrnsDtm = imgTrnsDtm;
    }

    public int getImgCnt() {
        return imgCnt;
    }

    public void setImgCnt(int imgCnt) {
        this.imgCnt = imgCnt;
    }

    public int getRecpCnt() {
        return recpCnt;
    }

    public void setRecpCnt(int recpCnt) {
        this.recpCnt = recpCnt;
    }



    @Override
    public int compareTo(NemoScheduleListRO ro) {
        return Integer.compare(order, ro.order);
    }

    @Override
    public String toString() {
        return "NemoScheduleListRO{" +
                "custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", smplTakePlanDt='" + smplTakePlanDt + '\'' +
                ", seqn='" + seqn + '\'' +
                ", takchgrEmpNo='" + takchgrEmpNo + '\'' +
                ", takeCmplYn='" + takeCmplYn + '\'' +
                ", useYn='" + useYn + '\'' +
                ", order='" + order + '\'' +
                ", takchgrDivCd='" + takchgrDivCd + '\'' +
                ", lhqrCd='" + lhqrCd + '\'' +
                ", brncCd='" + brncCd + '\'' +
                ", takePragDtm='" + takePragDtm + '\'' +
                ", takeDtm='" + takeDtm + '\'' +
                ", rndYn='" + rndYn + '\'' +
                ", prjtCd='" + prjtCd + '\'' +
                ", rndTakePlanUkeyid='" + rndTakePlanUkeyid + '\'' +
                ", imgTrnsDtm='" + imgTrnsDtm + '\'' +
                ", imgCnt='" + imgCnt + '\'' +
                ", recpCnt='" + recpCnt + '\'' +
                '}';
    }
}
