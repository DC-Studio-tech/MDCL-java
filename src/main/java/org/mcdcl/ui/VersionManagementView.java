package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VersionManagementView extends VBox {
    
    public VersionManagementView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        
        // 标题
        Label titleLabel = new Label("版本管理");
        titleLabel.getStyleClass().add("section-title");
        
        // 版本列表
        ListView<String> versionListView = new ListView<>();
        versionListView.setPrefHeight(400);
        VBox.setVgrow(versionListView, Priority.ALWAYS);
        
        // 按钮区域
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button installButton = new Button("安装版本");
        Button removeButton = new Button("删除版本");
        Button refreshButton = new Button("刷新列表");
        
        buttonBox.getChildren().addAll(installButton, removeButton, refreshButton);
        
        // 将组件添加到布局中
        getChildren().addAll(titleLabel, versionListView, buttonBox);

        //TODO: 加入mod管理
        //TODO: 加入世界管理
        //TODO: 加入独立版本游戏设置
        //TODO: 加入测试游戏
        //TODO: 加入mod更新按钮
        //TODO: 加入mod安装按钮（跳转到下载页面）
        //TODO: 加入mod删除按钮
    }
}