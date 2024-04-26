package kr.co.seesoft.nemo.starnemoapp.nemoapi.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoLoginRO;


public class NemoResultRO implements Serializable {

    @SerializedName("locale")
    private String locale;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("httpStatus")
    private String httpStatus;
    @SerializedName("messageId")
    private String messageId;
    @SerializedName("messageTitle")
    private String messageTitle;
    @SerializedName("messageContent")
    private String messageContent;
    @SerializedName("path")
    private String path;
    @SerializedName("detail")
    private String detail;

    public String getLocale() {
        return locale;
    }

    public void setDelImageDay(String locale) {
        this.locale = locale;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDetail() { return detail; }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    @Override
    public String toString() {
        return "NemoResultRO{" +
                "locale=" + locale +
                ", timestamp='" + timestamp + '\'' +
                ", httpStatus='" + httpStatus + '\'' +
                ", messageId='" + messageId + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", path='" + path + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
