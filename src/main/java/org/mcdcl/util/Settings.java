package org.mcdcl.util;

public class Settings {
    private String javaPath;
    private int maxMemory;
    private String jvmArgs;
    private String gameArgs;
    private String theme;
    private String minecraftPath;
    private String backgroundImage;

    public Settings() {
        this.maxMemory = 2;
        this.theme = "默认主题";
        this.jvmArgs = "";
        this.gameArgs = "";
    }

    // Getters and Setters
    public String getJavaPath() { return javaPath; }
    public void setJavaPath(String javaPath) { this.javaPath = javaPath; }
    
    public int getMaxMemory() { return maxMemory; }
    public void setMaxMemory(int maxMemory) { this.maxMemory = maxMemory; }
    
    public String getJvmArgs() { return jvmArgs; }
    public void setJvmArgs(String jvmArgs) { this.jvmArgs = jvmArgs; }
    
    public String getGameArgs() { return gameArgs; }
    public void setGameArgs(String gameArgs) { this.gameArgs = gameArgs; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public String getMinecraftPath() { return minecraftPath; }
    public void setMinecraftPath(String minecraftPath) { this.minecraftPath = minecraftPath; }
    
    public String getBackgroundImage() {
        return backgroundImage;
    }
    
    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}