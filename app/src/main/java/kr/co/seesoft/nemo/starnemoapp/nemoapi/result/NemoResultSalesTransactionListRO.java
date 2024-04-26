package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;


public class NemoResultSalesTransactionListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoSalesTransactionListRO> result;


    public ArrayList<NemoSalesTransactionListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoSalesTransactionListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultSalesTransactionListRO{" +
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
