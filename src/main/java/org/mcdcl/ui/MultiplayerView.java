package org.mcdcl.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label; // Import SettingsManager
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority; // Import Button
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MultiplayerView extends BorderPane {

    // Define an inner class to hold server information
    private static class ServerInfo {
        String name;
        String ip;
        String players;

        ServerInfo(String name, String ip, String players) {
            this.name = name;
            this.ip = ip;
            this.players = players;
        }
    }

    private ListView<ServerInfo> serverListView; // Change ListView type
    private final ObservableList<ServerInfo> serverList = FXCollections.observableArrayList(); // Change ObservableList type
    private ProgressIndicator loadingIndicator;
    private Label statusLabel;
    private Button joinServerButton; // Add join server button
    private MainView mainView; // Add reference to MainView

    public MultiplayerView(MainView mainView) { // Modify constructor to accept MainView
        this.mainView = mainView;
        setPadding(new Insets(20));

        // 标题
        Label titleLabel = new Label("多人游戏");
        titleLabel.getStyleClass().add("view-title");
        BorderPane.setAlignment(titleLabel, Pos.TOP_CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 20, 0));
        setTop(titleLabel);

        // 服务器列表
        serverListView = new ListView<>(serverList);
        serverListView.setPrefWidth(350); // Increase width slightly for better display
        serverListView.setCellFactory(param -> new ServerListCell()); // Set custom cell factory

        // 加载指示器和状态标签
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(30, 30);
        statusLabel = new Label("正在加载服务器列表...");
        VBox loadingBox = new VBox(10, loadingIndicator, statusLabel);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setVisible(false); // 初始隐藏

        // 列表和加载指示器的容器
        StackPane listContainer = new StackPane();
        listContainer.getChildren().addAll(serverListView, loadingBox);

        // 加入服务器按钮
        joinServerButton = new Button("加入服务器");
        joinServerButton.getStyleClass().add("launch-button"); // Reuse launch button style
        joinServerButton.setOnAction(event -> joinSelectedServer());
        VBox.setMargin(joinServerButton, new Insets(10, 0, 0, 0)); // Add margin

        // 服务器列表和按钮的容器
        VBox serverListArea = new VBox(10, listContainer, joinServerButton);
        serverListArea.setAlignment(Pos.TOP_CENTER);

        // P2P联机功能区域
        VBox joinPlaceholder = new VBox(10);
        joinPlaceholder.setAlignment(Pos.CENTER);
        joinPlaceholder.setPadding(new Insets(10));
        
        // 生成密钥按钮
        Button generateKeyButton = new Button("生成联机密钥（联机功能尚未做完）");
        generateKeyButton.getStyleClass().add("launch-button");
        
        // 密钥显示标签
        Label keyLabel = new Label();
        keyLabel.setVisible(false);
        
        // 复制密钥按钮
        Button copyKeyButton = new Button("复制");
        copyKeyButton.getStyleClass().add("launch-button");
        copyKeyButton.setVisible(false);
        
        // 输入密钥区域
        TextField keyInput = new TextField();
        keyInput.setPromptText("输入好友的联机密钥");
        keyInput.setVisible(false);
        
        // 连接按钮
        Button connectButton = new Button("连接好友");
        connectButton.getStyleClass().add("launch-button");
        connectButton.setVisible(false);
        
        // 按钮点击事件
        generateKeyButton.setOnAction(event -> {
            String randomKey = generateRandomKey();
            keyLabel.setText("您的联机密钥: " + randomKey);
            keyLabel.setVisible(true);
            copyKeyButton.setVisible(true);
            keyInput.setVisible(true);
            connectButton.setVisible(true);
            
            // 设置复制按钮点击事件
            copyKeyButton.setOnAction(e -> {
                javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(randomKey);
                clipboard.setContent(content);
                mainView.showAlert("成功", "密钥已复制到剪贴板");
            });
        });
        
        connectButton.setOnAction(event -> {
            String friendKey = keyInput.getText();
            if(!friendKey.isEmpty()) {
                mainView.showAlert("连接中", "正在尝试连接好友...");
                
                // 创建P2P连接线程
                new Thread(() -> {
                    try {
                        // 1. 密钥验证
                        if(!validateKey(friendKey)) {
                            Platform.runLater(() -> 
                                mainView.showAlert("错误", "无效的联机密钥"));
                            return;
                        }
                        
                        // 2. 建立P2P连接
                        String peerIP = establishP2PConnection(friendKey);
                        
                        // 3. 连接成功后启动游戏
                        Platform.runLater(() -> {
                            mainView.showAlert("成功", "已连接到好友，准备启动游戏");
                            // Use the correct launchGame method with selected version
                            String selectedVersion = mainView.getSelectedOrDefaultVersion();
                            if (selectedVersion != null) {
                                mainView.launchGame(selectedVersion, peerIP);
                            } else {
                                mainView.showAlert("错误", "无法获取当前选择的游戏版本");
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> 
                            mainView.showAlert("错误", "连接失败: " + e.getMessage()));
                    }
                }).start();
            }
        });
        
        joinPlaceholder.getChildren().addAll(
            generateKeyButton,
            new HBox(5, keyLabel, copyKeyButton),
            keyInput,
            connectButton
        );
        joinPlaceholder.setStyle("-fx-border-color: gray; -fx-border-style: dashed; -fx-padding: 10;");
        HBox.setHgrow(joinPlaceholder, Priority.ALWAYS);

        // 中间布局
        HBox centerLayout = new HBox(20); // Increase spacing
        centerLayout.getChildren().addAll(serverListArea, joinPlaceholder);
        System.out.println("MultiplayerView initialized"); // 调试输出
        setCenter(centerLayout); // 确保布局设置

        getStyleClass().add("multiplayer-view"); // 添加特定的样式类

        // 异步加载服务器列表
        loadServerList();
    }

    private void loadServerList() {
        showLoading(true, "正在加载服务器列表...");

        Task<ObservableList<ServerInfo>> task = new Task<>() {
            @Override
            protected ObservableList<ServerInfo> call() throws Exception {
                ObservableList<ServerInfo> fetchedServers = FXCollections.observableArrayList();
                URL url = new URL("https://list.mczfw.cn/api");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        System.out.println("Raw API response: " + response.toString());
                        System.out.println("Parsing JSON response...");
                        JSONArray serversArray = new JSONArray(response.toString());
                        System.out.println("Parsed servers count: " + serversArray.length());
                        if(serversArray.length() > 0) {
                            System.out.println("First server data: " + serversArray.getJSONObject(0).toString());
                            
                            // 调试日志：输出解析后的服务器数据
                            for (int i = 0; i < Math.min(serversArray.length(), 5); i++) {
                                JSONObject server = serversArray.getJSONObject(i);
                                System.out.println("Server " + i + ": " + 
                                    "name=" + server.optString("name", "") + ", " +
                                    "motd=" + server.optString("motd", "") + ", " +
                                    "ip=" + server.optString("ip", "") + ", " +
                                    "players=" + server.optString("p", ""));
                            }
                        }
                        for (int i = 0; i < serversArray.length(); i++) {
                            JSONObject server = serversArray.getJSONObject(i);
                            String motd = server.optString("motd", "");
                            String name = server.optString("name", "未知服务器");
                            String extractedName = motd.lines()
                                                     .map(String::trim)
                                                     .filter(motdLine -> !motdLine.isEmpty())
                                                     .findFirst()
                                                     .orElse(name);

                            String ip = server.optString("ip", "未知IP");
                            String players = server.optString("p", "?/?");
                            fetchedServers.add(new ServerInfo(extractedName, ip, players));
                        }
                    }
                } else {
                    throw new Exception("HTTP请求失败，响应码: " + responseCode);
                }
                connection.disconnect();
                return fetchedServers;
            }
        };
    
        task.setOnSucceeded(e -> {
            System.out.println("Server list updated with " + task.getValue().size() + " servers");
            // 确保UI更新在主线程执行
            Platform.runLater(() -> {
                try {
                    serverList.setAll(task.getValue());
                    showLoading(false, "");
                    System.out.println("UI updated on JavaFX thread");
                    
                    // 验证数据绑定
                    if (!serverList.isEmpty()) {
                        System.out.println("First server in UI list: " + 
                            serverList.get(0).name + ", " + 
                            serverList.get(0).ip + ", " + 
                            serverList.get(0).players);
                    }
                } catch (Exception ex) {
                    System.err.println("UI更新错误: " + ex.getMessage());
                    showLoading(false, "UI更新失败");
                }
            });
        });
    
        task.setOnFailed(e -> {
            showLoading(false, "加载失败: " + task.getException().getMessage());
        });
    
        new Thread(task).start();
    }

    private void showLoading(boolean show, String message) {
        Platform.runLater(() -> {
            StackPane listContainer = (StackPane) serverListView.getParent();
            VBox loadingBox = (VBox) listContainer.getChildren().get(1);
            loadingBox.setVisible(show);
            statusLabel.setText(message);
            loadingIndicator.setVisible(show); // 确保加载时图标可见
            serverListView.setVisible(!show);
        });
    }

    // Custom ListCell implementation
    private class ServerListCell extends ListCell<ServerInfo> {
        private final HBox contentBox = new HBox(10); // Use HBox for layout
        private final VBox nameIpBox = new VBox(2); // VBox for name and IP
        private final Text serverName = new Text();
        private final Text serverIp = new Text();
        private final Label playersLabel = new Label();
        private final Region spacer = new Region(); // Spacer to push playersLabel to the right

        public ServerListCell() {
            serverIp.setStyle("-fx-font-size: 0.9em;"); // Style IP differently, removed gray fill for better contrast
            nameIpBox.getChildren().addAll(serverName, serverIp);

            HBox.setHgrow(spacer, Priority.ALWAYS); // Make spacer grow
            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.getChildren().addAll(nameIpBox, spacer, playersLabel);
            contentBox.setPadding(new Insets(5, 10, 5, 10)); // Add padding within the cell
        }

        @Override
        protected void updateItem(ServerInfo item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                serverName.setText(item.name);
                serverIp.setText(item.ip);
                playersLabel.setText("[" + item.players + "]");
                setGraphic(contentBox);
            }
        }
    }

    private String generateRandomKey() {
        // 生成16位随机字母数字组合的密钥
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int index = (int)(Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    private boolean validateKey(String key) {
        // 更强的密钥验证
        if (key == null || key.isEmpty()) {
            return false;
        }
        
        // 检查长度
        if (key.length() < 16 || key.length() > 32) {
            return false;
        }
        
        // 检查格式 - 要求包含字母和数字的组合
        boolean hasLetter = false;
        boolean hasDigit = false;
        
        for (char c : key.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                // 不允许特殊字符
                return false;
            }
            
            if (hasLetter && hasDigit) {
                break;
            }
        }
        
        return hasLetter && hasDigit;
    }
    
    private String establishP2PConnection(String key) throws Exception {
        // 这里实现NAT穿透和P2P连接逻辑
        // 实际项目中应该使用专门的P2P库如JXTA或自定义实现
        
        // 模拟实现：将密钥转换为IP地址
        // 实际项目中应该通过信令服务器交换连接信息
        return "192.168.1." + (key.hashCode() % 255 & 0xFF);
    }

    private void joinSelectedServer() {
        ServerInfo selectedServer = serverListView.getSelectionModel().getSelectedItem();
        if (selectedServer != null) {
            mainView.showAlert("加入服务器", "正在加入服务器: " + selectedServer.name);
            // Use the correct launchGame method with selected version
            String selectedVersion = mainView.getSelectedOrDefaultVersion();
            if (selectedVersion != null) {
                mainView.launchGame(selectedVersion, selectedServer.ip);
            } else {
                mainView.showAlert("错误", "无法获取当前选择的游戏版本");
            }
        } else {
            mainView.showAlert("未选择服务器", "请先从列表中选择一个服务器");
        }
    }
}
