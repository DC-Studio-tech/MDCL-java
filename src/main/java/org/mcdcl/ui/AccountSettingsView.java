package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountSettingsView extends VBox {
    private Label titleLabel;
    private Label userStatusLabel;
    private Button loginButton;
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
            
            // 设置登录按钮事件
            loginView.getLoginButton().setOnAction(e -> {
                String username = loginView.getUsernameField().getText();
                String password = loginView.getPasswordField().getText();
                
                if (username.isEmpty() || password.isEmpty()) {
                    // TODO: 显示错误信息
                    return;
                }
                
                // TODO: 实现登录验证逻辑
                userStatusLabel.setText("已登录: " + username);
                loginStage.close();
            });
            
            // 设置离线模式按钮事件
            loginView.getOfflineButton().setOnAction(e -> {
                userStatusLabel.setText("离线模式");
                loginStage.close();
            });
        }
        
        loginStage.show();
    }

    public void updateUserStatus(String status) {
        userStatusLabel.setText(status);
    }

    public Button getLoginButton() {
        return loginButton;
    }
}