
package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GameView extends VBox {
    
    public GameView() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        
        // 创建子标签页
        TabPane gameTabPane = new TabPane();
        gameTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // 创建版本列表标签页
        Tab versionListTab = new Tab("版本列表");
        versionListTab.setContent(new VersionListView());
        
        // 创建版本管理标签页
        Tab versionManagementTab = new Tab("版本管理");
        versionManagementTab.setContent(new VersionManagementView());
        
        // 创建趣味功能标签页
        Tab moreFeaturesTab = new Tab("趣味功能");
        moreFeaturesTab.setContent(new MoreFeaturesView());
        
        // 将标签页添加到TabPane
        gameTabPane.getTabs().addAll(versionListTab, versionManagementTab, moreFeaturesTab);
        
        // 将TabPane添加到GameView
        getChildren().add(gameTabPane);
        
        // 设置TabPane填充整个区域
        VBox.setVgrow(gameTabPane, Priority.ALWAYS);
    }
}