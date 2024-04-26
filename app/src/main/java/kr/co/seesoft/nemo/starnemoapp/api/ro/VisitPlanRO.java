package kr.co.seesoft.nemo.starnemoapp.api.ro;

import java.io.Serializable;

/**
 * 방문 계획용
 */
public class VisitPlanRO implements Comparable<VisitPlanRO>, Serializable {


    public int order;

    public String hospitalCode;

    public String hospitalName;

    public int count;

    public VisitPlanRO(int order, String hospitalCode, String hospitalName, int count) {
        this.order = order;
        this.hospitalCode = hospitalCode;
        this.hospitalName = hospitalName;
        this.count = count;
    }

    @Override
    public int compareTo(VisitPlanRO visitPlanRO) {
        return Integer.compare(order, visitPlanRO.order);
    }

    @Override
    public String toString() {
        return "VisitPlanRO{" +
                "order=" + order +
                ", hospitalCode='" + hospitalCode + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", count=" + count +
                '}';
    }


}
