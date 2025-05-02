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
    private String backgroundImage;
    private String theme; // light, dark, custom
    private String minecraftPath;
    private String selectedVersion; // 添加用于存储所选版本的字段

    public Settings() {
        // 默认值
        this.javaPath = "";
        this.maxMemory = 4;
        this.jvmArgs = "";
        this.gameArgs = "";
        this.theme = "默认主题";
        this.minecraftPath = System.getProperty("user.home") + "/.minecraft";
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
    
    public String getBackgroundImage() {
        return backgroundImage;
    }
    
    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
    
    public String getTheme() {
        return theme != null ? theme : "dark";
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public String getMinecraftPath() {
        return minecraftPath;
    }
    
    public void setMinecraftPath(String minecraftPath) {
        this.minecraftPath = minecraftPath;
    }

    // 添加 setSelectedVersion 方法
    public void setSelectedVersion(String selectedVersion) {
        this.selectedVersion = selectedVersion;
    }

    public String getSelectedVersion() {
        return selectedVersion; // 修改 getSelectedVersion 方法以返回存储的值
    }
}