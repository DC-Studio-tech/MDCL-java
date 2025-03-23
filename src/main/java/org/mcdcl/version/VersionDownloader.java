package org.mcdcl.version;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import org.mcdcl.config.Settings;
import org.mcdcl.config.SettingsManager;
import org.to2mbn.jmccc.mcdownloader.CacheOption;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloaderBuilder;
import org.to2mbn.jmccc.mcdownloader.RemoteVersion;
import org.to2mbn.jmccc.mcdownloader.RemoteVersionList;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CombinedDownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.DownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.tasks.DownloadTask;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

/**
 * Minecraft版本下载器
 */
public class VersionDownloader {
    
    private MinecraftDownloader downloader;
    private Dialog<Void> progressDialog;
    private ProgressBar progressBar;
    private TextArea logArea;
    private boolean downloadCompleted = false;
    private boolean downloadCancelled = false;
    
    /**
     * 创建一个新的版本下载器
     */
    public VersionDownloader() {
        // 创建MinecraftDownloader实例
        downloader = MinecraftDownloaderBuilder.buildDefault();
        
        // 初始化进度对话框
        initProgressDialog();
    }
    
    /**
     * 初始化进度对话框
     */
    private void initProgressDialog() {
        progressDialog = new Dialog<>();
        progressDialog.setTitle("下载游戏版本");
        progressDialog.setHeaderText("正在下载Minecraft版本，请稍候...");
        progressDialog.initModality(Modality.APPLICATION_MODAL);
        
        // 创建进度条
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        
        // 创建日志区域
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(200);
        logArea.setWrapText(true);
        
        // 添加到对话框
        VBox content = new VBox(10);
        content.getChildren().addAll(progressBar, logArea);
        progressDialog.getDialogPane().setContent(content);
        
        // 添加取消按钮
        progressDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        progressDialog.setOnCloseRequest(event -> {
            downloadCancelled = true;
            if (downloader != null && !downloader.isShutdown()) {
                downloader.shutdown();
            }
        });
    }
    
    /**
     * 获取远程版本列表
     * 
     * @param callback 获取完成后的回调
     */
    public void fetchVersionList(VersionListCallback callback) {
        try {
            // 显示加载中消息
            logMessage("正在获取Minecraft版本列表...");
            
            // 获取远程版本列表
            Future<RemoteVersionList> future = downloader.fetchRemoteVersionList(new CallbackAdapter<RemoteVersionList>() {
                @Override
                public void done(RemoteVersionList result) {
                    Platform.runLater(() -> {
                        if (callback != null) {
                            callback.onVersionListFetched(result);
                        }
                    });
                }
                
                @Override
                public void failed(Throwable e) {
                    Platform.runLater(() -> {
                        logMessage("获取版本列表失败: " + e.getMessage());
                        showErrorAlert("获取版本列表失败", e.getMessage());
                    });
                }
                
                @Override
                public void cancelled() {
                    Platform.runLater(() -> {
                        logMessage("获取版本列表已取消");
                    });
                }
                
                @Override
                public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                    Platform.runLater(() -> {
                        logMessage("开始下载: " + task.getURI());
                    });
                    return null;
                }
            }, CacheOption.CACHE);
            
        } catch (Exception e) {
            logMessage("获取版本列表时发生错误: " + e.getMessage());
            showErrorAlert("获取版本列表失败", e.getMessage());
        }
    }
    
    /**
     * 下载指定的Minecraft版本
     * 
     * @param versionId 要下载的版本ID
     * @param callback 下载完成后的回调
     */
    public void downloadVersion(String versionId, VersionDownloadCallback callback) {
        try {
            // 获取Minecraft目录
            Settings settings = SettingsManager.loadSettings();
            String minecraftPath = settings.getMinecraftPath();
            
            if (minecraftPath == null || minecraftPath.isEmpty()) {
                minecraftPath = System.getProperty("user.home") + "/.minecraft";
            }
            
            File minecraftFolder = new File(minecraftPath);
            if (!minecraftFolder.exists()) {
                minecraftFolder.mkdirs();
            }
            
            MinecraftDirectory minecraftDir = new MinecraftDirectory(minecraftFolder);
            
            // 显示进度对话框
            Platform.runLater(() -> {
                progressBar.setProgress(0);
                logArea.clear();
                logMessage("开始下载Minecraft版本: " + versionId);
                progressDialog.show();
            });
            
            // 下载版本
            Future<Version> future = downloader.downloadIncrementally(minecraftDir, versionId, new CallbackAdapter<Version>() {
                @Override
                public void done(Version result) {
                    Platform.runLater(() -> {
                        downloadCompleted = true;
                        progressBar.setProgress(1.0);
                        logMessage("版本 " + versionId + " 下载完成!");
                        progressDialog.close();
                        
                        if (callback != null) {
                            callback.onVersionDownloaded(versionId);
                        }
                    });
                }
                
                @Override
                public void failed(Throwable e) {
                    Platform.runLater(() -> {
                        logMessage("下载失败: " + e.getMessage());
                        progressDialog.close();
                        showErrorAlert("下载失败", "版本 " + versionId + " 下载失败: " + e.getMessage());
                    });
                }
                
                @Override
                public void cancelled() {
                    Platform.runLater(() -> {
                        logMessage("下载已取消");
                        progressDialog.close();
                    });
                }
                
                @Override
                public void updateProgress(long done, long total) {
                    if (total > 0) {
                        double progress = (double) done / total;
                        // 确保进度值在0-1之间
                        progress = Math.max(0, Math.min(1, progress));
                        final double finalProgress = progress;
                        Platform.runLater(() -> {
                            progressBar.setProgress(finalProgress);
                            // 添加日志，显示下载进度百分比
                            if (finalProgress < 1.0 && (int)(finalProgress * 100) % 10 == 0) {
                                logMessage(String.format("下载进度: %.0f%%", finalProgress * 100));
                            }
                        });
                    }
                }
                
                @Override
                public void retry(Throwable e, int current, int max) {
                    Platform.runLater(() -> {
                        logMessage("下载失败，正在重试 (" + current + "/" + max + "): " + e.getMessage());
                    });
                }
                
                @Override
                public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                    Platform.runLater(() -> {
                        logMessage("开始下载: " + task.getURI());
                    });
                    return null;
                }
            });
            
        } catch (Exception e) {
            Platform.runLater(() -> {
                logMessage("下载时发生错误: " + e.getMessage());
                progressDialog.close();
                showErrorAlert("下载失败", e.getMessage());
            });
        }
    }
    
    /**
     * 添加日志消息
     * 
     * @param message 要添加的消息
     */
    private void logMessage(String message) {
        Platform.runLater(() -> {
            logArea.appendText(message + "\n");
            logArea.setScrollTop(Double.MAX_VALUE); // 滚动到底部
        });
    }
    
    /**
     * 显示错误提示
     * 
     * @param title 标题
     * @param message 错误消息
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * 关闭下载器
     */
    public void close() {
        if (downloader != null && !downloader.isShutdown()) {
            downloader.shutdown();
        }
    }
    
    /**
     * 版本列表获取回调接口
     */
    public interface VersionListCallback {
        /**
         * 当版本列表获取完成时调用
         * 
         * @param versionList 获取到的版本列表
         */
        void onVersionListFetched(RemoteVersionList versionList);
    }
    
    /**
     * 版本下载回调接口
     */
    public interface VersionDownloadCallback {
        /**
         * 当版本下载完成时调用
         * 
         * @param versionId 下载完成的版本ID
         */
        void onVersionDownloaded(String versionId);
    }
}