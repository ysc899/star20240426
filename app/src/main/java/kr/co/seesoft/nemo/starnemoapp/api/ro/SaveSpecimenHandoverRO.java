package kr.co.seesoft.nemo.starnemoapp.api.ro;

import com.google.gson.annotations.SerializedName;


public class SaveSpecimenHandoverRO {
    @SerializedName("returnValue")
    private int returnValue;

    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return "SaveSpecimenHandoverRO{" +
                "returnValue=" + returnValue +
                '}';
    }
}
