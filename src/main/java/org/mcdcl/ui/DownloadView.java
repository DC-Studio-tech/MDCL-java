package org.mcdcl.ui;

import java.util.List;

import org.mcdcl.util.VersionManager;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DownloadView extends VBox {
    private TabPane tabPane;

    public DownloadView() {
        // 初始化布局
        setPadding(new Insets(40, 10, 10, 10));  // 增加顶部边距，为返回按钮留出空间
        setSpacing(10);

        // 创建选项卡面板
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add("download-tabs");  // 添加自定义样式类

        // 创建各个选项卡
        Tab gameTab = new Tab("游戏");
        gameTab.setContent(new GameDownloadTab());

        Tab modpackTab = new Tab("整合包");
        modpackTab.setContent(new ModpackDownloadTab());

        Tab modTab = new Tab("模组");
        modTab.setContent(new ModDownloadTab());

        Tab resourceTab = new Tab("资源包");
        resourceTab.setContent(new ResourcePackDownloadTab());

        // 添加选项卡到面板
        tabPane.getTabs().addAll(gameTab, modpackTab, modTab, resourceTab);

        // 将选项卡面板添加到布局中
        getChildren().add(tabPane);
    }
}

// 游戏下载选项卡
class GameDownloadTab extends VBox {
    private ProgressBar progressBar;
    private Label progressLabel;
    private Button downloadButton;

    public GameDownloadTab() {
        setPadding(new Insets(10));
        setSpacing(10);

        Label titleLabel = new Label("下载新版本");
        titleLabel.getStyleClass().add("section-title");

        // 添加版本类型选择
        ToggleGroup versionTypeGroup = new ToggleGroup();
        HBox versionTypeBox = new HBox(10);
        versionTypeBox.setAlignment(Pos.CENTER_LEFT);

        RadioButton releaseButton = new RadioButton("正式版");
        releaseButton.setToggleGroup(versionTypeGroup);
        releaseButton.setSelected(true);

        RadioButton snapshotButton = new RadioButton("快照版");
        snapshotButton.setToggleGroup(versionTypeGroup);

        RadioButton allButton = new RadioButton("全部版本");
        allButton.setToggleGroup(versionTypeGroup);

        versionTypeBox.getChildren().addAll(releaseButton, snapshotButton, allButton);

        ComboBox<String> versionComboBox = new ComboBox<>();
        versionComboBox.setPromptText("选择要下载的版本");
        versionComboBox.setPrefWidth(200);

        // 版本类型切换监听器
        versionTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            try {
                VersionManager.VersionType type;
                RadioButton selected = (RadioButton) newVal;
                if (selected == snapshotButton) {
                    type = VersionManager.VersionType.SNAPSHOT;
                } else if (selected == allButton) {
                    type = VersionManager.VersionType.ALL;
                } else {
                    type = VersionManager.VersionType.RELEASE;
                }
                List<String> versions = VersionManager.getAvailableVersions(type);
                versionComboBox.getItems().clear();
                versionComboBox.getItems().addAll(versions);
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("获取版本列表失败");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });

        // 创建进度条和标签
        progressBar = new ProgressBar(0);
        progressBar.setVisible(false);
        progressBar.setPrefWidth(200);
        
        progressLabel = new Label("");
        progressLabel.setVisible(false);

        Button downloadButton = new Button("下载");
        downloadButton.getStyleClass().add("download-button");
        downloadButton.setOnAction(e -> {
            String selectedVersion = versionComboBox.getValue();
            if (selectedVersion != null && !selectedVersion.isEmpty()) {
                // 禁用下载按钮
                downloadButton.setDisable(true);
                // 显示进度条
                progressBar.setVisible(true);
                progressLabel.setVisible(true);
                
                // 在新线程中执行下载
                new Thread(() -> {
                    try {
                        VersionManager.downloadVersion(selectedVersion, progress -> {
                            Platform.runLater(() -> {
                                progressBar.setProgress(progress);
                                progressLabel.setText(String.format("下载进度: %.1f%%", progress * 100));
                                
                                if (progress >= 1.0) {
                                    // 显示下载完成
                                    progressLabel.setText("下载完成！");
                                    
                                    // 创建淡出动画
                                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(2));
                                    fadeOut.setNode(progressBar);
                                    fadeOut.setFromValue(1.0);
                                    fadeOut.setToValue(0.0);
                                    
                                    FadeTransition labelFadeOut = new FadeTransition(Duration.seconds(2));
                                    labelFadeOut.setNode(progressLabel);
                                    labelFadeOut.setFromValue(1.0);
                                    labelFadeOut.setToValue(0.0);
                                    
                                    // 动画完成后隐藏进度条和标签
                                    fadeOut.setOnFinished(event -> {
                                        progressBar.setVisible(false);
                                        progressLabel.setVisible(false);
                                        downloadButton.setDisable(false);
                                    });
                                    
                                    // 启动动画
                                    fadeOut.play();
                                    labelFadeOut.play();
                                    
                                    // 显示完成提示
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    alert.setTitle("下载完成");
                                    alert.setHeaderText("版本 " + selectedVersion + " 下载完成");
                                    alert.showAndWait();
                                }
                            });
                        });
                    } catch (Exception ex) {
                        Platform.runLater(() -> {
                            downloadButton.setDisable(false);
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("下载失败");
                            alert.setHeaderText("版本下载失败");
                            alert.setContentText(ex.getMessage());
                            alert.showAndWait();
                        });
                    }
                }).start();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("警告");
                alert.setHeaderText("未选择版本");
                alert.setContentText("请先选择要下载的版本");
                alert.showAndWait();
            }
        });

        // 加载可用版本列表
        try {
            List<String> versions = VersionManager.getAvailableVersions(VersionManager.VersionType.RELEASE);
            versionComboBox.getItems().addAll(versions);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("获取版本列表失败");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        getChildren().addAll(titleLabel, versionTypeBox, versionComboBox, progressBar, progressLabel, downloadButton);
    }
}

// 整合包下载选项卡
class ModpackDownloadTab extends VBox {
    public ModpackDownloadTab() {
        setPadding(new Insets(10));
        setSpacing(10);
        // TODO: 添加整合包列表和下载功能
        Label titleLabel = new Label("整合包下载");
        titleLabel.getStyleClass().add("section-title");
        Label descriptionLabel = new Label("整合包下载功能尚未实现。");
        descriptionLabel.getStyleClass().add("section-description");
        getChildren().addAll(titleLabel, descriptionLabel);
        getStyleClass().add("section");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(10);
    }
}

// 模组下载选项卡
class ModDownloadTab extends VBox {
    public ModDownloadTab() {
        setPadding(new Insets(10));
        setSpacing(10);
        // TODO: 添加模组列表和下载功能
        Label titleLabel = new Label("模组下载");
        titleLabel.getStyleClass().add("section-title");
        Label descriptionLabel = new Label("模组下载功能尚未实现。");
        descriptionLabel.getStyleClass().add("section-description");
        getChildren().addAll(titleLabel, descriptionLabel);
        getStyleClass().add("section");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(10);
    }
}

// 资源包下载选项卡
class ResourcePackDownloadTab extends VBox {
    public ResourcePackDownloadTab() {
        setPadding(new Insets(10));
        setSpacing(10);
        // TODO: 添加资源包列表和下载功能
        Label titleLabel = new Label("资源包下载");
        titleLabel.getStyleClass().add("section-title");
        Label descriptionLabel = new Label("资源包下载功能尚未实现。");
        descriptionLabel.getStyleClass().add("section-description");
        getChildren().addAll(titleLabel, descriptionLabel);
        getStyleClass().add("section");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(10);
    }
}