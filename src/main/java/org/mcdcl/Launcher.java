package org.mcdcl;

public class Launcher {
    public static void main(String[] args) {
        // 设置JavaFX模块路径，解决JavaFX运行时组件缺失问题
        System.setProperty("javafx.verbose", "true");
        System.setProperty("javafx.runtime.path", System.getProperty("java.home") + "/lib");
        Main.main(args);
    }
}