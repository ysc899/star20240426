package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;


public class NemoResultDeptCdNmListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoDeptCdNmListRO> result;


    public ArrayList<NemoDeptCdNmListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoDeptCdNmListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultDeptCdNmListRO{" +
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
