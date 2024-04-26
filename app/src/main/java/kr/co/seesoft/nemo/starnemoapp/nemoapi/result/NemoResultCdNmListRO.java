package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;


public class NemoResultCdNmListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoCdNmListRO> result;


    public ArrayList<NemoCdNmListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoCdNmListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCdNmListRO{" +
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
