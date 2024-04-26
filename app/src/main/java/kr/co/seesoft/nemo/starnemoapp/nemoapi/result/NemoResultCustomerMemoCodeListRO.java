package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;


public class NemoResultCustomerMemoCodeListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoCustomerMemoCodeListRO> result;


    public ArrayList<NemoCustomerMemoCodeListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoCustomerMemoCodeListRO> result) {
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
