package org.mcdcl.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.to2mbn.jmccc.mcdownloader.RemoteVersion;
import org.to2mbn.jmccc.mcdownloader.RemoteVersionList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

/**
 * Minecraft版本选择对话框
 */
public class VersionSelectDialog extends Dialog<String> {
    
    private ListView<String> versionList;
    private ObservableList<String> versionItems;
    private RemoteVersionList remoteVersionList;
    private ToggleGroup versionTypeGroup;
    private RadioButton releaseButton;
    private RadioButton snapshotButton;
    private RadioButton allButton;
    
    /**
     * 创建一个新的版本选择对话框
     * 
     * @param remoteVersionList 远程版本列表
     */
    public VersionSelectDialog(RemoteVersionList remoteVersionList) {
        this.remoteVersionList = remoteVersionList;
        
        // 设置对话框属性
        setTitle("选择要下载的Minecraft版本");
        setHeaderText("请选择要下载的Minecraft版本");
        initModality(Modality.APPLICATION_MODAL);
        
        // 创建版本列表
        versionItems = FXCollections.observableArrayList();
        versionList = new ListView<>(versionItems);
        versionList.setPrefHeight(300);
        versionList.setPrefWidth(400);
        
        // 创建版本类型选择按钮
        versionTypeGroup = new ToggleGroup();
        
        releaseButton = new RadioButton("正式版");
        releaseButton.setToggleGroup(versionTypeGroup);
        releaseButton.setSelected(true);
        releaseButton.setOnAction(e -> updateVersionList("release"));
        
        snapshotButton = new RadioButton("快照版");
        snapshotButton.setToggleGroup(versionTypeGroup);
        snapshotButton.setOnAction(e -> updateVersionList("snapshot"));
        
        allButton = new RadioButton("全部版本");
        allButton.setToggleGroup(versionTypeGroup);
        allButton.setOnAction(e -> updateVersionList("all"));
        
        // 创建版本类型选择布局
        HBox versionTypeBox = new HBox(15);
        versionTypeBox.setAlignment(Pos.CENTER);
        versionTypeBox.getChildren().addAll(releaseButton, snapshotButton, allButton);
        
        // 创建主布局
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
                new Label("版本类型:"),
                versionTypeBox,
                new Label("可用版本:"),
                versionList
        );
        
        // 设置对话框内容
        getDialogPane().setContent(content);
        
        // 添加按钮
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // 设置结果转换器
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return versionList.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        
        // 禁用确定按钮，直到选择了一个版本
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        versionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null);
        });
        
        // 初始加载版本列表
        updateVersionList("release");
    }
    
    /**
     * 根据类型更新版本列表
     * 
     * @param type 版本类型 (release, snapshot, all)
     */
    private void updateVersionList(String type) {
        versionItems.clear();
        
        if (remoteVersionList == null) {
            return;
        }
        
        Map<String, RemoteVersion> versions = remoteVersionList.getVersions();
        List<String> filteredVersions = new ArrayList<>();
        
        for (RemoteVersion version : versions.values()) {
            if (type.equals("all") || 
                (type.equals("release") && "release".equals(version.getType())) ||
                (type.equals("snapshot") && "snapshot".equals(version.getType()))) {
                filteredVersions.add(version.getVersion());
            }
        }
        
        versionItems.addAll(filteredVersions);
        
        // 如果有版本，选择第一个
        if (!versionItems.isEmpty()) {
            versionList.getSelectionModel().select(0);
        }
    }
}