package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class AboutView extends VBox {
    public AboutView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // 标题
        Label titleLabel = new Label("关于MDCL");
        titleLabel.getStyleClass().add("section-title");

        // 版本信息
        Label versionLabel = new Label("版本: 1.0.0");
        versionLabel.getStyleClass().add("about-text");

        // 描述信息
        Label descriptionLabel = new Label(
            "Dimension Collapsed Launcher 是一个开源的Minecraft启动器，" +
            "维度折叠启动器是一款专为Minecraft玩家设计的轻量级启动器，支持快速启动游戏、管理模组和配置文件。无论是单机游玩还是连接服务器，都能为您提供流畅的体验。"
        );
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        descriptionLabel.getStyleClass().add("about-text");

        // 版权信息
        Label copyrightLabel = new Label("© 2024 MDCL Team. All rights reserved.");
        copyrightLabel.getStyleClass().add("about-text");

        // 添加所有组件到布局
        getChildren().addAll(
            titleLabel,
            versionLabel,
            descriptionLabel,
            copyrightLabel
        );
    }
}