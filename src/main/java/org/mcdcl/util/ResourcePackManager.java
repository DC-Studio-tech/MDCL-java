package org.mcdcl.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResourcePackManager {
    private static final String RESOURCE_PACK_API = "https://api.modrinth.com/v2/search?facets=[[%22project_type:resourcepack%22]]&limit=20";
    private static final File resourcePackDir = new File(System.getProperty("user.home"), ".minecraft/resourcepacks");

    public static List<String> getAvailableResourcePacks() throws IOException {
        List<String> packs = new ArrayList<>();
        try {
            URL url = new URL(RESOURCE_PACK_API);
            JSONObject json = new JSONObject(new String(url.openStream().readAllBytes()));
            JSONArray hits = json.getJSONArray("hits");
            
            for (int i = 0; i < hits.length(); i++) {
                JSONObject pack = hits.getJSONObject(i);
                packs.add(pack.getString("title"));
            }
        } catch (Exception e) {
            throw new IOException("获取资源包列表失败: " + e.getMessage());
        }
        return packs;
    }

    public static void downloadResourcePack(String packName, Consumer<Double> progressCallback) throws IOException {
        try {
            // 确保目录存在
            resourcePackDir.mkdirs();
            
            // 查找资源包下载链接
            URL url = new URL(RESOURCE_PACK_API);
            JSONObject json = new JSONObject(new String(url.openStream().readAllBytes()));
            JSONArray hits = json.getJSONArray("hits");
            
            String downloadUrl = null;
            for (int i = 0; i < hits.length(); i++) {
                JSONObject pack = hits.getJSONObject(i);
                if (pack.getString("title").equals(packName)) {
                    // 假设API返回的结构中包含下载链接
                    downloadUrl = pack.getString("download_url");
                    break;
                }
            }
            
            if (downloadUrl == null) {
                throw new IOException("找不到资源包: " + packName);
            }
            
            // 执行下载
            URL packUrl = new URL(downloadUrl);
            String fileName = packName.replaceAll("[^a-zA-Z0-9.-]", "_") + ".zip";
            File outputFile = new File(resourcePackDir, fileName);
            
            try (InputStream in = packUrl.openStream();
                 FileOutputStream out = new FileOutputStream(outputFile)) {
                
                byte[] buffer = new byte[8192];
                long totalBytes = 0;
                int bytesRead;
                long fileSize = packUrl.openConnection().getContentLengthLong();
                
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    
                    if (fileSize > 0) {
                        double progress = (double) totalBytes / fileSize;
                        progressCallback.accept(progress);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("下载资源包失败: " + e.getMessage(), e);
        }
    }
}
