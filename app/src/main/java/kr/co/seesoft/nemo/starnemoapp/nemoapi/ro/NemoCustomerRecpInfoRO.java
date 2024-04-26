package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NemoCustomerRecpInfoRO implements Serializable {

    /** 순번 */
    public String seqn;

    /** 고객코드 */
    public String custCd;

    /** 고객명 */
    public String custNm;

    /**  */
    public String ocs;

    /** 방문 구문 */
    public String takchgrDiv;

    /** 방문 시간 */
    public String visitTime;

    /** 이미지 접수 일자 */
    public String imgTrnsDtm;

    /** 이미지 접수 건수 */
    public String imgCnt;

    /** 수거 건수 */
    public String recpCnt;

    /** 담당자 아이디 */
    public String chgrId;

    /** 담당자명 */
    public String chgrNm;


    public String getSeqn() {
        return seqn;
    }

    public void setSeqn(String seqn) {
        this.seqn = seqn;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getOcs() {
        return ocs;
    }

    public void setOcs(String ocs) {
        this.ocs = ocs;
    }

    public String getTakchgrDiv() {
        return takchgrDiv;
    }

    public void setTakchgrDiv(String takchgrDiv) {
        this.takchgrDiv = takchgrDiv;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getImgTrnsDtm() {
        return imgTrnsDtm;
    }

    public void setImgTrnsDtm(String imgTrnsDtm) {
        this.imgTrnsDtm = imgTrnsDtm;
    }

    public String getImgCnt() {
        return imgCnt;
    }

    public void setImgCnt(String imgCnt) {
        this.imgCnt = imgCnt;
    }

    public String getRecpCnt() {
        return recpCnt;
    }

    public void setRecpCnt(String recpCnt) {
        this.recpCnt = recpCnt;
    }

    public String getChgrId() {
        return chgrId;
    }

    public void setChgrId(String chgrId) {
        this.chgrId = chgrId;
    }

    public String getChgrNm() {
        return chgrNm;
    }

    public void setChgrNm(String chgrNm) {
        this.chgrNm = chgrNm;
    }


    @Override
    public String toString() {
        return "NemoCustomerRecpInfoRO{" +
                "seqn='" + seqn + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", ocs='" + ocs + '\'' +
                ", takchgrDiv='" + takchgrDiv + '\'' +
                ", visitTime='" + visitTime + '\'' +
                ", imgTrnsDtm='" + imgTrnsDtm + '\'' +
                ", imgCnt='" + imgCnt + '\'' +
                ", recpCnt='" + recpCnt + '\'' +
                ", chgrId='" + chgrId + '\'' +
                ", chgrNm='" + chgrNm + '\'' +
                '}';
    }
}
