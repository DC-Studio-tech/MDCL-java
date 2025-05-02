package org.mcdcl.ui;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;
import org.mcdcl.launcher.GameLauncher;
import org.to2mbn.jmccc.launch.LaunchException;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView; // Import MultiplayerView
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration; // Import Color class

public class MainView extends BorderPane {
    private TabPane tabPane;
    private VBox navigationBar;
    private StackPane contentArea;
    private StackPane mainStack; // New StackPane for center
    private LaunchConfigView launchConfigView;
    private Label userInfoLabel;
    private VBox accountSection;
    private VBox gameSection;
    private VBox generalSection;
    private Button backButton;
    private Button launchButton;
    private SettingsView settingsView;
    private GameLauncher gameLauncher;
    private Node someNode; // 定义 someNode 变量
    public MainView() {
        // 在构造函数开始处初始化 TabPane
        tabPane = new TabPane();
        // 初始化 someNode
        someNode = new VBox();
        
        // 初始化组件
        launchConfigView = new LaunchConfigView();
        userInfoLabel = new Label("V3.0.0");
        contentArea = new StackPane();

        // 初始化导航栏
        navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(20));
        navigationBar.setPrefWidth(250);
        navigationBar.getStyleClass().add("navigation-bar");

        // 设置内容区域的边距和背景
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: rgba(46, 52, 64, 0.7); -fx-background-radius: 10;"); // 添加半透明背景和圆角

        // 创建返回按钮
        backButton = new Button("返回");
        backButton.getStyleClass().add("back-button");
        backButton.setVisible(false);
        backButton.setOnAction(event -> showWelcomeView());
        // Alignment/Margin will be set in mainStack later

        // 创建账户分类
        accountSection = createSection("账户");
        Button settingsButton = createNavButton("账户设置");
        settingsButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            AccountSettingsView accountSettingsView = new AccountSettingsView();
            switchViewWithAnimation(currentView, accountSettingsView, true);
        });
        accountSection.getChildren().add(settingsButton);

        // 创建游戏分类
        gameSection = createSection("游戏");
        
        // 添加仪表盘按钮
        Button dashboardButton = createNavButton("仪表盘");
        dashboardButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            DashboardView dashboardView = new DashboardView();
            switchViewWithAnimation(currentView, dashboardView, true);
        });
        gameSection.getChildren().add(dashboardButton);
        Button versionsButton = createNavButton("版本列表");
        versionsButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            VersionView versionView = new VersionView();
            
            // 为选择版本按钮添加事件处理器
            versionView.getSelectButton().setOnAction(e -> {
                String selectedVersion = versionView.getVersionList().getSelectionModel().getSelectedItem();
                if (selectedVersion != null && !selectedVersion.isEmpty()) {
                    launchGame(selectedVersion, null); // Pass null for server IP
                } else {
                    showAlert("未选择版本", "请先从列表中选择一个游戏版本");
                }
            });
            switchViewWithAnimation(currentView, versionView, true);
        });
        gameSection.getChildren().add(versionsButton);

        // 添加下载按钮
        Button downloadButton = createNavButton("下载");
        downloadButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            DownloadView downloadView = new DownloadView();
            switchViewWithAnimation(currentView, (Node)downloadView, true);
        });
        gameSection.getChildren().add(downloadButton);

        // 添加版本管理按钮
        Button versionManagementButton = createNavButton("版本管理");
        versionManagementButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            VersionManagementView versionManagementView = new VersionManagementView();
            switchViewWithAnimation(currentView, (Node)versionManagementView, true);
        });
        gameSection.getChildren().add(versionManagementButton);

        // 添加趣味功能按钮
        Button funFeaturesButton = createNavButton("趣味功能");
        funFeaturesButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            MoreFeaturesView moreFeaturesView = new MoreFeaturesView();
            switchViewWithAnimation(currentView, (Node)moreFeaturesView, true);
        });
        gameSection.getChildren().add(funFeaturesButton);

        // 添加多人游戏按钮
        Button multiplayerButton = createNavButton("多人游戏");
        multiplayerButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            MultiplayerView multiplayerView = new MultiplayerView(this); // Pass MainView instance
            switchViewWithAnimation(currentView, multiplayerView, true);
        });
        gameSection.getChildren().add(multiplayerButton);

        // 创建启动游戏按钮
        launchButton = new Button("启动游戏");
        FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PLAY);
        playIcon.setSize("18");
        playIcon.setFill(Color.web("#FFFFFF")); // Set initial color for launch button icon
        launchButton.setGraphic(playIcon);
        launchButton.getStyleClass().addAll("launch-button", "nav-button");

        // Add hover listener for launch button icon
        launchButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Mouse entered
                playIcon.setFill(Color.web("#FFFFFF")); // Keep white on hover for launch button
            } else { // Mouse exited
                playIcon.setFill(Color.web("#FFFFFF")); // Revert to white
            }
        });

        // Modify launch button action to get selected version or default
        launchButton.setOnAction(event -> {
            // Logic to get selected version (e.g., from a ComboBox or default)
            // This part needs refinement based on where the version selection is stored/managed
            // For now, let's assume a default or previously selected version is available
            // We'll use a placeholder logic here, assuming getSelectedVersion() exists
            String version = getSelectedOrDefaultVersion();
            if (version != null) {
                launchGame(version, null); // Launch with null server IP
            } else {
                showAlert("未选择版本", "请选择一个游戏版本或确保版本可用");
            }
        });
        // Alignment/Margin will be set in mainStack later

        // 创建通用分类
        generalSection = createSection("通用");
        Button settingsGeneralButton = createNavButton("常规设置");
        settingsGeneralButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            settingsView = new SettingsView();
            switchViewWithAnimation(currentView, settingsView, true);
        });

        Button aboutButton = createNavButton("关于");
        aboutButton.setOnAction(event -> {
            Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
            AboutView aboutView = new AboutView();
            switchViewWithAnimation(currentView, aboutView, true);
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

        // Create mainStack to hold contentArea and overlay buttons
        mainStack = new StackPane();
        mainStack.getChildren().addAll(contentArea, backButton, launchButton);

        // Set alignment and margins for buttons within mainStack
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(10, 0, 0, 10)); // Adjusted margin for better placement
        StackPane.setAlignment(launchButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(launchButton, new Insets(0, 20, 20, 0));

        // 设置布局
        setLeft(navigationBar);
        setCenter(mainStack); // Set mainStack as the center

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
        
        // 根据不同按钮添加不同图标
        FontIcon icon = null;
        switch (text) {
            case "账户设置":  // 修改为实际按钮文本
                icon = new FontIcon(MaterialDesignA.ACCOUNT);
                break;
            case "仪表盘":
                icon = new FontIcon(MaterialDesignD.DESKTOP_MAC_DASHBOARD);
                break;
            case "版本列表":  // 修改为实际按钮文本
                icon = new FontIcon(MaterialDesignA.ARCHIVE);
                break;
            case "常规设置":
                icon = new FontIcon(MaterialDesignC.COG);
                break;
            case "关于":
                icon = new FontIcon(MaterialDesignI.INFORMATION);
                break;
            case "趣味功能":
                icon = new FontIcon(MaterialDesignI.INFORMATION);  // 改用 INFORMATION 图标
                break;
            case "下载":
                icon = new FontIcon(MaterialDesignA.ARROW_DOWN);
                break;
            case "版本管理":
                icon = new FontIcon(MaterialDesignA.ARCHIVE);
                break;
        }
        
        if (icon != null) {
            icon.setIconSize(18);
            icon.setIconSize(18);
            // Set initial icon color based on CSS or default
            icon.setIconColor(Color.web("#5E5CE6")); // Default purple
            button.setGraphic(icon);
            button.setContentDisplay(ContentDisplay.LEFT);

            // Add hover listeners to explicitly control icon color
            final FontIcon finalIcon = icon; // Need final variable for lambda
            button.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) { // Mouse entered
                    finalIcon.setIconColor(Color.web("#5E5CE6")); // Keep purple on hover
                } else { // Mouse exited
                    finalIcon.setIconColor(Color.web("#5E5CE6")); // Revert to purple (or original color)
                }
            });
        }
        
        return button;
    }
            
    private <T extends javafx.scene.Node> void switchViewWithAnimation(T oldView, T newView, boolean showBackButton) {
        Runnable updateContent = () -> {
            // Clear only the contentArea where the switchable view resides
            contentArea.getChildren().clear();
            newView.setOpacity(0.0);
            contentArea.getChildren().add(newView); // Add the new view to contentArea
    
            // Control back button visibility (it's already in mainStack)
            backButton.setVisible(showBackButton);
    
            // Start fade-in for the new view within contentArea
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), newView);
            fadeIn.setOnFinished(e -> {
                // After fade-in, ensure buttons are correctly placed and visible
                mainStack.getChildren().removeAll(backButton, launchButton);
                mainStack.getChildren().addAll(backButton, launchButton);
                backButton.setVisible(showBackButton);
            });
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        };
    
        if (oldView != null && contentArea.getChildren().contains(oldView)) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), oldView);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> updateContent.run());
            fadeOut.play();
        } else {
            updateContent.run(); // No old view or not in contentArea, just update
        }
    }
    
    private void showWelcomeView() {
        Node currentView = contentArea.getChildren().isEmpty() ? null : contentArea.getChildren().get(0);
        // Avoid animating if the current view is already the welcome view components
        if (currentView instanceof VBox && ((VBox)currentView).getChildren().get(0) instanceof Label && ((Label)((VBox)currentView).getChildren().get(0)).getText().equals("欢迎使用MDCL启动器")) {
             // Already showing welcome view, do nothing or just ensure state
             if (!contentArea.getChildren().contains(launchButton)) {
                 contentArea.getChildren().add(launchButton);
             }
             backButton.setVisible(false);
             return;
        }

        VBox welcomeContent = new VBox(10);
        welcomeContent.setAlignment(Pos.CENTER);
        welcomeContent.getChildren().add(new Label("欢迎使用MDCL启动器"));
        
        // Use animation helper, setting backButton visibility to false
        switchViewWithAnimation(currentView, welcomeContent, false);
        // launchButton is now managed outside contentArea, no need to add/remove here
    }

    // Method to get selected version or a default one (Placeholder)
    public String getSelectedOrDefaultVersion() {
        try {
            String selectedVersion = org.mcdcl.config.SettingsManager.loadSettings().getSelectedVersion();
            if (selectedVersion != null && !selectedVersion.isEmpty()) {
                return selectedVersion;
            }
        } catch (IOException e) {
            System.err.println("Error loading selected version from settings: " + e.getMessage());
        }
        System.err.println("No version selected in settings, returning null.");
        return null;
    }

    // Removed the faulty launchGameWithServer method

    // Modified launchGame method to accept version and server IP
    public void launchGame(String versionToLaunch, String serverIp) {
        // 如果settingsView为null，先尝试加载设置
        if (settingsView == null) {
            settingsView = new SettingsView(); // Consider loading settings if needed
        }

        // 添加测试错误视图的代码 - 可以通过注释切换来测试
         if (false) { // 设置为true来测试错误视图,不需要的时候改成false
            simulateGameLaunchError();
            return;
        }

        String javaPath = settingsView.getJavaPathComboBox().getValue();
        if (javaPath == null || javaPath.isEmpty()) {
            showAlert("Java路径未配置", "请在常规设置中选择或配置Java路径");
            return;
        }

        if (versionToLaunch == null || versionToLaunch.isEmpty()) {
            showAlert("未指定版本", "无法启动游戏，未指定版本。");
            return;
        }

        try {
            // 从GB转换为MB，SettingsView中的内存值是以GB为单位的
            int maxMemoryGB = Integer.parseInt(settingsView.getMaxMemoryField().getText());
            int maxMemory = maxMemoryGB * 1024; // 转换为MB
            String jvmArgs = settingsView.getJvmArgsField().getText();
            String gameArgs = settingsView.getGameArgsField().getText();
            
            // 从设置中获取Minecraft目录路径
            String minecraftPath = "";
            try {
                minecraftPath = org.mcdcl.config.SettingsManager.loadSettings().getMinecraftPath();
            } catch (IOException e) {
                // 如果加载失败，使用默认路径
                minecraftPath = System.getProperty("user.home") + "/.minecraft";
            }

            // Check if the specified version exists (optional but good practice)
            try {
                java.util.List<String> availableVersions = org.mcdcl.version.VersionManager.getAvailableVersions();
                if (!availableVersions.contains(versionToLaunch)) {
                     showAlert("版本不可用", "指定的版本 " + versionToLaunch + " 不可用或未安装。");
                     return;
                }
            } catch (Exception e) {
                 showAlert("版本检查失败", "无法检查可用版本: " + e.getMessage());
                 return;
            }

            gameLauncher = new GameLauncher(javaPath, maxMemory, jvmArgs, gameArgs, minecraftPath);
            // Pass serverIp to the launcher
            gameLauncher.launchGame(versionToLaunch, serverIp);
            launchButton.setDisable(true);
            launchButton.setText("游戏运行中");

                // 启动一个线程来监控游戏进程
                new Thread(() -> {
                    System.out.println("开始监控游戏进程...");
                    boolean wasRunning = false;
                    
                    // 检查进程是否成功启动
                    if (gameLauncher.isRunning()) {
                        wasRunning = true;
                        System.out.println("游戏进程已成功启动");
                    } else {
                        System.out.println("警告：游戏进程可能未成功启动");
                        javafx.application.Platform.runLater(() -> {
                            ErrorView errorView = new ErrorView(
                                "游戏启动失败", 
                                "启动Minecraft时发生错误: 游戏进程可能未成功启动", 
                                new Exception("游戏进程可能未成功启动")
                            );
                            
                            // 设置返回处理器
                            errorView.setReturnHandler(evt -> showWelcomeView());
                            
                            // 将errorView添加到场景中
                            contentArea.getChildren().setAll(errorView);
                        });
                    }
                    
                    // 监控进程运行状态
                    while (gameLauncher.isRunning()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    
                    // 进程已结束
                    System.out.println("游戏进程已结束");
                    if (wasRunning) {
                        System.out.println("游戏正常退出");
                    } else {
                        System.out.println("警告：游戏可能异常退出或未成功启动");
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

    // Removed duplicate launchGame(String versionName) method
    // private void launchGame(String versionName) {
    //     // ... (old code removed) ...
    // }

    // Helper method to show alerts (made public for MultiplayerView)
    public void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to update user info label
    public void updateUserInfo(String username) {
        if (username != null && !username.isEmpty()) {
            userInfoLabel.setText("当前账户: " + username);
        } else {
            userInfoLabel.setText("未登录");
        }
    }

    // Re-add the createHeader method
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 15, 0));

        Label titleLabel = new Label("MDCL");
        titleLabel.getStyleClass().add("header-title");

        header.getChildren().addAll(titleLabel, userInfoLabel);
        return header;
    }

    // Method to simulate game launch error for testing ErrorView
    private void simulateGameLaunchError() {
        System.out.println("Simulating game launch error...");
        try {
            // Simulate a condition that causes LaunchException
            throw new LaunchException("模拟启动错误：无效的配置文件");
        } catch (LaunchException e) {
            System.err.println("Simulated error caught: " + e.getMessage());
            ErrorView errorView = new ErrorView(
                "模拟游戏启动失败", 
                "启动Minecraft时发生模拟错误: " + e.getMessage(), 
                e
            );
            
            // 设置返回处理器
            errorView.setReturnHandler(evt -> showWelcomeView());
            
            // 将errorView添加到场景中
            contentArea.getChildren().setAll(errorView);
            backButton.setVisible(true); // Ensure back button is visible
        }
    }

    // Method called by MultiplayerView to launch game with server IP
    public void launchGameWithServer(String serverIp) {
        String version = getSelectedOrDefaultVersion(); // Get the currently selected/default version
        if (version != null) {
            System.out.println("MainView: Launching game version " + version + " to server " + serverIp);
            launchGame(version, serverIp); // Call the main launchGame method
        } else {
            showAlert("未选择版本", "请先选择一个游戏版本再加入服务器");
        }
    }
    private void handleVersionSelection(String selectedVersion) {
        if (selectedVersion != null && !selectedVersion.isEmpty()) {
            // Update UI or store selection
            // Potentially enable launch button or auto-launch
            // Example: launchGame(selectedVersion, null);
        } else {
            // Handle case where no version is selected
        }
    }

    // Placeholder for a method that might be called from elsewhere
    public void someOtherActionRequiringLaunch() {
        String selectedVersion = getSelectedOrDefaultVersion();
        if (selectedVersion != null) {
            // Corrected call to use the two-argument method
            launchGame(selectedVersion, null); 
        } else {
            showAlert("未选择版本", "请选择一个游戏版本");
        }
    }
}