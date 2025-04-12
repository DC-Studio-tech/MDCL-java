package org.mcdcl.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

public class VersionManager {
    private static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    private static final File mcDir = new File(System.getProperty("user.home"), ".minecraft");
    
    public enum VersionType {
        RELEASE("正式版"),
        SNAPSHOT("快照版"),
        ALL("全部版本");
        
        private final String displayName;
        
        VersionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }

    public static List<String> getAvailableVersions(VersionType type) throws IOException {
        List<String> versions = new ArrayList<>();
        try {
            URL url = new URL(VERSION_MANIFEST_URL);
            JSONObject json = new JSONObject(new String(url.openStream().readAllBytes()));
            JSONArray versions_json = json.getJSONArray("versions");
            
            for (int i = 0; i < versions_json.length(); i++) {
                JSONObject version = versions_json.getJSONObject(i);
                String versionType = version.getString("type");
                if (type == VersionType.ALL || 
                    (type == VersionType.RELEASE && "release".equals(versionType)) ||
                    (type == VersionType.SNAPSHOT && "snapshot".equals(versionType))) {
                    versions.add(version.getString("id"));
                }
            }
        } catch (Exception e) {
            throw new IOException("获取版本列表失败: " + e.getMessage());
        }
        return versions;
    }

    public static void downloadVersion(String version, Consumer<Double> progressCallback) throws IOException {
        try {
            // 获取版本信息
            URL manifestUrl = new URL(VERSION_MANIFEST_URL);
            JSONObject manifest = new JSONObject(new String(manifestUrl.openStream().readAllBytes()));
            JSONArray versions = manifest.getJSONArray("versions");
            
            String versionUrl = null;
            for (int i = 0; i < versions.length(); i++) {
                JSONObject versionInfo = versions.getJSONObject(i);
                if (version.equals(versionInfo.getString("id"))) {
                    versionUrl = versionInfo.getString("url");
                    break;
                }
            }
            
            if (versionUrl == null) {
                throw new IOException("未找到指定版本");
            }

            // 下载版本JSON
            URL versionJsonUrl = new URL(versionUrl);
            JSONObject versionJson = new JSONObject(new String(versionJsonUrl.openStream().readAllBytes()));
            
            // 获取客户端下载URL
            String clientUrl = versionJson.getJSONObject("downloads").getJSONObject("client").getString("url");
            
            // 创建版本目录
            File versionDir = new File(mcDir, "versions/" + version);
            versionDir.mkdirs();
            
            // 下载客户端JAR
            downloadFile(new URL(clientUrl), new File(versionDir, version + ".jar"), progressCallback);
            
            // 保存版本JSON
            try (FileWriter writer = new FileWriter(new File(versionDir, version + ".json"))) {
                writer.write(versionJson.toString(2));
            }
            
            progressCallback.accept(1.0);
            
        } catch (Exception e) {
            throw new IOException("下载版本失败: " + e.getMessage());
        }
    }
    
    private static void downloadFile(URL url, File file, Consumer<Double> progressCallback) throws IOException {
        try (InputStream in = url.openStream();
             FileOutputStream out = new FileOutputStream(file)) {
            
            byte[] buffer = new byte[8192];
            long total = url.openConnection().getContentLengthLong();
            long downloaded = 0;
            int count;
            
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
                downloaded += count;
                if (total > 0) {
                    progressCallback.accept((double) downloaded / total);
                }
            }
        }
    }
}