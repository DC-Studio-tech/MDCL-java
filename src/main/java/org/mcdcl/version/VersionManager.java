package org.mcdcl.version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mcdcl.config.Settings;
import org.mcdcl.config.SettingsManager;
import org.mcdcl.exception.MinecraftDirectoryException;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.parsing.Versions;

/**
 * 管理Minecraft版本的类
 */
public class VersionManager {
    
    /**
     * 获取可用的Minecraft版本列表
     * 
     * @return 版本名称列表
     * @throws MinecraftDirectoryException 如果读取版本信息时发生错误
     */
    public static List<String> getAvailableVersions() throws MinecraftDirectoryException {
        try {
            // 从设置中获取Minecraft目录路径
            Settings settings = SettingsManager.loadSettings();
            String minecraftPath = settings.getMinecraftPath();
            
            // 检查路径是否有效
            if (minecraftPath == null || minecraftPath.isEmpty()) {
                // 如果路径无效，使用默认路径
                minecraftPath = System.getProperty("user.home") + "/.minecraft";
            }
            
            File minecraftFolder = new File(minecraftPath);
            if (!minecraftFolder.exists() || !minecraftFolder.isDirectory()) {
                throw new MinecraftDirectoryException("Minecraft目录不存在或不是有效目录: " + minecraftPath);
            }
            
            // 检查versions目录是否存在
            File versionsFolder = new File(minecraftFolder, "versions");
            if (!versionsFolder.exists() || !versionsFolder.isDirectory()) {
                throw new MinecraftDirectoryException("Minecraft版本目录不存在: " + versionsFolder.getAbsolutePath());
            }
            
            // 使用JMCCC库获取版本列表
            MinecraftDirectory minecraftDir = new MinecraftDirectory(minecraftFolder);
            Set<String> versions = Versions.getVersions(minecraftDir);
            
            // 如果版本列表为空，尝试手动检查版本目录
            if (versions.isEmpty()) {
                File[] versionDirs = versionsFolder.listFiles(File::isDirectory);
                if (versionDirs != null && versionDirs.length > 0) {
                    for (File versionDir : versionDirs) {
                        // 检查是否有对应的json文件
                        File versionJson = new File(versionDir, versionDir.getName() + ".json");
                        if (versionJson.exists() && versionJson.isFile()) {
                            versions.add(versionDir.getName());
                        }
                    }
                }
            }
            
            // 转换为List并返回
            return new ArrayList<>(versions);
        } catch (IOException e) {
            throw new MinecraftDirectoryException("获取Minecraft版本列表失败", e);
        }
    }
    
    /**
     * 检查指定版本是否存在
     * 
     * @param versionName 要检查的版本名称
     * @return 如果版本存在返回true，否则返回false
     * @throws MinecraftDirectoryException 如果读取版本信息时发生错误
     */
    public static boolean versionExists(String versionName) throws MinecraftDirectoryException {
        List<String> versions = getAvailableVersions();
        return versions.contains(versionName);
    }
}