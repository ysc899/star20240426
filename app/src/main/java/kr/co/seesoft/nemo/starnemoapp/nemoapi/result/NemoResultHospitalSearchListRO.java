package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;


public class NemoResultHospitalSearchListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoHospitalSearchRO> result;


    public ArrayList<NemoHospitalSearchRO> getResult() { return result; }

    public void setResult(ArrayList<NemoHospitalSearchRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultHospitalSearchListRO{" +
                "locale=" + super.getLocale() +
                ", timestamp='" + super.getTimestamp() + '\'' +
                ", httpStatus='" + super.getHttpStatus() + '\'' +
                ", messageId='" + super.getMessageId() + '\'' +
                ", messageTitle='" + super.getMessageTitle() + '\'' +
                ", messageContent='" + super.getMessageContent() + '\'' +
                ", path='" + super.getPath() + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
