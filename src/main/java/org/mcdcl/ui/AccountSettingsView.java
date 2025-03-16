package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AccountSettingsView extends VBox {
    private final Label titleLabel;
    private final Label userStatusLabel;
    private final Button loginButton;
    private Stage loginStage;

    public AccountSettingsView() {
        // 设置基本布局
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // 创建标题
        titleLabel = new Label("账户设置");
        titleLabel.getStyleClass().add("section-title");

        // 创建用户状态标签
        userStatusLabel = new Label("未登录");
        userStatusLabel.getStyleClass().add("user-info-label");

        // 创建登录按钮
        loginButton = new Button("登录");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(200);

        // 添加组件到布局
        getChildren().addAll(titleLabel, userStatusLabel, loginButton);

        // 设置登录按钮点击事件
        loginButton.setOnAction(event -> showLoginDialog());
    }

    private void showLoginDialog() {
        if (loginStage == null) {
            loginStage = new Stage();
            loginStage.setTitle("登录");

            LoginView loginView = new LoginView();
            javafx.scene.Scene scene = new javafx.scene.Scene(loginView);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

            loginStage.setScene(scene);
        }
    }
}