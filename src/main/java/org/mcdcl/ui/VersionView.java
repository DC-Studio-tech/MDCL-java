package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class VersionView extends VBox {
    private ListView<String> versionList;
    private Button refreshButton;
    private Button downloadButton;
    private Button selectButton;

    public VersionView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // 标题
        Label titleLabel = new Label("游戏版本管理");
        titleLabel.setFont(new Font(20));

        // 版本列表
        versionList = new ListView<>();
        versionList.setPrefHeight(400);
        versionList.setPrefWidth(300);

        // 按钮区域
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        refreshButton = new Button("刷新版本列表");
        refreshButton.setPrefWidth(150);

        downloadButton = new Button("下载新版本");
        downloadButton.setPrefWidth(150);

        selectButton = new Button("选择版本");
        selectButton.setPrefWidth(150);

        buttonBox.getChildren().addAll(refreshButton, downloadButton, selectButton);

        // 添加所有组件到主布局
        getChildren().addAll(titleLabel, versionList, buttonBox);
    }

    public ListView<String> getVersionList() {
        return versionList;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getDownloadButton() {
        return downloadButton;
    }

    public Button getSelectButton() {
        return selectButton;
    }
}