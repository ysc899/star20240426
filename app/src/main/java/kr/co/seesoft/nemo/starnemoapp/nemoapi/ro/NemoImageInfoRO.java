package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoImageInfoRO implements Serializable {

    /** mainCount */
    private int mainCount = 0;
    /** subCount */
    private int subCount;

    public int getMainCount() {
        return mainCount;
    }

    public void setMainCount(int mainCount) {
        this.mainCount = mainCount;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    @Override
    public String toString() {
        return "NemoImageInfoRO{" +
                "mainCount=" + mainCount +
                ", subCount=" + subCount +
                '}';
    }
}
