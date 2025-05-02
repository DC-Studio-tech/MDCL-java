package org.mcdcl.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CrashReport {
    private String title;
    private LocalDateTime timestamp;
    private String errorType;
    private String errorMessage;
    private List<String> stackTrace;
    private String gameVersion;
    private String modList;
    private String systemInfo;
    
    public CrashReport() {
        this.stackTrace = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public List<String> getStackTrace() {
        return stackTrace;
    }
    
    public void setStackTrace(List<String> stackTrace) {
        this.stackTrace = stackTrace;
    }
    
    public void addStackTraceLine(String line) {
        this.stackTrace.add(line);
    }
    
    public String getGameVersion() {
        return gameVersion;
    }
    
    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }
    
    public String getModList() {
        return modList;
    }
    
    public void setModList(String modList) {
        this.modList = modList;
    }
    
    public String getSystemInfo() {
        return systemInfo;
    }
    
    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }
}