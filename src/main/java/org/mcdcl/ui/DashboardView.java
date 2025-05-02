package org.mcdcl.ui;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mcdcl.model.CrashReport;
import org.mcdcl.service.CrashReportService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DashboardView extends ScrollPane {
    private VBox mainContainer;
    private VBox analysisContainer;
    private VBox placeholderContainer;
    private LineChart<Number, Number> memoryChart;
    private LineChart<Number, Number> performanceChart;
    private XYChart.Series<Number, Number> performanceSeries;
    private XYChart.Series<Number, Number> memorySeries;
    private Runtime runtime = Runtime.getRuntime();
    private CrashReportService crashReportService = new CrashReportService();
    private List<CrashReport> crashReports;
    private javafx.animation.Timeline timeline;
    private long lastFrameTime = System.nanoTime();
    private int frameCount = 0;
    private double currentFPS = 0.0;
    
    public DashboardView() {
        mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        
        // 创建分析类别容器
        createAnalysisSection();
        
        // 创建占位符类别容器
        createPlaceholderSection();
        
        // 设置ScrollPane属性
        setContent(mainContainer);
        setFitToWidth(true);
        getStyleClass().add("dashboard-view");
        
        // 初始化定时器，每秒更新一次数据
        initializeUpdateTimeline();
    }
    
    private void createAnalysisSection() {
        VBox section = createSection("分析");
        analysisContainer = new VBox(20);
        
        // 创建性能监控面板
        createPerformancePanel();
        
        // 创建内存使用分析面板
        createMemoryPanel();
        
        // 创建崩溃报告面板
        createCrashReportPanel();
        
        // 创建优化建议面板
        createOptimizationPanel();
        
        section.getChildren().add(analysisContainer);
        mainContainer.getChildren().add(section);
    }
    
    private void createPlaceholderSection() {
        VBox section = createSection("占位符");
        placeholderContainer = new VBox(20);
        
        // 这里预留占位符部分的实现
        
        section.getChildren().add(placeholderContainer);
        mainContainer.getChildren().add(section);
    }
    
    private VBox createSection(String title) {
        VBox section = new VBox(15);
        section.getStyleClass().add("dashboard-section");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        
        section.getChildren().add(titleLabel);
        return section;
    }
    
    private void createPerformancePanel() {
        VBox panel = createPanel("实时性能监控");
        
        // 创建性能图表
        NumberAxis xAxis = new NumberAxis("时间(秒)", 0, 50, 10);
        NumberAxis yAxis = new NumberAxis("FPS", 0, 120, 20);
        performanceChart = new LineChart<>(xAxis, yAxis);
        performanceChart.setTitle("游戏性能监控");
        performanceChart.setPrefHeight(300);
        performanceChart.setAnimated(false);
        
        // 初始化性能数据系列
        performanceSeries = new XYChart.Series<>();
        performanceSeries.setName("FPS");
        performanceChart.getData().add(performanceSeries);
        
        // 添加FPS计算器
        javafx.animation.AnimationTimer fpsCounter = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                frameCount++;
                if (now - lastFrameTime >= 1_000_000_000) { // 每秒更新一次
                    currentFPS = frameCount;
                    frameCount = 0;
                    lastFrameTime = now;
                }
            }
        };
        fpsCounter.start();
        
        // 创建FPS显示标签
        Label fpsLabel = new Label("当前FPS: 0");
        fpsLabel.getStyleClass().add("metric-label");
        
        // 创建水平布局容器
        HBox chartContainer = new HBox(20);
        chartContainer.setAlignment(Pos.CENTER_LEFT);
        chartContainer.getChildren().addAll(performanceChart, fpsLabel);
        
        panel.getChildren().add(chartContainer);
        analysisContainer.getChildren().add(panel);
    }
    
    private void createMemoryPanel() {
        VBox panel = createPanel("内存使用分析");
        
        // 创建内存图表
        NumberAxis xAxis = new NumberAxis("时间(秒)", 0, 50, 10);
        NumberAxis yAxis = new NumberAxis("内存使用(MB)", 0, 
            (int)(runtime.maxMemory() / (1024 * 1024)), 512);
        memoryChart = new LineChart<>(xAxis, yAxis);
        memoryChart.setTitle("内存使用趋势");
        memoryChart.setPrefHeight(300);
        memoryChart.setAnimated(false);
        
        // 初始化内存数据系列
        memorySeries = new XYChart.Series<>();
        memorySeries.setName("已用内存");
        memoryChart.getData().add(memorySeries);
        
        // 创建内存使用量显示标签
        Label memoryLabel = new Label("当前内存使用: 0 MB");
        memoryLabel.getStyleClass().add("metric-label");
        
        // 创建水平布局容器
        HBox chartContainer = new HBox(20);
        chartContainer.setAlignment(Pos.CENTER_LEFT);
        chartContainer.getChildren().addAll(memoryChart, memoryLabel);
        
        panel.getChildren().add(chartContainer);
        analysisContainer.getChildren().add(panel);
    }
    
    private void createCrashReportPanel() {
        VBox panel = createPanel("崩溃报告分析");
        
        // 创建刷新按钮
        Button refreshButton = new Button("刷新崩溃报告");
        refreshButton.getStyleClass().add("refresh-button");
        refreshButton.setOnAction(e -> refreshCrashReports());
        
        // 创建崩溃报告列表视图
        ListView<CrashReport> crashListView = new ListView<>();
        crashListView.setPrefHeight(300);
        crashListView.getStyleClass().add("crash-list-view");
        
        // 设置单元格工厂
        crashListView.setCellFactory(lv -> new ListCell<CrashReport>() {
            @Override
            protected void updateItem(CrashReport report, boolean empty) {
                super.updateItem(report, empty);
                if (empty || report == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    container.getStyleClass().add("crash-report-cell");
                    
                    // 创建标题和时间行
                    HBox header = new HBox(10);
                    Label titleLabel = new Label(report.getTitle());
                    titleLabel.getStyleClass().add("crash-title");
                    Label dateLabel = new Label(report.getTimestamp().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    dateLabel.getStyleClass().add("crash-date");
                    header.getChildren().addAll(titleLabel, dateLabel);
                    
                    // 创建错误信息行
                    Text errorText = new Text(report.getErrorType() + ": " + report.getErrorMessage());
                    errorText.getStyleClass().add("crash-error");
                    
                    // 创建详细信息展开面板
                    TitledPane detailsPane = new TitledPane();
                    detailsPane.setText("详细信息");
                    VBox details = new VBox(5);
                    
                    // 添加堆栈跟踪信息
                    TextArea stackTraceArea = new TextArea();
                    stackTraceArea.setEditable(false);
                    stackTraceArea.setWrapText(true);
                    stackTraceArea.setPrefRowCount(5);
                    stackTraceArea.setText(String.join("\n", report.getStackTrace()));
                    
                    // 添加游戏版本和模组信息
                    Label versionLabel = new Label("游戏版本: " + report.getGameVersion());
                    Label modsLabel = new Label("已加载模组: " + report.getModList());
                    Label systemLabel = new Label("系统信息: " + report.getSystemInfo());
                    
                    details.getChildren().addAll(
                        new Label("堆栈跟踪:"),
                        stackTraceArea,
                        versionLabel,
                        modsLabel,
                        systemLabel
                    );
                    
                    detailsPane.setContent(details);
                    detailsPane.setExpanded(false);
                    
                    // 创建操作按钮
                    Button deleteButton = new Button("删除");
                    deleteButton.getStyleClass().add("delete-button");
                    deleteButton.setOnAction(e -> {
                        crashReportService.deleteCrashReport(report.getTitle());
                        refreshCrashReports();
                    });
                    
                    container.getChildren().addAll(header, errorText, detailsPane, deleteButton);
                    setGraphic(container);
                }
            }
        });
        
        // 加载崩溃报告
        refreshCrashReports();
        
        // 将组件添加到面板
        panel.getChildren().addAll(refreshButton, crashListView);
        VBox.setVgrow(crashListView, Priority.ALWAYS);
        analysisContainer.getChildren().add(panel);
    }
    
    private void refreshCrashReports() {
        crashReports = crashReportService.loadCrashReports();
        ListView<CrashReport> crashListView = findCrashListView();
        if (crashListView != null) {
            crashListView.getItems().setAll(crashReports);
        }
    }
    
    private ListView<CrashReport> findCrashListView() {
        for (javafx.scene.Node node : analysisContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox panel = (VBox) node;
                for (javafx.scene.Node child : panel.getChildren()) {
                    if (child instanceof ListView) {
                        return (ListView<CrashReport>) child;
                    }
                }
            }
        }
        return null;
    }
    
    private void createOptimizationPanel() {
        VBox panel = createPanel("优化建议");
        
        // 添加优化建议列表
        VBox suggestions = new VBox(10);
        suggestions.setPadding(new Insets(10));
        
        // 添加示例优化建议
        addSuggestion(suggestions, "内存分配", "建议将最大内存设置调整为4GB以提升性能");
        addSuggestion(suggestions, "Java参数", "添加GC优化参数可以改善游戏运行流畅度");
        addSuggestion(suggestions, "显示设置", "当前渲染距离过大，建议调整为12个区块以获得更好的帧率");
        
        panel.getChildren().add(suggestions);
        analysisContainer.getChildren().add(panel);
    }
    
    private VBox createPanel(String title) {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("dashboard-panel");
        panel.setPadding(new Insets(15));
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("dashboard-panel-title");
        
        panel.getChildren().add(titleLabel);
        return panel;
    }
    

    
    private void addSuggestion(VBox container, String title, String description) {
        VBox suggestionBox = new VBox(5);
        suggestionBox.getStyleClass().add("suggestion-box");
        suggestionBox.setPadding(new Insets(10));
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("suggestion-title");
        
        Text descriptionText = new Text(description);
        descriptionText.getStyleClass().add("suggestion-description");
        
        suggestionBox.getChildren().addAll(titleLabel, descriptionText);
        container.getChildren().add(suggestionBox);
    }
    
    private void initializeUpdateTimeline() {
        timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), event -> {
                // 更新内存使用数据
                long totalMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();
                long usedMemory = (totalMemory - freeMemory) / (1024 * 1024); // 转换为MB
                
                // 更新图表数据
                if (memorySeries.getData().size() > 50) {
                    memorySeries.getData().remove(0);
                    performanceSeries.getData().remove(0);
                }
                
                memorySeries.getData().add(new XYChart.Data<>(memorySeries.getData().size(), usedMemory));
                performanceSeries.getData().add(new XYChart.Data<>(performanceSeries.getData().size(), currentFPS));
                
                // 更新标签显示
                for (javafx.scene.Node node : analysisContainer.getChildren()) {
                    if (node instanceof VBox panel) {
                        for (javafx.scene.Node child : panel.getChildren()) {
                            if (child instanceof HBox chartContainer) {
                                for (javafx.scene.Node metric : chartContainer.getChildren()) {
                                    if (metric instanceof Label label) {
                                        if (label.getStyleClass().contains("metric-label")) {
                                            if (chartContainer.getChildren().get(0) == performanceChart) {
                                                label.setText(String.format("当前FPS: %.0f", currentFPS));
                                            } else if (chartContainer.getChildren().get(0) == memoryChart) {
                                                label.setText(String.format("当前内存使用: %d MB", usedMemory));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }
}