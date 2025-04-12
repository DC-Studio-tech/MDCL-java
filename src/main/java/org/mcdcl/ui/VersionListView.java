package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.mcdcl.version.VersionDownloader;
import org.to2mbn.jmccc.mcdownloader.RemoteVersion;
import org.to2mbn.jmccc.mcdownloader.RemoteVersionList;

import java.util.Map;

public class VersionListView extends VBox {
    
    private ListView<String> versionListView;
    
    public VersionListView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        
        // 标题
        Label titleLabel = new Label("Minecraft版本列表");
        titleLabel.getStyleClass().add("section-title");
        
        // 版本列表
        versionListView = new ListView<>();
        versionListView.setPrefHeight(400);
        VBox.setVgrow(versionListView, Priority.ALWAYS);
        
        // 按钮区域
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button refreshButton = new Button("刷新列表");
        refreshButton.setOnAction(e -> fetchVersionList());
        
        buttonBox.getChildren().add(refreshButton);
        
        // 将组件添加到布局中
        getChildren().addAll(titleLabel, versionListView, buttonBox);
        
        // 初始加载版本列表
        fetchVersionList();
    }
    
    private void fetchVersionList() {
        VersionDownloader downloader = new VersionDownloader();
        downloader.fetchVersionList(new VersionDownloader.VersionListCallback() {
            @Override
            public void onVersionListFetched(RemoteVersionList versionList) {
                // 清空列表
                versionListView.getItems().clear();
                
                // 添加版本到列表
                // 修改这里，使用Map.entrySet()来迭代
                for (Map.Entry<String, RemoteVersion> entry : versionList.getVersions().entrySet()) {
                    versionListView.getItems().add(entry.getKey());
                }
            }
        });
    }
}