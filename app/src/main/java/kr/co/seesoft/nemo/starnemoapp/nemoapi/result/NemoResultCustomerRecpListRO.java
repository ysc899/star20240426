package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpListRO;


public class NemoResultCustomerRecpListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoCustomerRecpListRO> result;


    public ArrayList<NemoCustomerRecpListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoCustomerRecpListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerRecpListRO{" +
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
