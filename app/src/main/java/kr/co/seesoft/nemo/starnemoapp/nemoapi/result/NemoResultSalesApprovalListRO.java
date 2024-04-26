package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesApprovalListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;


public class NemoResultSalesApprovalListRO extends NemoResultRO {

    @SerializedName("result")
    private ArrayList<NemoSalesApprovalListRO> result;


    public ArrayList<NemoSalesApprovalListRO> getResult() { return result; }

    public void setResult(ArrayList<NemoSalesApprovalListRO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NemoResultSalesApprovalListRO{" +
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
