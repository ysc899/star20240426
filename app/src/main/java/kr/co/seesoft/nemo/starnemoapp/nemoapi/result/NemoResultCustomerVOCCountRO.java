package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerVOCListRO;


public class NemoResultCustomerVOCCountRO extends NemoResultRO {

    @SerializedName("result")
    private int result;


    public int getResult() { return result; }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultCustomerVOCCountRO{" +
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
