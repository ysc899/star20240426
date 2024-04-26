package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;


import java.util.ArrayList;

/**
 * 일일 방문계획 순서 Update PO
 */
public class NemoScheduleOrderUpdatePO {
    /** 수거계획일자 */
    private String smplTakePlanDt;

    /** 수거담당자사원번호 */
    private String takchgrEmpNo;

    /** 순번 */
    private int seqn;

    /** RND Key */
    private String rndTakePlanUkeyid;

    public NemoScheduleOrderUpdatePO(String smplTakePlanDt , String takchgrEmpNo , int seqn , String rndTakePlanUkeyid)
    {
        this.smplTakePlanDt = smplTakePlanDt;
        this.takchgrEmpNo = takchgrEmpNo;
        this.seqn = seqn;
        this.rndTakePlanUkeyid = rndTakePlanUkeyid;
    }

    public String getSmplTakePlanDt() {
        return smplTakePlanDt;
    }

    public void setSmplTakePlanDt(String smplTakePlanDt) {
        this.smplTakePlanDt = smplTakePlanDt;
    }

    public String getTakchgrEmpNo() {
        return takchgrEmpNo;
    }

    public void setTakchgrEmpNo(String takchgrEmpNo) {
        this.takchgrEmpNo = takchgrEmpNo;
    }

    public int getSeqn() {
        return seqn;
    }

    public void setSeqn(int seqn) {
        this.seqn = seqn;
    }

    public String getRndTakePlanUkeyid() {
        return rndTakePlanUkeyid;
    }

    public void setRndTakePlanUkeyid(String rndTakePlanUkeyid) {
        this.rndTakePlanUkeyid = rndTakePlanUkeyid;
    }



    @Override
    public String toString() {
        return "NemoScheduleOrderUpdatePO{" +
                "smplTakePlanDt='" + smplTakePlanDt + '\'' +
                ", takchgrEmpNo='" + takchgrEmpNo + '\'' +
                ", seqn='" + seqn + '\'' +
                ", rndTakePlanUkeyid='" + rndTakePlanUkeyid + '\'' +
                '}';
    }
}
