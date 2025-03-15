package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LaunchConfigView extends VBox {
    private Slider memorySlider;
    private TextField memoryField;
    private TextField jvmArgsField;
    private TextField gameArgsField;
    private Button saveButton;
    private FlowPane jvmPresetPane;
    private FlowPane gamePresetPane;

    public LaunchConfigView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // 标题
        Label titleLabel = new Label("启动配置");
        titleLabel.setFont(new Font(20));

        // 配置表单
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        // 内存配置
        Label memoryLabel = new Label("内存分配:");
        HBox memoryBox = new HBox(10);
        memorySlider = new Slider(2, 32, 4);
        memorySlider.setShowTickLabels(true);
        memorySlider.setShowTickMarks(true);
        memorySlider.setMajorTickUnit(2);
        memorySlider.setBlockIncrement(1);
        memoryField = new TextField("4");
        memoryField.setPrefWidth(60);
        Label unitLabel = new Label("GB");
        memoryBox.getChildren().addAll(memorySlider, memoryField, unitLabel);
        
        // 内存滑块和输入框联动
        memorySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            memoryField.setText(String.format("%.0f", newVal));
        });
        
        memoryField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                memoryField.setText(newVal.replaceAll("[^\\d]", ""));
            } else {
                try {
                    int value = Integer.parseInt(newVal);
                    if (value >= 2 && value <= 32) {
                        memorySlider.setValue(value);
                    }
                } catch (NumberFormatException e) {}
            }
        });
        
        formGrid.add(memoryLabel, 0, 0);
        formGrid.add(memoryBox, 1, 0);

        // JVM参数
        Label jvmArgsLabel = new Label("JVM参数:");
        jvmArgsField = new TextField();
        jvmArgsField.setPromptText("-XX:+UseG1GC");
        jvmArgsField.setTooltip(new Tooltip("输入JVM启动参数，多个参数用空格分隔"));
        formGrid.add(jvmArgsLabel, 0, 1);
        formGrid.add(jvmArgsField, 1, 1);

        // JVM预设参数按钮
        jvmPresetPane = new FlowPane();
        jvmPresetPane.setHgap(10);
        jvmPresetPane.setVgap(10);
        jvmPresetPane.setPadding(new Insets(10));

        addPresetButton("G1垃圾回收", "-XX:+UseG1GC", "使用G1垃圾回收器，适合大内存应用");
        addPresetButton("并行GC", "-XX:+UseParallelGC", "使用并行垃圾回收器，注重吞吐量");
        addPresetButton("内存优化", "-XX:+AggressiveOpts -XX:+UseFastAccessorMethods", "优化内存访问性能");
        addPresetButton("调试模式", "-XX:+HeapDumpOnOutOfMemoryError", "发生内存溢出时自动导出堆转储文件");

        formGrid.add(jvmPresetPane, 1, 2);
        GridPane.setColumnSpan(jvmPresetPane, 2);

        // 添加参数验证
        jvmArgsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidJvmArgs(newValue)) {
                jvmArgsField.setStyle("-fx-border-color: red;");
                jvmArgsField.setTooltip(new Tooltip("参数格式不正确，请检查参数格式"));
            } else {
                jvmArgsField.setStyle("");
                jvmArgsField.setTooltip(new Tooltip("输入JVM启动参数，多个参数用空格分隔"));
            }
        });

        // 游戏参数
        Label gameArgsLabel = new Label("游戏参数:");
        gameArgsField = new TextField();
        gameArgsField.setPromptText("--width 1920 --height 1080");
        formGrid.add(gameArgsLabel, 0, 3);
        formGrid.add(gameArgsField, 1, 3);

        // 游戏参数预设按钮
        gamePresetPane = new FlowPane();
        gamePresetPane.setHgap(10);
        gamePresetPane.setVgap(10);
        gamePresetPane.setPadding(new Insets(10));

        addGamePresetButton("1080p", "--width 1920 --height 1080", "全高清分辨率");
        addGamePresetButton("全屏", "--fullscreen", "全屏模式启动游戏");
        addGamePresetButton("窗口化", "--windowed", "窗口化模式启动游戏");

        formGrid.add(gamePresetPane, 1, 4);
        GridPane.setColumnSpan(gamePresetPane, 2);

        // 保存按钮
        saveButton = new Button("保存配置");
        saveButton.setPrefWidth(150);

        // 添加所有组件到主布局
        getChildren().addAll(titleLabel, formGrid, saveButton);
    }

    public int getMemoryValue() {
        return Integer.parseInt(memoryField.getText());
    }

    public TextField getJvmArgsField() {
        return jvmArgsField;
    }

    public TextField getGameArgsField() {
        return gameArgsField;
    }

    public Button getSaveButton() {
        return saveButton;
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

    private boolean isValidJvmArgs(String args) {
        if (args.trim().isEmpty()) return true;
        String[] params = args.split(" ");
        for (String param : params) {
            if (!param.startsWith("-X") && !param.startsWith("-D")) {
                return false;
            }
        }
        return true;
    }
}