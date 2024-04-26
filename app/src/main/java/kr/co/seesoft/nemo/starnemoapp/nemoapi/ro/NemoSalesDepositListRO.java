package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoSalesDepositListRO implements Serializable {

    /** 수금일자(ex. 20230928) */
    public String blclDt;
    /** 수금일시(ex. 2023-07-31 19:32) */
    public String regDtm;
    /** 수금액 */
    public long blclAmt;
    /** 수금방법(입금유형코드) */
    public String dpstTypeNm;
    /** 입금구분 CD */
    public String dpstTypeCd;
    /** 입금구분 */
    public String dpstDivNm;
    /** 승인번호 */
    public String crcdAprvNo;
    /** 카드명 */
    public String crcdCmpNm;
    /** 카드 No */
    public String crcdNo;
    /** 입금은행 */
    public String bankNm;
    /** 입금계좌 */
    public String bankAcctno;

    private int seqn;

    public String sumr;

    public String dpstDtm;

    public long dpstAmt;

    public long aprvAmt;

    public String insMm;

    public String aprvDtm;

    public String custCd;

    public String custNm;


    public String getBlclDt() {
        return blclDt;
    }

    public void setBlclDt(String blclDt) {
        this.blclDt = blclDt;
    }

    public String getRegDtm() {
        return regDtm;
    }

    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }

    public long getBlclAmt() {
        return blclAmt;
    }

    public void setBlclAmt(long blclAmt) {
        this.blclAmt = blclAmt;
    }

    public String getDpstTypeNm() {
        return dpstTypeNm;
    }

    public void setDpstTypeNm(String dpstTypeNm) {
        this.dpstTypeNm = dpstTypeNm;
    }

    public String getDpstTypeCd() {
        return dpstTypeCd;
    }

    public void setDpstTypeCd(String dpstTypeCd) {
        this.dpstTypeCd = dpstTypeCd;
    }

    public String getDpstDivNm() {
        return dpstDivNm;
    }

    public void setDpstDivNm(String dpstDivNm) {
        this.dpstDivNm = dpstDivNm;
    }

    public String getCrcdAprvNo() {
        return crcdAprvNo;
    }

    public void setCrcdAprvNo(String crcdAprvNo) {
        this.crcdAprvNo = crcdAprvNo;
    }

    public String getCrcdCmpNm() {
        return crcdCmpNm;
    }

    public void setCrcdCmpNm(String crcdCmpNm) {
        this.crcdCmpNm = crcdCmpNm;
    }

    public String getCrcdNo() {
        return crcdNo;
    }

    public void setCrcdNo(String crcdNo) {
        this.crcdNo = crcdNo;
    }

    public String getBankNm() {
        return bankNm;
    }

    public void setBankNm(String bankNm) {
        this.bankNm = bankNm;
    }

    public String getBankAcctno() {
        return bankAcctno;
    }

    public void setBankAcctno(String bankAcctno) {
        this.bankAcctno = bankAcctno;
    }

    public int getSeqn() {
        return seqn;
    }

    public void setSeqn(int seqn) {
        this.seqn = seqn;
    }

    public String getSumr() {
        return sumr;
    }

    public void setSumr(String sumr) {
        this.sumr = sumr;
    }

    public String getDpstDtm() {
        return dpstDtm;
    }

    public void setDpstDtm(String dpstDtm) {
        this.dpstDtm = dpstDtm;
    }

    public long getDpstAmt() {
        return dpstAmt;
    }

    public void setDpstAmt(long dpstAmt) {
        this.dpstAmt = dpstAmt;
    }

    public long getAprvAmt() {
        return aprvAmt;
    }

    public void setAprvAmt(long aprvAmt) {
        this.aprvAmt = aprvAmt;
    }

    public String getInsMm() {
        return insMm;
    }

    public void setInsMm(String insMm) {
        this.insMm = insMm;
    }

    public String getAprvDtm() {
        return aprvDtm;
    }

    public void setAprvDtm(String aprvDtm) {
        this.aprvDtm = aprvDtm;
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



    @Override
    public String toString() {
        return "NemoSalesDepositListRO{" +
                "blclDt='" + blclDt + '\'' +
                ", regDtm='" + regDtm + '\'' +
                ", blclAmt='" + blclAmt + '\'' +
                ", dpstTypeCd='" + dpstTypeCd + '\'' +
                ", dpstTypeNm='" + dpstTypeNm + '\'' +
                ", dpstDivNm='" + dpstDivNm + '\'' +
                ", crcdAprvNo='" + crcdAprvNo + '\'' +
                ", crcdCmpNm='" + crcdCmpNm + '\'' +
                ", crcdNo='" + crcdNo + '\'' +
                ", bankNm='" + bankNm + '\'' +
                ", bankAcctno='" + bankAcctno + '\'' +
                ", seqn='" + seqn + '\'' +
                ", sumr='" + sumr + '\'' +
                ", dpstDtm='" + dpstDtm + '\'' +
                ", dpstAmt='" + dpstAmt + '\'' +
                ", aprvAmt='" + aprvAmt + '\'' +
                ", insMm='" + insMm + '\'' +
                ", aprvDtm='" + aprvDtm + '\'' +
                ", custCd='" + custCd + '\'' +
                ", custNm='" + custNm + '\'' +
                '}';
    }
}

