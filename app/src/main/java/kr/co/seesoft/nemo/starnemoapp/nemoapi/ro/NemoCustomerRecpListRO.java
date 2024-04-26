package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;
import java.util.ArrayList;


public class NemoCustomerRecpListRO implements Serializable {

    public int order;

    /** 접수일 */
    public String recpDt;

    /** 접수번호 */
    public String recpNo;

    /** 환자명 */
    public String patnNm;

    /** 성별 */
    public String sex;

    /** 시험 의뢰 건수 */
    public String tstCnt;

    /** 의뢰 상세 목록 */
    public ArrayList<NemoCustomerRecpTestListRO> testCodeList;


    public int getOrder() { return order; }

    public void setOrder(int order) { this.order = order; }

    public String getRecpDt() {
        return recpDt;
    }

    public void setRecpDt(String recpDt) {
        this.recpDt = recpDt;
    }

    public String getRecpNo() {
        return recpNo;
    }

    public void setRecpNo(String recpNo) {
        this.recpNo = recpNo;
    }

    public String getPatnNm() {
        return patnNm;
    }

    public void setPatnNm(String patnNm) {
        this.patnNm = patnNm;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTstCnt() {
        return tstCnt;
    }

    public void setTstCnt(String tstCnt) {
        this.tstCnt = tstCnt;
    }

    public ArrayList<NemoCustomerRecpTestListRO> getTestCodeList() {
        return testCodeList;
    }

    public void setTestCodeList(ArrayList<NemoCustomerRecpTestListRO> testCodeList) {
        this.testCodeList = testCodeList;
    }


    @Override
    public String toString() {
        return "NemoCustomerRecpListRO{" +
                "recpDt='" + recpDt + '\'' +
                ", recpNo='" + recpNo + '\'' +
                ", patnNm='" + patnNm + '\'' +
                ", sex='" + sex + '\'' +
                ", tstCnt='" + tstCnt + '\'' +
                ", testCodeList='" + testCodeList + '\'' +
                '}';
    }
}

