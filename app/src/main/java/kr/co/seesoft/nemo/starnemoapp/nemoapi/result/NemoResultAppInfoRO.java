package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoAppInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;


public class NemoResultAppInfoRO extends NemoResultRO {

    @SerializedName("result")
    private NemoAppInfoRO result;


    public NemoAppInfoRO getResult() { return result; }

    public void setResult(NemoAppInfoRO result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoAppInfoRO{" +
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
