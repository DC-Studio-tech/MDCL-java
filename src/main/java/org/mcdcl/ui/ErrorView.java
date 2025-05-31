package org.mcdcl.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 错误信息显示页面
 * 用于展示游戏启动或运行过程中的错误信息
 */
public class ErrorView extends BorderPane {
    
    private String errorTitle;
    private String errorMessage;
    private String stackTrace;
    private EventHandler<ActionEvent> returnHandler;
    
    public ErrorView(String errorTitle, String errorMessage, Throwable exception) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.stackTrace = getStackTraceAsString(exception);
        
        initUI();
    }
    
    /**
     * 设置返回按钮的事件处理器
     * @param handler 返回按钮点击时的处理逻辑
     */
    public void setReturnHandler(EventHandler<ActionEvent> handler) {
        this.returnHandler = handler;
    }
    
    private void initUI() {
        setPadding(new Insets(20));
        
        // 顶部标题
        Text title = new Text("启动错误");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setFill(Color.RED);
        
        // 错误标题
        Label errorTitleLabel = new Label(errorTitle);
        errorTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        errorTitleLabel.setTextFill(Color.RED);
        
        // 错误信息
        Label errorMessageLabel = new Label(errorMessage);
        errorMessageLabel.setWrapText(true);
        
        // 堆栈跟踪
        TextArea stackTraceArea = new TextArea(stackTrace);
        stackTraceArea.setEditable(false);
        stackTraceArea.setWrapText(true);
        stackTraceArea.setPrefHeight(300);
        VBox.setVgrow(stackTraceArea, Priority.ALWAYS);
        
        // 创建滚动面板
        ScrollPane scrollPane = new ScrollPane(stackTraceArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        // 底部按钮
        Button copyButton = new Button("复制错误信息");
        copyButton.setOnAction(e -> {
            javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(errorTitle + "\n\n" + errorMessage + "\n\n" + stackTrace);
            clipboard.setContent(content);
        });
        
        Button returnButton = new Button("返回主界面");
        returnButton.setOnAction(e -> {
            if (returnHandler != null) {
                returnHandler.handle(e);
            }
        });
        
        HBox buttonBox = new HBox(10, copyButton, returnButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        // 组装界面
        VBox topBox = new VBox(10, title, errorTitleLabel, errorMessageLabel);
        
        VBox centerBox = new VBox(10);
        centerBox.getChildren().addAll(
            new Label("详细错误信息:"),
            scrollPane
        );
        
        // 在initUI方法中添加常见问题解决建议
        Label helpLabel = new Label("常见解决方法：");
        helpLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        VBox helpBox = new VBox(5);
        helpBox.getChildren().addAll(
            new Label("1. 检查Java版本是否与游戏版本兼容"),
            new Label("2. 确保网络连接正常"),
            new Label("3. 检查是否有足够的磁盘空间"),
            new Label("4. 尝试重新下载游戏文件")
        );
        
        // 将帮助信息添加到界面
        centerBox.getChildren().addAll(helpLabel, helpBox);
        setTop(topBox);
        setCenter(centerBox);
        setBottom(buttonBox);
    }
    
    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "无堆栈跟踪信息";
        }
        
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
