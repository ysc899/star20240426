package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpInfoRO;


public class NemoResultCustomerRecpInfoRO extends NemoResultRO {

    @SerializedName("result")
    private NemoCustomerRecpInfoRO result;


    public NemoCustomerRecpInfoRO getResult() { return result; }

    public void setResult(NemoCustomerRecpInfoRO result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerRecpInfoRO{" +
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
