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
            
            // TODO: 实现实际的资源包下载逻辑
            // 这里暂时使用模拟下载
            for (double progress = 0; progress <= 1; progress += 0.1) {
                Thread.sleep(500);
                progressCallback.accept(progress);
            }
            
        } catch (Exception e) {
            throw new IOException("下载资源包失败: " + e.getMessage());
        }
    }
}