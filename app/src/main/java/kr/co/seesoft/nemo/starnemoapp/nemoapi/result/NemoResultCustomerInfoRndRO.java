package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRndRO;


public class NemoResultCustomerInfoRndRO extends NemoResultRO {

    @SerializedName("result")
    private NemoCustomerInfoRndRO result;


    public NemoCustomerInfoRndRO getResult() { return result; }

    public void setResult(NemoCustomerInfoRndRO result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerInfoRndRO{" +
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
