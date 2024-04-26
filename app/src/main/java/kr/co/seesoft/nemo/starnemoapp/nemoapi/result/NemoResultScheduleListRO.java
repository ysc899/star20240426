package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;


public class NemoResultScheduleListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoScheduleListRO> result;


    public ArrayList<NemoScheduleListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoScheduleListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultScheduleListRO{" +
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
