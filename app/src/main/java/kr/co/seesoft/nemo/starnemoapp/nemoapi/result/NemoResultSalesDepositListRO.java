package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;


public class NemoResultSalesDepositListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoSalesDepositListRO> result;


    public ArrayList<NemoSalesDepositListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoSalesDepositListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultSalesDepositListRO{" +
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
