package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;
import java.util.ArrayList;


public class NemoSalesTransactionListRO implements Serializable {

    /**  */
    public String recpYm;
    /** 기준가 */
    public long sprcSellAmt;
    /** 실제가 */
    public long rprcSellAmt;
    /** 입금액(자금수입금액 + 일일수금금액) 이것이 수금금액과 같은건가 */
    public long dpstAmt;
    /**  */
    public String salePrft;
    /** 가산액 */
    public long adtnAmt;
    /** 차감액 */
    public long subAmt;
    /** 당미수(당월청구미수금액) */
    public long tmrqUcamt;
    /** 최종수금일 */
    public String blclDt;
    /** 수금금액 */
    public long blclAmt;
    /** 년월 */
    public String jobYm;
    /** 전미수(전월청구미수금액) */
    public long bfmmRqesUcamt;
    /** 비교검사(SeeLis 에서만 사용) */
    public long ctstAmt;
    /** 전자 서명 이미지 URL */
    public String sgntImgUrl;
    /** 당월청구 */
    public long tmrqSellAmt;

    /** 수금목표금액 */
    public long blclGoalAmt;


    public String getRecpYm() {
        return recpYm;
    }

    public void setRecpYm(String recpYm) {
        this.recpYm = recpYm;
    }

    public long getSprcSellAmt() {
        return sprcSellAmt;
    }

    public void setRecpNo(long sprcSellAmt) {
        this.sprcSellAmt = sprcSellAmt;
    }

    public long getRprcSellAmt() {
        return rprcSellAmt;
    }

    public void setRprcSellAmt(long rprcSellAmt) {
        this.rprcSellAmt = rprcSellAmt;
    }

    public long getDpstAmt() {
        return dpstAmt;
    }

    public void setDpstAmt(long dpstAmt) {
        this.dpstAmt = dpstAmt;
    }

    public String getSalePrft() {
        return salePrft;
    }

    public void setSalePrft(String salePrft) {
        this.salePrft = salePrft;
    }

    public long getAdtnAmt() {
        return adtnAmt;
    }

    public void setAdtnAmt(long adtnAmt) {
        this.adtnAmt = adtnAmt;
    }

    public long getSubAmt() {
        return subAmt;
    }

    public void setSubAmt(long subAmt) {
        this.subAmt = subAmt;
    }

    public long getTmrqUcamt() {
        return tmrqUcamt;
    }

    public void setTmrqUcamt(long tmrqUcamt) {
        this.tmrqUcamt = tmrqUcamt;
    }

    public String getBlclDt() {
        return blclDt;
    }

    public void setBlclDt(String blclDt) {
        this.blclDt = blclDt;
    }

    public long getBlclAmt() {
        return blclAmt;
    }

    public void setBlclAmt(long blclAmt) {
        this.blclAmt = blclAmt;
    }

    public String getJobYm() {
        return jobYm;
    }

    public void setJobYm(String jobYm) {
        this.jobYm = jobYm;
    }

    public long getBfmmRqesUcamt() {
        return bfmmRqesUcamt;
    }

    public void setBfmmRqesUcamt(long bfmmRqesUcamt) {
        this.bfmmRqesUcamt = bfmmRqesUcamt;
    }

    public long getCtstAmt() {
        return ctstAmt;
    }

    public void setCtstAmt(long ctstAmt) {
        this.ctstAmt = ctstAmt;
    }

    public String getSgntImgUrl() {
        return sgntImgUrl;
    }

    public void setSgntImgUrl(String sgntImgUrl) {
        this.sgntImgUrl = sgntImgUrl;
    }

    public long getTmrqSellAmt() {
        return tmrqSellAmt;
    }

    public void setTmrqSellAmt(long tmrqSellAmt) {
        this.tmrqSellAmt = tmrqSellAmt;
    }

    public long getBlclGoalAmt() {
        return blclGoalAmt;
    }

    public void setBlclGoalAmt(long blclGoalAmt) {
        this.blclGoalAmt = blclGoalAmt;
    }


    @Override
    public String toString() {
        return "NemoSalesTransactionListRO{" +
                "recpYm='" + recpYm + '\'' +
                ", sprcSellAmt='" + sprcSellAmt + '\'' +
                ", rprcSellAmt='" + rprcSellAmt + '\'' +
                ", dpstAmt='" + dpstAmt + '\'' +
                ", salePrft='" + salePrft + '\'' +
                ", adtnAmt='" + adtnAmt + '\'' +
                ", subAmt='" + subAmt + '\'' +
                ", tmrqUcamt='" + tmrqUcamt + '\'' +
                ", blclDt='" + blclDt + '\'' +
                ", blclAmt='" + blclAmt + '\'' +
                ", jobYm='" + jobYm + '\'' +
                ", bfmmRqesUcamt='" + bfmmRqesUcamt + '\'' +
                ", ctstAmt='" + ctstAmt + '\'' +
                ", sgntImgUrl='" + sgntImgUrl + '\'' +
                ", tmrqSellAmt='" + tmrqSellAmt + '\'' +
                '}';
    }
}

