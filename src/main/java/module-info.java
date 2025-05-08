module org.mcdcl {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.google.gson;
    requires org.json;
    requires jmccc;
    requires jmccc.mcdownloader;
    requires jmccc.microsoft.authenticator;
    requires jmccc.mojang.api;
    requires jmccc.cli;
    requires java.prefs;
    requires jdk.management;
    requires java.management;

    exports org.mcdcl;
    exports org.mcdcl.auth;
    exports org.mcdcl.config;
    exports org.mcdcl.controller;
    exports org.mcdcl.exception;
    exports org.mcdcl.launcher;
    exports org.mcdcl.model;

    exports org.mcdcl.service;
    exports org.mcdcl.ui;
    exports org.mcdcl.util;
    exports org.mcdcl.version;

    opens org.mcdcl to javafx.fxml;
    opens org.mcdcl.controller to javafx.fxml;
    opens org.mcdcl.ui to javafx.fxml;
}