package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoImageAddRO implements Serializable {

    private boolean resultFlag;

    private long seq;

    private boolean lastFlag;

    private String serverFileName;

    public NemoImageAddRO(boolean resultFlag, long seq, boolean lastFlag, String serverFileName) {
        this.resultFlag = resultFlag;
        this.seq = seq;
        this.lastFlag = lastFlag;
        this.serverFileName = serverFileName;
    }

    public boolean isResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(boolean resultFlag) {
        this.resultFlag = resultFlag;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public boolean isLastFlag() {
        return lastFlag;
    }

    public void setLastFlag(boolean lastFlag) {
        this.lastFlag = lastFlag;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    @Override
    public String toString() {
        return "NemoImageAddRO{" +
                "resultFlag=" + resultFlag +
                ", seq=" + seq +
                ", lastFlag=" + lastFlag +
                ", serverFileName='" + serverFileName + '\'' +
                '}';
    }
}
