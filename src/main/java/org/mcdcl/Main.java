package org.mcdcl;

import java.io.InputStream;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.lang.reflect.Method;

import org.mcdcl.ui.MainView;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MDCL v0.2beta");

        // 设置任务栏图标
        try {
            InputStream iconStream = getClass().getResourceAsStream("/images/icon.png");
            if (iconStream != null) {
                Image icon = new Image(iconStream);
                primaryStage.getIcons().add(icon);
                
                // 设置 macOS Dock 图标
                if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    try {
                        Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
                        Method getApplication = applicationClass.getMethod("getApplication");
                        Object application = getApplication.invoke(null);
                        Method setDockIconImage = applicationClass.getMethod("setDockIconImage", java.awt.Image.class);
                        setDockIconImage.invoke(application, SwingFXUtils.fromFXImage(icon, null));
                    } catch (Exception e) {
                        // 如果上述方法失败，尝试使用另一种方式
                        try {
                            Class<?> taskbarClass = Class.forName("java.awt.Taskbar");
                            Method getTaskbar = taskbarClass.getMethod("getTaskbar");
                            Object taskbar = getTaskbar.invoke(null);
                            Method setIconImage = taskbarClass.getMethod("setIconImage", java.awt.Image.class);
                            setIconImage.invoke(taskbar, SwingFXUtils.fromFXImage(icon, null));
                        } catch (Exception e2) {
                            System.err.println("设置 Dock 图标失败: " + e2.getMessage());
                        }
                    }
                }
            } else {
                System.err.println("无法找到图标文件: /images/icon.png");
            }
        } catch (Exception e) {
            System.err.println("无法加载图标: " + e.getMessage());
        }
        
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 1280, 800);
        
        try {
            String cssUrl = getClass().getResource("/styles/main.css").toExternalForm();
            scene.getStylesheets().add(cssUrl);
        } catch (Exception e) {
            System.err.println("无法加载样式表: " + e.getMessage());
        }
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
