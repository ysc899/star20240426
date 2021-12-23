package kr.co.seesoft.nemo.starnemo.api.ro;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 병원 환자 정보용
 */
public class HospitalRegisterRO implements Comparable<HospitalRegisterRO>, Serializable {


    public int order;

    public String registerId;

    public String patientName;

//    public ArrayList<String> specimenList;
    public String specimenList;

    public String barcode;

    public boolean scanFlag = false;

////    public HospitalRegisterRO(int order, String registerId, String patientName, List<String> specimenList, String barcode) {
//    public HospitalRegisterRO(int order, String registerId, String patientName, String specimenList, String barcode) {
//        this.order = order;
//        this.registerId = registerId;
//        this.patientName = patientName;
////        this.specimenList = new ArrayList<>();
////        this.specimenList.addAll(specimenList);
//
//        this.specimenList = specimenList;
//        this.barcode = barcode;
//    }

//    public HospitalRegisterRO(int order, String registerId, String patientName, ArrayList<String> specimenList, String barcode) {
    public HospitalRegisterRO(int order, String registerId, String patientName, String specimenList, String barcode) {
        this.order = order;
        this.registerId = registerId;
        this.patientName = patientName;
        this.specimenList = specimenList;
        this.barcode = barcode;
    }

    public HospitalRegisterRO(int order, String barcode){
        this.order = order;
        this.registerId = StringUtils.substring(barcode, 5, 10);
        this.barcode = barcode;
        this.scanFlag = true;
    }

    @Override
    public int compareTo(HospitalRegisterRO hospitalRegisterRO) {
        return Integer.compare(order, hospitalRegisterRO.order);
    }



}
