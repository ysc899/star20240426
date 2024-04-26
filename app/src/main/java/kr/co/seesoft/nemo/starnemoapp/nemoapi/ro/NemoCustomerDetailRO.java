package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class NemoCustomerDetailRO implements Serializable {

    /** 고객코드 */
    @SerializedName("custCd")
    private String custCd;

    /** 고객명 */
    @SerializedName("custNm")
    private String custNm;

    /** 대표전화번호 */
    @SerializedName("custTelno")
    private String custTelno;

    /** Email */
    @SerializedName("custEmalAddr")
    private String custEmalAddr;

    /** 고객상태코드 */
    @SerializedName("cstatCd")
    private String cstatCd;

    /** 고객상태명 */
    @SerializedName("cstatNm")
    private String cstatNm;

    /**  */
    @SerializedName("careInstNo")
    private String careInstNo;

    /** 전화번호 */
    @SerializedName("bizrNo")
    private String bizrNo;

    /** 주소 */
    @SerializedName("addr")
    private String addr;

    /**  */
    @SerializedName("pmntRttnDcnt")
    private String pmntRttnDcnt;

    /**  */
    @SerializedName("pmntPragDd")
    private String pmntPragDd;

    /** 담당자 아이디 */
    @SerializedName("chgrId")
    private String chgrId;

    /** 담당자명 */
    @SerializedName("chgrNm")
    private String chgrNm;

    /** 연동구분(O:OCS연동,U:유비랩,E:엑셀,H:,수기) */
    @SerializedName("itrnCd")
    private String itrnCd;

    private ArrayList<NemoCustomerDetailListRO> cmjprsnList;

    private ArrayList<NemoCustomerDetailNoticeListRO> salsUisuList;


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

    public String getCustTelno() {
        return custTelno;
    }

    public void setCustTelno(String custTelno) {
        this.custTelno = custTelno;
    }

    public String getCustEmalAddr() {
        return custEmalAddr;
    }

    public void setCustEmalAddr(String custEmalAddr) {
        this.custEmalAddr = custEmalAddr;
    }

    public String getCstatCd() {
        return cstatCd;
    }

    public void setCstatCd(String cstatCd) {
        this.cstatCd = cstatCd;
    }

    public String getCstatNm() {
        return cstatNm;
    }

    public void setCstatNm(String cstatNm) {
        this.cstatNm = cstatNm;
    }

    public String getCareInstNo() {
        return careInstNo;
    }

    public void setCareInstNo(String careInstNo) {
        this.careInstNo = careInstNo;
    }

    public String getBizrNo() {
        return bizrNo;
    }

    public void setBizrNo(String bizrNo) {
        this.bizrNo = bizrNo;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPmntRttnDcnt() {
        return pmntRttnDcnt;
    }

    public void setPmntRttnDcnt(String pmntRttnDcnt) {
        this.pmntRttnDcnt = pmntRttnDcnt;
    }

    public String getPmntPragDd() {
        return pmntPragDd;
    }

    public void setPmntPragDd(String pmntPragDd) {
        this.pmntPragDd = pmntPragDd;
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

    public String getItrnCd() {
        return itrnCd;
    }

    public void setItrnCd(String itrnCd) {
        this.itrnCd = itrnCd;
    }

    public ArrayList<NemoCustomerDetailListRO> getCmjprsnList() {
        return cmjprsnList;
    }

    public void setCmjprsnList(ArrayList<NemoCustomerDetailListRO> cmjprsnList) {
        this.cmjprsnList = cmjprsnList;
    }

    public ArrayList<NemoCustomerDetailNoticeListRO> getSalsUisuList() {
        return salsUisuList;
    }

    public void setSalsUisuList(ArrayList<NemoCustomerDetailNoticeListRO> salsUisuList) {
        this.salsUisuList = salsUisuList;
    }


    @Override
    public String toString() {
        return "NemoCustomerDetailRO{" +
                "custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                ", custTelno='" + custTelno + '\'' +
                ", custEmalAddr='" + custEmalAddr + '\'' +
                ", cstatCd='" + cstatCd + '\'' +
                ", cstatNm='" + cstatNm + '\'' +
                ", careInstNo='" + careInstNo + '\'' +
                ", bizrNo='" + bizrNo + '\'' +
                ", addr='" + addr + '\'' +
                ", pmntRttnDcnt='" + pmntRttnDcnt + '\'' +
                ", pmntPragDd='" + pmntPragDd + '\'' +
                ", chgrId='" + chgrId + '\'' +
                ", chgrNm='" + chgrNm + '\'' +
                ", itrnCd='" + itrnCd + '\'' +
                ", cmjprsnList='" + cmjprsnList + '\'' +
                ", salsUisuList='" + salsUisuList + '\'' +
                '}';
    }
}
