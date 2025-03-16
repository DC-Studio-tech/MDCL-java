package org.mcdcl.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 设置管理器类，用于保存和加载启动器设置
 */
public class SettingsManager {
    private static final String SETTINGS_FILE = "launcher_settings.dat";
    
    /**
     * 保存设置到文件
     * 
     * @param settings 要保存的设置对象
     * @throws IOException 如果保存过程中发生IO错误
     */
    public static void saveSettings(Settings settings) throws IOException {
        File settingsFile = new File(SETTINGS_FILE);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingsFile))) {
            oos.writeObject(settings);
        }
    }
    
    /**
     * 从文件加载设置
     * 
     * @return 加载的设置对象，如果文件不存在则返回默认设置
     * @throws IOException 如果加载过程中发生IO错误
     */
    public static Settings loadSettings() throws IOException {
        File settingsFile = new File(SETTINGS_FILE);
        if (!settingsFile.exists()) {
            return new Settings(); // 返回默认设置
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile))) {
            return (Settings) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("无法加载设置: " + e.getMessage(), e);
        }
    }
}