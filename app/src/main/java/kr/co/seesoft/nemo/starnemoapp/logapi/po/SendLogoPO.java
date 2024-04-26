package kr.co.seesoft.nemo.starnemoapp.logapi.po;

import java.io.File;

public class SendLogoPO {

    private String userId;

    private File logFile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }
}
