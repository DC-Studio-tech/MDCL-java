package org.mcdcl.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 场景管理器，用于处理不同界面之间的切换
 */
public class SceneManager {
    private static Stage primaryStage;
    
    /**
     * 设置主舞台
     * 
     * @param stage JavaFX主舞台
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
    
    /**
     * 加载登录场景
     * 
     * @throws IOException 如果FXML文件加载失败
     */
    public static void loadLoginScene() throws IOException {
        loadScene("/fxml/login.fxml", "登录");
    }
    
    /**
     * 加载主界面场景
     * 
     * @throws IOException 如果FXML文件加载失败
     */
    public static void loadMainScene() {
        try {
            loadScene("/fxml/main.fxml", "MDCL启动器");
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常，可能显示错误对话框
        }
    }
    
    /**
     * 加载注册场景
     * 
     * @throws IOException 如果FXML文件加载失败
     */
    public static void loadRegisterScene() throws IOException {
        loadScene("/fxml/register.fxml", "注册");
    }
    
    /**
     * 加载设置场景
     * 
     * @throws IOException 如果FXML文件加载失败
     */
    public static void loadSettingsScene() throws IOException {
        loadScene("/fxml/settings.fxml", "设置");
    }
    
    /**
     * 通用场景加载方法
     * 
     * @param fxmlPath FXML文件路径
     * @param title 窗口标题
     * @throws IOException 如果FXML文件加载失败
     */
    private static void loadScene(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}