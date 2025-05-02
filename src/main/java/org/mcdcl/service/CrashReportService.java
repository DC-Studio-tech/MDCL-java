package org.mcdcl.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mcdcl.model.CrashReport;

public class CrashReportService {
    private static final String CRASH_REPORTS_DIR = "crash-reports";
    private static final Pattern ERROR_PATTERN = Pattern.compile("([\\w\\.]+Exception|[\\w\\.]+Error):\\s*(.*)\\n");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public List<CrashReport> loadCrashReports() {
        List<CrashReport> reports = new ArrayList<>();
        Path crashReportsPath = Paths.get(CRASH_REPORTS_DIR);
        
        try {
            if (!Files.exists(crashReportsPath)) {
                return reports;
            }
            
            Files.list(crashReportsPath)
                .filter(path -> path.toString().endsWith(".txt") || path.toString().endsWith(".log"))
                .forEach(path -> {
                    try {
                        CrashReport report = parseCrashReport(path);
                        if (report != null) {
                            reports.add(report);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    private CrashReport parseCrashReport(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) {
            return null;
        }
        
        CrashReport report = new CrashReport();
        report.setTimestamp(LocalDateTime.now()); // 默认使用当前时间
        
        boolean inStackTrace = false;
        StringBuilder stackTrace = new StringBuilder();
        
        for (String line : lines) {
            // 解析错误类型和消息
            Matcher errorMatcher = ERROR_PATTERN.matcher(line);
            if (errorMatcher.find()) {
                report.setErrorType(errorMatcher.group(1));
                report.setErrorMessage(errorMatcher.group(2));
                inStackTrace = true;
                continue;
            }
            
            // 收集堆栈跟踪信息
            if (inStackTrace && line.trim().startsWith("at ")) {
                report.addStackTraceLine(line.trim());
                stackTrace.append(line.trim()).append("\n");
            }
            
            // 解析游戏版本
            if (line.contains("Minecraft Version:")) {
                report.setGameVersion(line.split(":")[1].trim());
            }
            
            // 解析模组列表
            if (line.contains("Loaded Mods:")) {
                report.setModList(line.split(":")[1].trim());
            }
            
            // 解析系统信息
            if (line.contains("Operating System:")) {
                report.setSystemInfo(line.split(":")[1].trim());
            }
        }
        
        // 设置标题
        String fileName = path.getFileName().toString();
        report.setTitle(fileName.substring(0, fileName.lastIndexOf('.')));
        
        return report;
    }
    
    public void deleteCrashReport(String reportTitle) {
        Path crashReportsPath = Paths.get(CRASH_REPORTS_DIR);
        try {
            Files.list(crashReportsPath)
                .filter(path -> path.getFileName().toString().startsWith(reportTitle))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}