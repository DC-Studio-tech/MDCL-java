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
            "MDCL (Minecraft Development Client Launcher) 是一个开源的Minecraft启动器，" +
            "旨在为玩家提供简单、快速、安全的游戏启动体验。"
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