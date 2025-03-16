package org.mcdcl.controller;

import org.mcdcl.auth.AuthenticationService;
import org.mcdcl.config.UserPreferences;
import org.mcdcl.ui.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.to2mbn.jmccc.auth.AuthenticationException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("请输入用户名和密码");
            return;
        }

        statusLabel.setText("登录中...");

        // 调用认证服务进行登录验证
        AuthenticationService authService = new AuthenticationService();
        try {
            boolean loginSuccess = authService.authenticate(username, password);

            if (loginSuccess) {
                // 登录成功，保存用户信息
                if (rememberMe.isSelected()) {
                    UserPreferences.saveCredentials(username, password);
                }
                // 跳转到主界面
                SceneManager.loadMainScene();
            } else {
                statusLabel.setText("用户名或密码错误");
            }
        } catch (AuthenticationException e) {
            statusLabel.setText("登录错误: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        // 打开注册窗口
        try {
            SceneManager.loadRegisterScene();
        } catch (Exception e) {
            statusLabel.setText("打开注册窗口失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}