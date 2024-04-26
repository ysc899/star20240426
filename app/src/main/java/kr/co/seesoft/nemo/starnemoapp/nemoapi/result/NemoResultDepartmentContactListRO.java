package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoBagSendListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDepartmentContactListRO;


public class NemoResultDepartmentContactListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoDepartmentContactListRO> result;


    public ArrayList<NemoDepartmentContactListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoDepartmentContactListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultDepartmentContactListRO{" +
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
