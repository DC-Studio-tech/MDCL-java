package org.mcdcl.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.mcdcl.config.Settings;
import org.mcdcl.config.SettingsManager;
import org.mcdcl.version.VersionManager;
import org.mcdcl.version.VersionDownloader;
import org.mcdcl.exception.MinecraftDirectoryException;
import org.to2mbn.jmccc.mcdownloader.RemoteVersionList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;

public class VersionView extends VBox {
    private ListView<String> versionList;
    private Button refreshButton;
    private Button downloadButton;
    private Button selectButton;
    private TextField minecraftPathField;
    private Button chooseMinecraftPathButton;
    private Button savePathButton;

    public VersionView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // 标题
        Label titleLabel = new Label("游戏版本管理");
        titleLabel.setFont(new Font(20));

        // 版本列表和选择按钮区域
        javafx.scene.layout.HBox listAndSelectBox = new javafx.scene.layout.HBox(10);
        listAndSelectBox.setAlignment(Pos.CENTER);
        
        // 版本列表
        versionList = new ListView<>();
        versionList.setPrefHeight(400);
        versionList.setPrefWidth(300);
        
        // 选择版本按钮（放在列表旁边）
        VBox selectButtonBox = new VBox();
        selectButtonBox.setAlignment(Pos.CENTER);
        selectButtonBox.setPadding(new Insets(0, 0, 0, 10));
        
        selectButton = new Button("选择版本");
        selectButton.setPrefWidth(100);
        selectButton.setPrefHeight(40);
        selectButton.getStyleClass().add("primary-button");

        // 添加选择版本按钮的事件处理
        selectButton.setOnAction(e -> {
            String selectedVersion = versionList.getSelectionModel().getSelectedItem();
            if (selectedVersion != null && !selectedVersion.isEmpty()) {
                try {
                    Settings settings = SettingsManager.loadSettings();
                    settings.setSelectedVersion(selectedVersion);
                    SettingsManager.saveSettings(settings);
                    showSuccessAlert("版本 " + selectedVersion + " 已选择并保存");
                } catch (IOException ex) {
                    showErrorAlert("保存设置失败: " + ex.getMessage());
                }
            } else {
                showErrorAlert("请先从列表中选择一个版本");
            }
        });
        
        selectButtonBox.getChildren().add(selectButton);
        listAndSelectBox.getChildren().addAll(versionList, selectButtonBox);
        
        // 其他按钮区域
        javafx.scene.layout.HBox otherButtonsBox = new javafx.scene.layout.HBox(10);
        otherButtonsBox.setAlignment(Pos.CENTER);
        
        // 修改刷新按钮
        refreshButton = new Button("刷新版本列表");
        FontIcon refreshIcon = new FontIcon(MaterialDesignR.REFRESH);
        refreshIcon.setIconSize(16);
        refreshButton.setGraphic(refreshIcon);
        
        // 修改下载按钮
        downloadButton = new Button("下载新版本");
        FontIcon downloadIcon = new FontIcon(MaterialDesignD.DOWNLOAD);
        downloadIcon.setIconSize(16);
        downloadButton.setGraphic(downloadIcon);
        
        // 修改选择按钮
        selectButton = new Button("选择版本");
        FontIcon selectIcon = new FontIcon(MaterialDesignF.FOLDER_OPEN);
        selectIcon.setIconSize(16);
        selectButton.setGraphic(selectIcon);
        
        refreshButton.setPrefWidth(150);
        
        downloadButton = new Button("下载新版本");
        downloadButton.setPrefWidth(150);
        downloadButton.setOnAction(e -> {
            showDownloadVersionDialog();
        });
        
        otherButtonsBox.getChildren().addAll(refreshButton, downloadButton);
        
        // Minecraft路径设置区域
        HBox minecraftPathBox = new HBox(10);
        minecraftPathBox.setAlignment(Pos.CENTER);
        minecraftPathBox.setPadding(new Insets(10, 0, 0, 0));
        
        Label minecraftPathLabel = new Label("Minecraft目录:");
        
        // 尝试加载已保存的设置
        Settings savedSettings = null;
        try {
            savedSettings = SettingsManager.loadSettings();
        } catch (IOException e) {
            System.err.println("无法加载设置: " + e.getMessage());
            // 加载失败时使用默认设置
            savedSettings = new Settings();
        }
        
        minecraftPathField = new TextField(savedSettings.getMinecraftPath());
        minecraftPathField.setPromptText("请选择Minecraft游戏目录");
        minecraftPathField.setPrefWidth(240);
        
        chooseMinecraftPathButton = new Button("选择");
        savePathButton = new Button("保存路径");
        
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
        
        // 保存路径按钮事件
        savePathButton.setOnAction(e -> {
            String minecraftPath = minecraftPathField.getText();
            
            try {
                // 加载当前设置
                Settings settings = SettingsManager.loadSettings();
                // 更新Minecraft路径
                settings.setMinecraftPath(minecraftPath);
                // 保存设置
                SettingsManager.saveSettings(settings);
                // 显示保存成功提示
                showSuccessAlert("Minecraft路径已保存");
            } catch (IOException ex) {
                // 显示保存失败错误
                showErrorAlert("保存设置失败: " + ex.getMessage());
            }
        });
        
        minecraftPathBox.getChildren().addAll(minecraftPathLabel, minecraftPathField, chooseMinecraftPathButton, savePathButton);
        
        // 添加所有组件到主布局
        getChildren().addAll(titleLabel, listAndSelectBox, minecraftPathBox, otherButtonsBox);
        
        // 添加刷新按钮事件
        refreshButton.setOnAction(e -> {
            loadVersions();
        });
        
        // 初始加载版本列表
        loadVersions();
    }

    public ListView<String> getVersionList() {
        return versionList;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getDownloadButton() {
        return downloadButton;
    }

    public Button getSelectButton() {
        return selectButton;
    }
    
    public TextField getMinecraftPathField() {
        return minecraftPathField;
    }
    
    public Button getChooseMinecraftPathButton() {
        return chooseMinecraftPathButton;
    }
    
    public Button getSavePathButton() {
        return savePathButton;
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
    
    /**
     * 显示下载版本对话框
     */
    private void showDownloadVersionDialog() {
        // 创建版本下载器
        org.mcdcl.version.VersionDownloader downloader = new org.mcdcl.version.VersionDownloader();
        
        // 显示加载中提示
        Alert loadingAlert = new Alert(AlertType.INFORMATION);
        loadingAlert.setTitle("正在加载");
        loadingAlert.setHeaderText(null);
        loadingAlert.setContentText("正在获取Minecraft版本列表，请稍候...");
        loadingAlert.show();
        
        // 获取远程版本列表
        downloader.fetchVersionList(new org.mcdcl.version.VersionDownloader.VersionListCallback() {
            @Override
            public void onVersionListFetched(org.to2mbn.jmccc.mcdownloader.RemoteVersionList versionList) {
                // 关闭加载提示
                loadingAlert.close();
                
                // 显示版本选择对话框
                VersionSelectDialog dialog = new VersionSelectDialog(versionList);
                dialog.showAndWait().ifPresent(selectedVersion -> {
                    if (selectedVersion != null && !selectedVersion.isEmpty()) {
                        // 下载选择的版本
                        downloader.downloadVersion(selectedVersion, new org.mcdcl.version.VersionDownloader.VersionDownloadCallback() {
                            @Override
                            public void onVersionDownloaded(String versionId) {
                                // 下载完成后刷新版本列表
                                loadVersions();
                                // 显示成功提示
                                showSuccessAlert("Minecraft版本 " + versionId + " 已成功下载");
                            }
                        });
                    }
                });
            }
        });
    }
    
    /**
     * 加载Minecraft版本列表
     */
    private void loadVersions() {
        try {
            // 清空当前列表
            versionList.getItems().clear();
            
            // 获取当前设置的Minecraft路径
            String minecraftPath = minecraftPathField.getText();
            if (minecraftPath == null || minecraftPath.isEmpty()) {
                Settings settings = SettingsManager.loadSettings();
                minecraftPath = settings.getMinecraftPath();
                // 更新路径字段
                minecraftPathField.setText(minecraftPath);
            }
            
            // 检查Minecraft目录是否存在
            File minecraftFolder = new File(minecraftPath);
            if (!minecraftFolder.exists() || !minecraftFolder.isDirectory()) {
                showErrorAlert("Minecraft目录不存在或不是有效目录: " + minecraftPath);
                return;
            }
            
            // 获取版本列表
            List<String> versions = VersionManager.getAvailableVersions();
            
            if (versions.isEmpty()) {
                showErrorAlert("未找到任何Minecraft版本，请检查Minecraft目录设置");
            } else {
                // 添加到列表视图
                versionList.getItems().addAll(versions);
                // 选择第一个版本
                if (!versions.isEmpty()) {
                    versionList.getSelectionModel().select(0);
                }
            }
        } catch (IOException | MinecraftDirectoryException e) {
            showErrorAlert("加载版本列表失败: " + e.getMessage());
        }
    }
}