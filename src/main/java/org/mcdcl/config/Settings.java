package org.mcdcl.config;

import java.io.Serializable;

/**
 * 启动器设置类，用于存储用户配置的启动器设置
 */
public class Settings implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String javaPath;
    private int maxMemory;
    private String jvmArgs;
    private String gameArgs;
    private String theme;
    
    public Settings() {
        // 默认值
        this.javaPath = "";
        this.maxMemory = 4;
        this.jvmArgs = "";
        this.gameArgs = "";
        this.theme = "默认主题";
    }
    
    public String getJavaPath() {
        return javaPath;
    }
    
    public void setJavaPath(String javaPath) {
        this.javaPath = javaPath;
    }
    
    public int getMaxMemory() {
        return maxMemory;
    }
    
    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }
    
    public String getJvmArgs() {
        return jvmArgs;
    }
    
    public void setJvmArgs(String jvmArgs) {
        this.jvmArgs = jvmArgs;
    }
    
    public String getGameArgs() {
        return gameArgs;
    }
    
    public void setGameArgs(String gameArgs) {
        this.gameArgs = gameArgs;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
}