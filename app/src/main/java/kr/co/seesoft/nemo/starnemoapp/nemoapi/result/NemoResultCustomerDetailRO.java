package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerDetailRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;


public class NemoResultCustomerDetailRO extends NemoResultRO {

    @SerializedName("result")
    private NemoCustomerDetailRO result;


    public NemoCustomerDetailRO getResult() { return result; }

    public void setResult(NemoCustomerDetailRO result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerDetailRO{" +
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
