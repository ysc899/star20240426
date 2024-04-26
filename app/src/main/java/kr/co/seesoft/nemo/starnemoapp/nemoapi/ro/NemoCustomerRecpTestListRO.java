package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoCustomerRecpTestListRO implements Serializable {

    /** 시험 번호 */
    public String tstCd;

    /** 시험 명 */
    public String tstNm;

    /** 샘플 코드 */
    public String smplCd;

    /** 샘플 명 */
    public String smplSclfNm;


    public String getTstCd() {
        return tstCd;
    }

    public void setTstCd(String tstCd) {
        this.tstCd = tstCd;
    }

    public String getTstNm() {
        return tstNm;
    }

    public void setTstNm(String tstNm) {
        this.tstNm = tstNm;
    }

    public String getSmplCd() {
        return smplCd;
    }

    public void setSmplCd(String smplCd) {
        this.smplCd = smplCd;
    }

    public String getSmplSclfNm() {
        return smplSclfNm;
    }

    public void setSmplSclfNm(String smplSclfNm) {
        this.smplSclfNm = smplSclfNm;
    }


    @Override
    public String toString() {
        return "NemoCustomerRecpTestListRO{" +
                "tstCd='" + tstCd + '\'' +
                ", tstNm='" + tstNm + '\'' +
                ", smplCd='" + smplCd + '\'' +
                ", smplSclfNm='" + smplSclfNm + '\'' +
                '}';
    }
}

