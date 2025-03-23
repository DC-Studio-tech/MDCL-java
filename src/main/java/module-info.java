module org.mcdcl {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires java.desktop;
    requires java.prefs;  // 添加 java.prefs 模块依赖
    requires jmccc;
    requires jmccc.mcdownloader;
    requires jmccc.microsoft.authenticator;
    requires jmccc.mojang.api;
    requires jmccc.cli;
    
    exports org.mcdcl;
    exports org.mcdcl.ui;
    exports org.mcdcl.config;
    exports org.mcdcl.controller;
    
    opens org.mcdcl to javafx.graphics;
    opens org.mcdcl.ui to javafx.fxml;
    opens org.mcdcl.controller to javafx.fxml;
}