package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {
    private VBox navigationBar;
    private StackPane contentArea;
    private LaunchConfigView launchConfigView;
    private Label userInfoLabel;
    private VBox accountSection;
    private VBox gameSection;
    private VBox generalSection;
    private Button backButton;
    private Button launchButton;

    public MainView() {
        // 初始化组件
        launchConfigView = new LaunchConfigView();
        userInfoLabel = new Label("未登录");
        contentArea = new StackPane();

        // 初始化导航栏
        navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(20));
        navigationBar.setPrefWidth(250);
        navigationBar.getStyleClass().add("navigation-bar");

        // 设置内容区域的边距
        contentArea.setPadding(new Insets(20));

        // 创建返回按钮
        backButton = new Button("返回");
        backButton.getStyleClass().add("back-button");
        backButton.setVisible(false);
        backButton.setOnAction(event -> showWelcomeView());
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(0, 0, 0, 0));

        // 创建账户分类
        accountSection = createSection("账户");
        Button settingsButton = createNavButton("账户设置");
        settingsButton.setOnAction(event -> {
            contentArea.getChildren().clear();
            AccountSettingsView accountSettingsView = new AccountSettingsView();
            contentArea.getChildren().addAll(accountSettingsView, backButton);
            backButton.setVisible(true);
        });
        accountSection.getChildren().add(settingsButton);

        // 创建游戏分类
        gameSection = createSection("游戏");
        Button versionsButton = createNavButton("版本列表");
        versionsButton.setOnAction(event -> {
            contentArea.getChildren().clear();
            VersionView versionView = new VersionView();
            contentArea.getChildren().addAll(versionView, backButton);
            backButton.setVisible(true);
        });
        gameSection.getChildren().add(versionsButton);

        // 创建启动游戏按钮
        launchButton = new Button("启动游戏");
        launchButton.getStyleClass().addAll("launch-button", "nav-button");
        StackPane.setAlignment(launchButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(launchButton, new Insets(0, 20, 20, 0));

        // 创建通用分类
        generalSection = createSection("通用");
        Button settingsGeneralButton = createNavButton("常规设置");
        settingsGeneralButton.setOnAction(event -> {
            contentArea.getChildren().clear();
            SettingsView settingsView = new SettingsView();
            contentArea.getChildren().addAll(settingsView, backButton);
            backButton.setVisible(true);
        });

        Button aboutButton = createNavButton("关于");
        aboutButton.setOnAction(event -> {
            contentArea.getChildren().clear();
            AboutView aboutView = new AboutView();
            contentArea.getChildren().addAll(aboutView, backButton);
            backButton.setVisible(true);
        });
        generalSection.getChildren().addAll(settingsGeneralButton, aboutButton);

        // 添加所有分类到导航栏
        navigationBar.getChildren().addAll(
            createHeader(),
            accountSection,
            new Separator(),
            gameSection,
            new Separator(),
            generalSection
        );

        // 设置默认内容
        showWelcomeView();
        contentArea.getChildren().add(backButton);

        // 设置布局
        setLeft(navigationBar);
        setCenter(contentArea);

        // 设置样式
        getStyleClass().add("main-view");
        userInfoLabel.getStyleClass().add("user-info-label");
    }

    public Label getUserInfoLabel() {
        return userInfoLabel;
    }

    public LaunchConfigView getLaunchConfigView() {
        return launchConfigView;
    }
    
    private VBox createSection(String title) {
        VBox section = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        section.getChildren().add(titleLabel);
        return section;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void showWelcomeView() {
        contentArea.getChildren().clear();
        VBox welcomeContent = new VBox(10);
        welcomeContent.setAlignment(Pos.CENTER);
        welcomeContent.getChildren().add(new Label("欢迎使用MDCL启动器"));
        contentArea.getChildren().addAll(welcomeContent, launchButton);
        backButton.setVisible(false);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 15, 0));

        Label titleLabel = new Label("MDCL");
        titleLabel.getStyleClass().add("header-title");

        header.getChildren().addAll(titleLabel, userInfoLabel);
        return header;
    }


}