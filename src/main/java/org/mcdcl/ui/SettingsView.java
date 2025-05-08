package org.mcdcl.ui;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import org.mcdcl.util.JavaFinder;
import org.mcdcl.util.Settings;
import org.mcdcl.util.SettingsManager;

import com.google.gson.GsonBuilder;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SettingsView extends ScrollPane {
    // 通用UI组件创建方法
    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        if (styleClass != null) {
            label.getStyleClass().add(styleClass);
        }
        return label;
    }

    private TextField createTextField(String promptText, String tooltip, double prefWidth) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        if (tooltip != null) {
            textField.setTooltip(new Tooltip(tooltip));
        }
        textField.setPrefWidth(prefWidth);
        return textField;
    }

    private ComboBox<String> createComboBox(String promptText, double prefWidth, List<String> items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(prefWidth);
        if (items != null) {
            comboBox.getItems().addAll(items);
        }
        return comboBox;
    }

    private Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        if (styleClass != null) {
            button.getStyleClass().add(styleClass);
        }
        return button;
    }

    private FlowPane createFlowPane(double hgap, double vgap, Insets padding) {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(hgap);
        flowPane.setVgap(vgap);
        if (padding != null) {
            flowPane.setPadding(padding);
        }
        return flowPane;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private VBox contentBox;
    private ComboBox<String> javaPathComboBox;
    private Button chooseJavaButton;
    private Slider memorySlider;
    private TextField maxMemoryField;
    private TextField jvmArgsField;
    private TextField gameArgsField;
    private ComboBox<String> themeComboBox;
    private ComboBox<String> navigationPositionComboBox; // 导航栏位置选择
    private TextField minecraftPathField;
    private Button chooseMinecraftPathButton;
    private Button saveButton;
    private FlowPane jvmPresetPane;
    private FlowPane gamePresetPane;
    private TextField backgroundPathField;  // 添加背景图片路径字段

    public SettingsView() {
        initializeLayout();
        Settings savedSettings = loadSavedSettings();
        initializeComponents(savedSettings);
    }

    private void initializeLayout() {
        contentBox = new VBox();
        contentBox.setSpacing(15);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.TOP_CENTER);
        
        setContent(contentBox);
        setFitToWidth(true);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setHbarPolicy(ScrollBarPolicy.NEVER);
    }

    private Settings loadSavedSettings() {
        Settings savedSettings = null;
        try {
            savedSettings = SettingsManager.loadSettings();
        } catch (IOException e) {
            System.err.println("无法加载设置: " + e.getMessage());
            savedSettings = new Settings();
        }
        return savedSettings;
    }

    private void initializeComponents(Settings savedSettings) {
        // 标题
        Label titleLabel = createLabel("常规设置", "section-title");

        // Java路径设置
        Label javaPathLabel = new Label("Java路径:");
        HBox javaPathBox = new HBox(10);
        javaPathComboBox = new ComboBox<>();
        javaPathComboBox.setPromptText("请选择Java可执行文件");
        javaPathComboBox.setPrefWidth(240);
        chooseJavaButton = new Button("选择");

        // 加载系统中已安装的Java版本
        List<String> javaInstallations = JavaFinder.findJavaInstallations();
        javaPathComboBox.getItems().addAll(javaInstallations);
        
        // 如果有保存的Java路径，则使用保存的值，否则使用默认值
        if (savedSettings.getJavaPath() != null && !savedSettings.getJavaPath().isEmpty()) {
            // 确保保存的Java路径在下拉列表中
            if (!javaPathComboBox.getItems().contains(savedSettings.getJavaPath())) {
                javaPathComboBox.getItems().add(savedSettings.getJavaPath());
            }
            javaPathComboBox.setValue(savedSettings.getJavaPath());
        } else if (!javaInstallations.isEmpty()) {
            javaPathComboBox.setValue(javaInstallations.get(0));
        }

        // 手动选择Java路径
        chooseJavaButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择Java可执行文件");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Java可执行文件", "java", "java.exe")
            );
            File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
            if (selectedFile != null) {
                String path = selectedFile.getAbsolutePath();
                if (JavaFinder.isValidJavaExecutable(path)) {
                    if (!javaPathComboBox.getItems().contains(path)) {
                        javaPathComboBox.getItems().add(path);
                    }
                    javaPathComboBox.setValue(path);
                } else {
                    javaPathComboBox.setStyle("-fx-border-color: red;");
                    javaPathComboBox.setTooltip(new Tooltip("请选择有效的Java可执行文件"));
                }
            }
        });
        javaPathBox.getChildren().addAll(javaPathComboBox, chooseJavaButton);

        // 最大内存设置
        Label maxMemoryLabel = new Label("最大内存:");
        HBox memoryBox = new HBox(10);
        memorySlider = new Slider(2, 32, savedSettings.getMaxMemory());
        memorySlider.setShowTickLabels(true);
        memorySlider.setShowTickMarks(true);
        memorySlider.setMajorTickUnit(2);
        memorySlider.setBlockIncrement(1);
        maxMemoryField = new TextField(String.valueOf(savedSettings.getMaxMemory()));
        maxMemoryField.setPrefWidth(60);
        Label unitLabel = new Label("GB");
        memoryBox.getChildren().addAll(memorySlider, maxMemoryField, unitLabel);
        
        // 内存滑块和输入框联动
        memorySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxMemoryField.setText(String.format("%.0f", newVal));
        });
        
        maxMemoryField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                maxMemoryField.setText(newVal.replaceAll("[^\\d]", ""));
            } else {
                try {
                    int value = Integer.parseInt(newVal);
                    if (value >= 2 && value <= 32) {
                        memorySlider.setValue(value);
                    }
                } catch (NumberFormatException e) {}
            }
        });

        // JVM参数设置
        Label jvmArgsLabel = new Label("JVM参数:");
        jvmArgsField = new TextField(savedSettings.getJvmArgs());
        jvmArgsField.setPromptText("-XX:+UseG1GC");
        jvmArgsField.setTooltip(new Tooltip("输入JVM启动参数，多个参数用空格分隔"));
        jvmArgsField.setPrefWidth(300);

        // JVM预设参数按钮
        jvmPresetPane = new FlowPane();
        jvmPresetPane.setHgap(10);
        jvmPresetPane.setVgap(10);
        jvmPresetPane.setPadding(new Insets(10));

        addPresetButton("G1垃圾回收", "-XX:+UseG1GC", "使用G1垃圾回收器，适合大内存应用");
        addPresetButton("并行GC", "-XX:+UseParallelGC", "使用并行垃圾回收器，注重吞吐量");
        addPresetButton("内存优化", "-XX:+AggressiveOpts -XX:+UseFastAccessorMethods", "优化内存访问性能");
        addPresetButton("调试模式", "-XX:+HeapDumpOnOutOfMemoryError", "发生内存溢出时自动导出堆转储文件");

        // 游戏参数设置
        Label gameArgsLabel = new Label("游戏参数:");
        gameArgsField = new TextField(savedSettings.getGameArgs());
        gameArgsField.setPromptText("--width 1920 --height 1080");
        gameArgsField.setPrefWidth(300);

        // 游戏参数预设按钮
        gamePresetPane = new FlowPane();
        gamePresetPane.setHgap(10);
        gamePresetPane.setVgap(10);
        gamePresetPane.setPadding(new Insets(10));

        addGamePresetButton("1080p", "--width 1920 --height 1080", "全高清分辨率");
        addGamePresetButton("全屏", "--fullscreen", "全屏模式启动游戏");
        addGamePresetButton("窗口化", "--windowed", "窗口化模式启动游戏");

        // 主题设置
        Label themeLabel = new Label("主题:");
        themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll("默认主题", "深色主题", "浅色主题", "自定义主题");
        themeComboBox.setValue(savedSettings.getTheme());
        themeComboBox.setPrefWidth(300);

        // 将所有组件添加到contentBox - 第一次添加
        contentBox.getChildren().clear(); // 清除之前可能存在的组件
        contentBox.getChildren().addAll(
            titleLabel,
            javaPathLabel, javaPathBox,
            maxMemoryLabel, memoryBox,
            jvmArgsLabel, jvmArgsField, jvmPresetPane,
            gameArgsLabel, gameArgsField, gamePresetPane,
            themeLabel, themeComboBox
        );

        // 导航栏位置设置
        Label navigationPositionLabel = new Label("导航栏位置:");
        navigationPositionLabel.getStyleClass().add("setting-label");
        
        HBox navigationPositionBox = new HBox(10);
        navigationPositionBox.setAlignment(Pos.CENTER_LEFT);
        
        navigationPositionComboBox = new ComboBox<>();
        navigationPositionComboBox.getItems().addAll("左侧", "顶部", "底部");
        navigationPositionComboBox.getStyleClass().add("setting-combo-box");
        navigationPositionComboBox.setPrefWidth(300);
        navigationPositionComboBox.setTooltip(new Tooltip("选择导航栏的显示位置"));
        
        String currentPosition = savedSettings.getNavigationPosition();
        String displayPosition = switch(currentPosition) {
            case "left" -> "左侧";
            case "top" -> "顶部";
            case "bottom" -> "底部";
            default -> "左侧";
        };
        navigationPositionComboBox.setValue(displayPosition);
        
        Button applyPositionButton = new Button("应用");
        applyPositionButton.getStyleClass().addAll("apply-button", "small-button");
        applyPositionButton.setOnAction(event -> {
            String selectedPosition = navigationPositionComboBox.getValue();
            String positionValue = switch(selectedPosition) {
                case "左侧" -> "left";
                case "顶部" -> "top";
                case "底部" -> "bottom";
                default -> "left";
            };
            try {
                Settings settings = SettingsManager.loadSettings();
                settings.setNavigationPosition(positionValue);
                SettingsManager.saveSettings(settings);
                
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("设置已保存");
                alert.setHeaderText("导航栏位置已更改");
                alert.setContentText("需要重启启动器才能应用更改，是否立即重启？");
                
                alert.getButtonTypes().setAll(
                    javafx.scene.control.ButtonType.YES,
                    javafx.scene.control.ButtonType.NO
                );
                
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == javafx.scene.control.ButtonType.YES) {
                        // 重启应用
                        Platform.runLater(() -> {
                            getScene().getWindow().hide();
                            // 这里可以添加重启应用的逻辑
                            Platform.exit();
                        });
                    }
                });
            } catch (IOException e) {
                showErrorAlert("保存设置失败: " + e.getMessage());
            }
        });
        
        navigationPositionBox.getChildren().addAll(navigationPositionComboBox, applyPositionButton);
        
        // 样式表设置
        Label styleLabel = new Label("界面样式:");
        styleComboBox = new ComboBox<>();
        styleComboBox.getItems().addAll("Apple Design", "Google Design", "Default");
        styleComboBox.setValue("Apple Design");
        styleComboBox.setPrefWidth(300);
        
        // 添加样式表切换监听器
        styleComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                getScene().getStylesheets().clear();
                String cssPath;
                switch (newVal) {
                    case "Apple Design" -> cssPath = "/AppleDesign.css";
                    case "Google Design" -> cssPath = "/GoogleDesign.css";
                    default -> cssPath = "/styles/main.css";
                }
                try {
                    String cssUrl = getClass().getResource(cssPath).toExternalForm();
                    getScene().getStylesheets().add(cssUrl);
                } catch (NullPointerException e) {
                    showErrorAlert("无法加载样式表文件：" + cssPath);
                    // 加载失败时使用默认样式
                    String defaultCssUrl = getClass().getResource("/styles/main.css").toExternalForm();
                    getScene().getStylesheets().add(defaultCssUrl);
                }                
            }
        });
        
        // 添加背景图片设置
        Label backgroundLabel = new Label("背景图片:");
        HBox backgroundBox = new HBox(10);
        backgroundPathField = new TextField(savedSettings.getBackgroundImage());
        backgroundPathField.setPromptText("选择背景图片路径");
        backgroundPathField.setPrefWidth(240);
        Button selectBackgroundButton = new Button("选择");
        
        selectBackgroundButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择背景图片");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.png")
            );
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                backgroundPathField.setText(file.getAbsolutePath());
                updateBackground(file.getAbsolutePath());
            }
        });
        
        backgroundBox.getChildren().addAll(backgroundPathField, selectBackgroundButton);

        // Minecraft目录设置
        Label minecraftPathLabel = new Label("Minecraft目录:");
        HBox minecraftPathBox = new HBox(10);
        minecraftPathField = new TextField(savedSettings.getMinecraftPath());
        minecraftPathField.setPromptText("请选择Minecraft游戏目录");
        minecraftPathField.setPrefWidth(240);
        chooseMinecraftPathButton = new Button("选择");
        
        // 选择Minecraft目录按钮事件
        chooseMinecraftPathButton.setOnAction(e -> {
            javafx.stage.DirectoryChooser directoryChooser = new javafx.stage.DirectoryChooser();
            directoryChooser.setTitle("选择Minecraft游戏目录");
            
            // 如果已有路径，则设置为初始目录
            String currentPath = minecraftPathField.getText();
            if (currentPath != null && !currentPath.isEmpty()) {
                File initialDir = new File(currentPath);
                if (initialDir.exists() && initialDir.isDirectory()) {
                    directoryChooser.setInitialDirectory(initialDir);
                }
            }
            
            File selectedDirectory = directoryChooser.showDialog(getScene().getWindow());
            if (selectedDirectory != null) {
                minecraftPathField.setText(selectedDirectory.getAbsolutePath());
            }
        });
        
        minecraftPathBox.getChildren().addAll(minecraftPathField, chooseMinecraftPathButton);

        // 保存按钮
        saveButton = new Button("保存设置");
        saveButton.getStyleClass().add("save-button");
        saveButton.setMaxWidth(200);

        // 添加自定义主题按钮
        customThemeButton = new Button("导入自定义主题");
        customThemeButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择CSS主题文件");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSS文件", "*.css")
            );
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                try {
                    // 复制CSS文件到主题目录
                    File themesDir = new File("themes");
                    if (!themesDir.exists()) {
                        themesDir.mkdir();
                    }
                    java.nio.file.Files.copy(
                        file.toPath(),
                        new File(themesDir, file.getName()).toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                    // 应用新主题
                    String cssUrl = file.toURI().toURL().toExternalForm();
                    getScene().getStylesheets().clear();
                    getScene().getStylesheets().add(cssUrl);
                    showSuccessAlert("自定义主题已导入");
                } catch (Exception ex) {
                    showErrorAlert("导入主题失败: " + ex.getMessage());
                }
            }
        });

        // 添加备份和恢复按钮
        HBox backupBox = new HBox(10);
        backupBox.setAlignment(Pos.CENTER);
        
        backupButton = new Button("备份设置");
        backupButton.setOnAction(e -> {
            try {
                File backupDir = new File("backups");
                if (!backupDir.exists()) {
                    backupDir.mkdir();
                }
                String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
                File backupFile = new File(backupDir, "settings_" + timestamp + ".json");
                Settings settings = SettingsManager.loadSettings();
                try (Writer writer = Files.newBufferedWriter(backupFile.toPath())) {
                    new GsonBuilder().setPrettyPrinting().create().toJson(settings, writer);
                }
                showSuccessAlert("设置已备份到: " + backupFile.getPath());
            } catch (IOException ex) {
                showErrorAlert("备份设置失败: " + ex.getMessage());
            }
        });
        
        restoreButton = new Button("恢复设置");
        restoreButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择备份文件");
            fileChooser.setInitialDirectory(new File("backups"));
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("备份文件", "*.json")
            );
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                try {
                    Settings restoredSettings;
                    try (Reader reader = Files.newBufferedReader(file.toPath())) {
                        restoredSettings = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, Settings.class);
                    }
                    SettingsManager.saveSettings(restoredSettings);
                    showSuccessAlert("设置已恢复，请重启应用以应用更改");
                } catch (IOException ex) {
                    showErrorAlert("恢复设置失败: " + ex.getMessage());
                }
            }
        });
        
        backupBox.getChildren().addAll(backupButton, restoreButton);

        // 添加性能监控和日志查看按钮
        HBox monitorBox = new HBox(10);
        monitorBox.setAlignment(Pos.CENTER);
        
        monitorButton = new Button("性能监控");
        monitorButton.setOnAction(e -> {
            if (monitorStage == null) {
                monitorStage = new Stage();
                monitorStage.setTitle("启动器性能监控");
                
                VBox monitorContent = new VBox(10);
                monitorContent.setPadding(new Insets(15));
                
                Label cpuLabel = new Label("CPU使用率: 0%");
                Label memoryLabel = new Label("内存使用: 0MB");
                Label diskLabel = new Label("磁盘使用: 0MB");
                
                javafx.scene.chart.LineChart<Number, Number> chart = createPerformanceChart();
                
                monitorContent.getChildren().addAll(
                    cpuLabel, memoryLabel, diskLabel, chart
                );
                
                Scene monitorScene = new Scene(monitorContent, 600, 400);
                monitorStage.setScene(monitorScene);
                
                // 启动性能数据更新任务
                java.util.Timer timer = new java.util.Timer(true);
                timer.scheduleAtFixedRate(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            // 更新性能数据
                            updatePerformanceData(cpuLabel, memoryLabel, diskLabel, chart);
                        });
                    }
                }, 0, 1000);
                
                monitorStage.setOnCloseRequest(event -> timer.cancel());
            }
            monitorStage.show();
        });
        
        logViewerButton = new Button("日志查看");
        logViewerButton.setOnAction(e -> {
            if (logViewerStage == null) {
                logViewerStage = new Stage();
                logViewerStage.setTitle("启动器日志查看");
                
                VBox logContent = new VBox(10);
                logContent.setPadding(new Insets(15));
                
                TextArea logArea = new TextArea();
                logArea.setEditable(false);
                logArea.setWrapText(true);
                logArea.setPrefRowCount(20);
                
                Button refreshLogButton = new Button("刷新日志");
                refreshLogButton.setOnAction(event -> {
                    loadLatestLog(logArea);
                });
                
                logContent.getChildren().addAll(refreshLogButton, logArea);
                
                Scene logScene = new Scene(logContent, 600, 400);
                logViewerStage.setScene(logScene);
                
                // 初始加载日志
                loadLatestLog(logArea);
            }
            logViewerStage.show();
        });
        
        monitorBox.getChildren().addAll(monitorButton, logViewerButton);

        // 添加剩余组件到布局
        contentBox.getChildren().addAll(
            navigationPositionLabel, navigationPositionBox,
            styleLabel, styleComboBox,
            customThemeButton,
            backgroundLabel, backgroundBox,  // 添加背景设置
            minecraftPathLabel, minecraftPathBox,
            backupBox,
            monitorBox,  // 添加性能监控和日志查看按钮
            saveButton
        );

        // 内容已经设置在initializeLayout中，不需要重复设置
        // setContent(contentBox);
        // 设置保存按钮事件
        saveButton.setOnAction(event -> saveSettings());
    }

    private void saveSettings() {
        String javaPath = javaPathComboBox.getValue();
        String maxMemory = maxMemoryField.getText();
        String jvmArgs = jvmArgsField.getText();
        String gameArgs = gameArgsField.getText();
        String theme = themeComboBox.getValue();
        String minecraftPath = minecraftPathField.getText();
        String backgroundImage = backgroundPathField.getText();  // 获取背景图片路径
        
        // 创建配置对象
        Settings settings = new Settings();
        
        // 设置各项配置值
        settings.setJavaPath(javaPath);
        settings.setMaxMemory(Integer.parseInt(maxMemory));
        settings.setJvmArgs(jvmArgs);
        settings.setGameArgs(gameArgs);
        settings.setTheme(theme);
        settings.setMinecraftPath(minecraftPath);
        settings.setBackgroundImage(backgroundImage);  // 设置背景图片路径

        try {
            SettingsManager.saveSettings(settings);
            updateThemeAndBackground();  // 保存后更新主题和背景
            showSuccessAlert("设置已保存");
        } catch (IOException e) {
            showErrorAlert("保存设置失败: " + e.getMessage());
        }
    }

    public ComboBox<String> getJavaPathComboBox() {
        return javaPathComboBox;
    }

    public TextField getMaxMemoryField() {
        return maxMemoryField;
    }

    public ComboBox<String> getThemeComboBox() {
        return themeComboBox;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public TextField getJvmArgsField() {
        return jvmArgsField;
    }

    public TextField getGameArgsField() {
        return gameArgsField;
    }

    private void addPresetButton(String text, String args, String tooltip) {
        Button button = new Button(text);
        button.setTooltip(new Tooltip(tooltip));
        button.setOnAction(e -> jvmArgsField.setText(args));
        button.getStyleClass().add("preset-button");
        jvmPresetPane.getChildren().add(button);
    }

    private void addGamePresetButton(String text, String args, String tooltip) {
        Button button = new Button(text);
        button.setTooltip(new Tooltip(tooltip));
        button.setOnAction(e -> gameArgsField.setText(args));
        button.getStyleClass().add("preset-button");
        gamePresetPane.getChildren().add(button);
    }
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private ComboBox<String> styleComboBox;
    private Button customThemeButton;
    private Button backupButton;
    private Button restoreButton;
    private Button monitorButton;
    private Button logViewerButton;
    private Stage monitorStage;
    private Stage logViewerStage;
    
    private void updateThemeAndBackground() {
        try {
            Settings settings = SettingsManager.loadSettings();
            // 更新主题
            String theme = settings.getTheme();
            if (theme != null) {
                getScene().getRoot().getStyleClass().removeAll("theme-light", "theme-dark", "theme-custom");
                switch (theme) {
                    case "浅色主题" -> getScene().getRoot().getStyleClass().add("theme-light");
                    case "深色主题" -> getScene().getRoot().getStyleClass().add("theme-dark");
                    case "自定义主题" -> getScene().getRoot().getStyleClass().add("theme-custom");
                }
            }
            
            // 更新样式表
            String style = styleComboBox.getValue();
            if (style != null) {
                getScene().getStylesheets().clear();
                if (style.equals("Apple Design")) {
                    URL styleUrl = getClass().getResource("/AppleDesign.css");
                    if (styleUrl != null) {
                        getScene().getStylesheets().add(styleUrl.toExternalForm());
                    } else {
                        showErrorAlert("无法加载样式表文件：/AppleDesign.css");
                    }
                } else {
                    URL styleUrl = getClass().getResource("/styles/main.css");
                    if (styleUrl != null) {
                        getScene().getStylesheets().add(styleUrl.toExternalForm());
                    } else {
                        showErrorAlert("无法加载样式表文件：/styles/main.css");
                    }
                }
            }
            
            // 更新背景图片
            String backgroundImage = settings.getBackgroundImage();
            if (backgroundImage != null && !backgroundImage.isEmpty()) {
                updateBackground(backgroundImage);
            }
        } catch (IOException e) {
            showErrorAlert("无法加载设置: " + e.getMessage());
        }
    }

    private void updateBackground(String path) {
        if (path != null && !path.isEmpty()) {
            String styleString = String.format("-fx-background-image: url('file:%s'); " +
                                             "-fx-background-size: cover; " +
                                             "-fx-background-position: center;", path);
            setStyle(styleString);
        } else {
            setStyle("");
        }
    }

    private javafx.scene.chart.LineChart<Number, Number> createPerformanceChart() {
        javafx.scene.chart.NumberAxis xAxis = new javafx.scene.chart.NumberAxis();
        javafx.scene.chart.NumberAxis yAxis = new javafx.scene.chart.NumberAxis();
        xAxis.setLabel("时间(秒)");
        yAxis.setLabel("使用率(%)");
        
        javafx.scene.chart.LineChart<Number, Number> lineChart = new javafx.scene.chart.LineChart<>(xAxis, yAxis);
        lineChart.setTitle("性能监控");
        lineChart.setAnimated(false);
        
        // 添加CPU和内存数据系列
        javafx.scene.chart.XYChart.Series<Number, Number> cpuSeries = new javafx.scene.chart.XYChart.Series<>();
        cpuSeries.setName("CPU使用率");
        javafx.scene.chart.XYChart.Series<Number, Number> memorySeries = new javafx.scene.chart.XYChart.Series<>();
        memorySeries.setName("内存使用率");
        
        lineChart.getData().addAll(cpuSeries, memorySeries);
        return lineChart;
    }
    
    private Label cpuLabel;
    private Label memoryLabel;
    private Label diskLabel;
    private javafx.scene.chart.LineChart<Number, Number> chart;

    private void updatePerformanceData(Label cpuLabel, Label memoryLabel, Label diskLabel, 
                                      javafx.scene.chart.LineChart<Number, Number> chart) {
        // 获取系统性能数据
        com.sun.management.OperatingSystemMXBean osBean = 
            (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        
        // 更新CPU使用率
        double cpuLoad = osBean.getSystemCpuLoad() * 100;
        cpuLabel.setText(String.format("CPU使用率: %.1f%%", cpuLoad));
        
        // 更新内存使用情况
        long totalMemory = osBean.getTotalPhysicalMemorySize();
        long freeMemory = osBean.getFreePhysicalMemorySize();
        long usedMemory = totalMemory - freeMemory;
        memoryLabel.setText(String.format("内存使用: %.1fGB", usedMemory / (1024.0 * 1024 * 1024)));
        
        // 更新磁盘使用情况
        File gameDir = new File(minecraftPathField.getText());
        if (gameDir.exists()) {
            long totalSpace = gameDir.getTotalSpace();
            long freeSpace = gameDir.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            diskLabel.setText(String.format("磁盘使用: %.1fGB", usedSpace / (1024.0 * 1024 * 1024)));
        }
        
        // 更新图表数据
        if (!chart.getData().isEmpty()) {
            javafx.scene.chart.XYChart.Series<Number, Number> cpuSeries = chart.getData().get(0);
            javafx.scene.chart.XYChart.Series<Number, Number> memorySeries = chart.getData().get(1);
            
            // 添加新数据点
            int timePoint = cpuSeries.getData().size();
            cpuSeries.getData().add(new javafx.scene.chart.XYChart.Data<>(timePoint, cpuLoad));
            memorySeries.getData().add(new javafx.scene.chart.XYChart.Data<>(timePoint, 
                (double) usedMemory / totalMemory * 100));
            
            // 保持最近30个数据点
            if (cpuSeries.getData().size() > 30) {
                cpuSeries.getData().remove(0);
                memorySeries.getData().remove(0);
            }
        }
        
        // 更新类成员变量
        this.cpuLabel = cpuLabel;
        this.memoryLabel = memoryLabel;
        this.diskLabel = diskLabel;
        this.chart = chart;
    }
    
    private void loadLatestLog(TextArea logArea) {
        try {
            File logFile = new File("logs/latest.log");
            if (logFile.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(logFile.toPath()));
                logArea.setText(content);
                logArea.positionCaret(content.length()); // 滚动到底部
            } else {
                logArea.setText("未找到日志文件");
            }
        } catch (IOException e) {
            logArea.setText("读取日志失败: " + e.getMessage());
        }
    }
}