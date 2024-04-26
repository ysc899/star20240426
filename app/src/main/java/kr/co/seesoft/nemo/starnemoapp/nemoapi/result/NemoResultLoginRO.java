package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoLoginRO;


public class NemoResultLoginRO extends NemoResultRO {

    @SerializedName("result")
    private NemoLoginRO result;


    public NemoLoginRO getResult() { return result; }

    public void setResult(NemoLoginRO result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultLoginRO{" +
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
