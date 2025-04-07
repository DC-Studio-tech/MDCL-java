package org.mcdcl.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaFinder {
    public static List<String> findJavaInstallations() {
        List<String> installations = new ArrayList<>();
        
        // 添加系统环境变量中的Java路径
        String javaHome = System.getProperty("java.home");
        if (javaHome != null) {
            installations.add(javaHome + File.separator + "bin" + File.separator + "java");
        }
        
        // 添加常见的Java安装路径
        String[] commonPaths = {
            "/Library/Java/JavaVirtualMachines/",
            "/usr/java/",
            "/usr/lib/jvm/"
        };
        
        for (String path : commonPaths) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            String javaPath = file.getAbsolutePath() + "/bin/java";
                            if (new File(javaPath).exists()) {
                                installations.add(javaPath);
                            }
                        }
                    }
                }
            }
        }
        
        return installations;
    }

    public static boolean isValidJavaExecutable(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        
        File file = new File(path);
        return file.exists() && file.canExecute() && 
               (path.endsWith("java") || path.endsWith("java.exe"));
    }
}