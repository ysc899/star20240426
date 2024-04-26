package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;


public class NemoResultCustomerMemoListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoCustomerMemoListRO> result;


    public ArrayList<NemoCustomerMemoListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoCustomerMemoListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerMemoListRO{" +
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
