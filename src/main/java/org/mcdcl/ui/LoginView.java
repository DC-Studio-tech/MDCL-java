package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LoginView extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button offlineButton;
    private Button microsoftLoginButton;

    public LoginView() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        // 标题
        Label titleLabel = new Label("MDCL 登录");
        titleLabel.setFont(new Font(24));

        // 创建表单布局
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        // 用户名输入
        Label usernameLabel = new Label("用户名:");
        usernameField = new TextField();
        usernameField.setPromptText("请输入用户名");
        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);

        // 密码输入
        Label passwordLabel = new Label("密码:");
        passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);

        // 按钮布局
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        loginButton = new Button("登录");
        loginButton.setPrefWidth(100);
        
        offlineButton = new Button("离线模式");
        offlineButton.setPrefWidth(100);
        
        // 微软登录按钮
        microsoftLoginButton = new Button("微软登录");
        microsoftLoginButton.setPrefWidth(100);
        try {
            // 可以添加微软图标
            // ImageView microsoftIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/microsoft_icon.png")));
            // microsoftIcon.setFitHeight(16);
            // microsoftIcon.setFitWidth(16);
            // microsoftLoginButton.setGraphic(microsoftIcon);
        } catch (Exception e) {
            System.err.println("无法加载微软图标: " + e.getMessage());
        }

        buttonBox.getChildren().addAll(loginButton, offlineButton, microsoftLoginButton);

        // 添加所有组件到主布局
        getChildren().addAll(titleLabel, formGrid, buttonBox);
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getOfflineButton() {
        return offlineButton;
    }
    
    public Button getMicrosoftLoginButton() {
        return microsoftLoginButton;
    }
}