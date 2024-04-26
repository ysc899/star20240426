package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerVOCListRO;


public class NemoResultCustomerSupportListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoCustomerSupportListRO> result;


    public ArrayList<NemoCustomerSupportListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoCustomerSupportListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerSupportListRO{" +
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
