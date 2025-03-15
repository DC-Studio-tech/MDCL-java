package org.mcdcl.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JavaFinder {
    private static final String[] COMMON_JAVA_PATHS = {
        "/Library/Java/JavaVirtualMachines",  // macOS
        "/usr/lib/jvm",                      // Linux
        "C:\\Program Files\\Java",            // Windows
        "C:\\Program Files (x86)\\Java"       // Windows (32-bit)
    };

    /**
     * 扫描系统中已安装的Java版本
     * @return 返回找到的Java可执行文件路径列表
     */
    public static List<String> findJavaInstallations() {
        List<String> javaInstallations = new ArrayList<>();

        // 检查JAVA_HOME环境变量
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null && !javaHome.isEmpty()) {
            String javaPath = getJavaExecutablePath(javaHome);
            if (isValidJavaExecutable(javaPath)) {
                javaInstallations.add(javaPath);
            }
        }

        // 检查PATH环境变量
        String pathEnv = System.getenv("PATH");
        if (pathEnv != null) {
            String[] paths = pathEnv.split(File.pathSeparator);
            for (String path : paths) {
                String javaPath = getJavaExecutablePath(path);
                if (isValidJavaExecutable(javaPath) && !javaInstallations.contains(javaPath)) {
                    javaInstallations.add(javaPath);
                }
            }
        }

        // 扫描常见的Java安装目录
        for (String commonPath : COMMON_JAVA_PATHS) {
            Path path = Paths.get(commonPath);
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path, 5)) {
                    walk.filter(p -> p.getFileName().toString().equals(getJavaExecutableName()))
                        .map(Path::toString)
                        .filter(JavaFinder::isValidJavaExecutable)
                        .forEach(javaPath -> {
                            if (!javaInstallations.contains(javaPath)) {
                                javaInstallations.add(javaPath);
                            }
                        });
                } catch (IOException e) {
                    // 忽略访问错误
                }
            }
        }

        return javaInstallations;
    }

    /**
     * 获取Java可执行文件路径
     * @param basePath 基础路径
     * @return Java可执行文件的完整路径
     */
    private static String getJavaExecutablePath(String basePath) {
        return Paths.get(basePath, "bin", getJavaExecutableName()).toString();
    }

    /**
     * 获取当前操作系统对应的Java可执行文件名
     * @return Java可执行文件名
     */
    private static String getJavaExecutableName() {
        return System.getProperty("os.name").toLowerCase().contains("windows") ? "java.exe" : "java";
    }

    /**
     * 验证Java可执行文件是否有效
     * @param path Java可执行文件路径
     * @return 是否为有效的Java可执行文件
     */
    public static boolean isValidJavaExecutable(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile() || !file.canExecute()) {
            return false;
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(path, "-version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
}