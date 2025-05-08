package org.mcdcl.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressDialog extends Stage {
    private final Label titleLabel;
    private final Label statusLabel;
    private final ProgressBar progressBar;
    private final Button cancelButton;
    private boolean isCancelled = false;

    public ProgressDialog() {
        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        // 创建UI组件
        titleLabel = new Label("启动游戏");
        titleLabel.getStyleClass().add("dialog-title");

        statusLabel = new Label("正在准备启动...");
        statusLabel.getStyleClass().add("status-label");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.getStyleClass().add("progress-bar");

        cancelButton = new Button("取消");
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setOnAction(e -> {
            isCancelled = true;
            close();
        });

        // 布局
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, statusLabel, progressBar, cancelButton);
        layout.getStyleClass().add("progress-dialog");

        // 场景
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/styles/progress-dialog.css").toExternalForm());
        setScene(scene);

        // 窗口属性
        setWidth(400);
        setHeight(200);
        setResizable(false);
    }

    public void updateProgress(double progress) {
        Platform.runLater(() -> progressBar.setProgress(progress));
    }

    public void updateStatus(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void showProgress() {
        show();
        centerOnScreen();
    }
}