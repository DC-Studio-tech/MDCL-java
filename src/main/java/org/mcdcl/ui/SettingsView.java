package org.mcdcl.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.mcdcl.config.Settings;
import org.mcdcl.config.SettingsManager;
import org.mcdcl.util.JavaFinder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class SettingsView extends VBox {
    private ComboBox<String> javaPathComboBox;
    private Button chooseJavaButton;
    private Slider memorySlider;
    private TextField maxMemoryField;
    private TextField jvmArgsField;
    private TextField gameArgsField;
    private ComboBox<String> themeComboBox;
    private Button saveButton;
    private FlowPane jvmPresetPane;
    private FlowPane gamePresetPane;

    public SettingsView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // 标题
        Label titleLabel = new Label("常规设置");
        titleLabel.getStyleClass().add("section-title");

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
        if (!javaInstallations.isEmpty()) {
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
        memorySlider = new Slider(2, 32, 4);
        memorySlider.setShowTickLabels(true);
        memorySlider.setShowTickMarks(true);
        memorySlider.setMajorTickUnit(2);
        memorySlider.setBlockIncrement(1);
        maxMemoryField = new TextField("4");
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
        jvmArgsField = new TextField();
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
        gameArgsField = new TextField();
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
        themeComboBox.getItems().addAll("默认主题", "暗色主题", "亮色主题");
        themeComboBox.setValue("默认主题");
        themeComboBox.setPrefWidth(300);

        // 保存按钮
        saveButton = new Button("保存设置");
        saveButton.getStyleClass().add("save-button");
        saveButton.setMaxWidth(200);

        // 添加所有组件到布局
        getChildren().addAll(
            titleLabel,
            javaPathLabel, javaPathBox,
            maxMemoryLabel, memoryBox,
            jvmArgsLabel, jvmArgsField,
            jvmPresetPane,
            gameArgsLabel, gameArgsField,
            gamePresetPane,
            themeLabel, themeComboBox,
            saveButton
        );

        // 设置保存按钮事件
        saveButton.setOnAction(event -> saveSettings());
    }

    private void saveSettings() {
        String javaPath = javaPathComboBox.getValue();
        String maxMemory = maxMemoryField.getText();
        String jvmArgs = jvmArgsField.getText();
        String gameArgs = gameArgsField.getText();
        String theme = themeComboBox.getValue();
        
        // 创建配置对象
        Settings settings = new Settings();
        
        // 设置各项配置值
        settings.setJavaPath(javaPath);
        settings.setMaxMemory(Integer.parseInt(maxMemory));
        settings.setJvmArgs(jvmArgs);
        settings.setGameArgs(gameArgs);
        settings.setTheme(theme);

        try {
            // 保存配置到文件
            SettingsManager.saveSettings(settings);
            // 显示保存成功提示
            showSuccessAlert("设置已保存");
        } catch (IOException e) {
            // 显示保存失败错误
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
}