package org.mcdcl.ui;

import java.io.IOException;

import org.mcdcl.launcher.GameLauncher;
import org.to2mbn.jmccc.launch.LaunchException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private SettingsView settingsView;
    private GameLauncher gameLauncher;

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
            
            // 为选择版本按钮添加事件处理器
            versionView.getSelectButton().setOnAction(e -> {
                String selectedVersion = versionView.getVersionList().getSelectionModel().getSelectedItem();
                if (selectedVersion != null && !selectedVersion.isEmpty()) {
                    launchGame(selectedVersion);
                } else {
                    showAlert("未选择版本", "请先从列表中选择一个游戏版本");
                }
            });
            
            contentArea.getChildren().addAll(versionView, backButton);
            backButton.setVisible(true);
        });
        gameSection.getChildren().add(versionsButton);

        // 创建启动游戏按钮
        launchButton = new Button("启动游戏");
        launchButton.getStyleClass().addAll("launch-button", "nav-button");
        launchButton.setOnAction(event -> launchGame());
        StackPane.setAlignment(launchButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(launchButton, new Insets(0, 20, 20, 0));

        // 创建通用分类
        generalSection = createSection("通用");
        Button settingsGeneralButton = createNavButton("常规设置");
        settingsGeneralButton.setOnAction(event -> {
            contentArea.getChildren().clear();
            settingsView = new SettingsView();
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

    private void launchGame() {
        if (settingsView == null) {
            showAlert("请先配置启动器设置", "请在常规设置中配置Java路径和启动参数");
            return;
        }

        String javaPath = settingsView.getJavaPathComboBox().getValue();
        if (javaPath == null || javaPath.isEmpty()) {
            showAlert("Java路径未配置", "请在常规设置中选择或配置Java路径");
            return;
        }

        try {
            int maxMemory = Integer.parseInt(settingsView.getMaxMemoryField().getText());
            String jvmArgs = settingsView.getJvmArgsField().getText();
            String gameArgs = settingsView.getGameArgsField().getText();

            gameLauncher = new GameLauncher(javaPath, maxMemory, jvmArgs, gameArgs);
            gameLauncher.launchGame("1.19.2"); // 替换为你想启动的Minecraft版本
            launchButton.setDisable(true);
            launchButton.setText("游戏运行中");

            // 启动一个线程来监控游戏进程
            new Thread(() -> {
                while (gameLauncher.isRunning()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                javafx.application.Platform.runLater(() -> {
                    launchButton.setDisable(false);
                    launchButton.setText("启动游戏");
                });
            }).start();

        } catch (NumberFormatException e) {
            showAlert("内存配置错误", "请输入有效的内存大小");
        } catch (IOException e) {
            showAlert("启动失败", "游戏启动失败: " + e.getMessage());
        } catch (LaunchException e) {
            showAlert("启动失败", "游戏启动失败: " + e.getMessage());
        }
    }

    private void launchGame(String versionName) {
        if (settingsView == null) {
            showAlert("请先配置启动器设置", "请在常规设置中配置Java路径和启动参数");
            return;
        }

        String javaPath = settingsView.getJavaPathComboBox().getValue();
        if (javaPath == null || javaPath.isEmpty()) {
            showAlert("Java路径未配置", "请在常规设置中选择或配置Java路径");
            return;
        }

        try {
            int maxMemory = Integer.parseInt(settingsView.getMaxMemoryField().getText());
            String jvmArgs = settingsView.getJvmArgsField().getText();
            String gameArgs = settingsView.getGameArgsField().getText();

            gameLauncher = new GameLauncher(javaPath, maxMemory, jvmArgs, gameArgs);
            gameLauncher.launchGame(versionName);
            launchButton.setDisable(true);
            launchButton.setText("游戏运行中");

            // 启动一个线程来监控游戏进程
            new Thread(() -> {
                while (gameLauncher.isRunning()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                javafx.application.Platform.runLater(() -> {
                    launchButton.setDisable(false);
                    launchButton.setText("启动游戏");
                });
            }).start();

        } catch (NumberFormatException e) {
            showAlert("内存配置错误", "请输入有效的内存大小");
        } catch (IOException e) {
            showAlert("启动失败", "游戏启动失败: " + e.getMessage());
        } catch (LaunchException e) {
            showAlert("启动失败", "游戏启动失败: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
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