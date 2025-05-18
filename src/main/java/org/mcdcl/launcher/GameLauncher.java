package org.mcdcl.launcher;

import java.io.File;
import java.io.IOException;

import org.mcdcl.config.UserPreferences;
import org.mcdcl.ui.ProgressDialog;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;

// Import List and ArrayList
import java.util.List;
import java.util.ArrayList;
 public class GameLauncher {
     private String javaPath;
     private int maxMemory;
     private String jvmArgs;
     private String gameArgs;
     private Process gameProcess;
     private Launcher launcher;
     private MinecraftDirectory minecraftDir;
 
     // 构造函数
     public GameLauncher(String javaPath, int maxMemory, String jvmArgs, String gameArgs, String minecraftPath) {
         this.javaPath = javaPath;
         this.maxMemory = maxMemory;
         this.jvmArgs = jvmArgs;
         this.gameArgs = gameArgs;
         
         // 使用指定的Minecraft目录
         File minecraftFolder;
         if (minecraftPath != null && !minecraftPath.isEmpty()) {
             minecraftFolder = new File(minecraftPath);
         } else {
             // 如果没有指定，则使用默认目录
             String userHome = System.getProperty("user.home");
             minecraftFolder = new File(userHome, ".minecraft");
         }
         
         this.minecraftDir = new MinecraftDirectory(minecraftFolder);
         this.launcher = LauncherBuilder.create().build();
     }
 
     // 启动游戏的方法
    public void launchGame(String versionName) throws LaunchException, IOException {
        launchGame(versionName, null); // Call the overloaded method with null server IP
    }

    // Overloaded launchGame method to accept server IP
    public void launchGame(String versionName, String serverIp) throws LaunchException, IOException {
        // 创建并显示进度弹窗
        ProgressDialog progressDialog = new ProgressDialog();
        progressDialog.showProgress();
        
        try {
            // 检测Java版本
            progressDialog.updateStatus("检测Java版本...");
            progressDialog.updateProgress(0.1);
            if (progressDialog.isCancelled()) {
                return;
            }
            System.out.println("开始启动游戏版本: " + versionName);
            if (javaPath == null || javaPath.isEmpty()) {
                throw new LaunchException("Java路径未设置，请在设置中选择有效的Java路径");
            }
         
            File javaFile = new File(javaPath);
            if (!javaFile.exists() || !javaFile.isFile() || !javaFile.canExecute()) {
                throw new LaunchException("Java路径无效或不可执行: " + javaPath);
            }
            
            // 检查游戏依赖
            progressDialog.updateStatus("检查游戏依赖...");
            progressDialog.updateProgress(0.3);
            if (progressDialog.isCancelled()) {
                return;
            }
            if (!minecraftDir.getRoot().exists() || !minecraftDir.getRoot().isDirectory()) {
                throw new LaunchException("Minecraft目录不存在或不是有效目录: " + minecraftDir.getRoot().getAbsolutePath());
            }
         
            // 检查版本目录是否存在
            progressDialog.updateStatus("检查游戏版本...");
            progressDialog.updateProgress(0.5);
            if (progressDialog.isCancelled()) {
                return;
            }
            File versionDir = new File(minecraftDir.getVersions(), versionName);
            if (!versionDir.exists() || !versionDir.isDirectory()) {
                throw new LaunchException("找不到版本: " + versionName + "，请确认该版本已安装");
            }
            
            // 使用版本名称获取版本对象
            Version version = org.to2mbn.jmccc.version.parsing.Versions.resolveVersion(minecraftDir, versionName);
            if (version == null) {
                throw new LaunchException("找不到版本: " + versionName + "，版本文件可能已损坏");
            }
         
            // 处理账号登录
            progressDialog.updateStatus("处理账号登录...");
            progressDialog.updateProgress(0.7);
            if (progressDialog.isCancelled()) {
                return;
            }
            String username = UserPreferences.getCurrentAccount();
            if (username.isEmpty()) {
                username = "Player";
            }
            
            // 创建 LaunchOption，使用离线模式认证器
            LaunchOption option = new LaunchOption(version, new OfflineAuthenticator(username), minecraftDir);
         
         // 设置Java路径
         option.setJavaEnvironment(new org.to2mbn.jmccc.option.JavaEnvironment(new File(javaPath)));
         
         // 设置最大内存
         option.setMaxMemory(maxMemory);
         
         // 设置JVM参数
         if (jvmArgs != null && !jvmArgs.isEmpty()) {
             for (String arg : jvmArgs.split(" ")) {
                 option.extraJvmArguments().add(arg);
             }
         }
         
         // 设置游戏参数
        List<String> minecraftArgs = new ArrayList<>();
        if (gameArgs != null && !gameArgs.isEmpty()) {
            for (String arg : gameArgs.split(" ")) {
                minecraftArgs.add(arg);
            }
        }

        // Add server IP and port if provided
        if (serverIp != null && !serverIp.isEmpty()) {
            minecraftArgs.add("--server");
            minecraftArgs.add(serverIp);
            // Assuming default port 25565, add --port if needed
            // minecraftArgs.add("--port");
            // minecraftArgs.add("25565");
        }

        // Add all arguments to the option
        for (String arg : minecraftArgs) {
            option.extraMinecraftArguments().add(arg);
        }
         
            // 准备启动游戏
            progressDialog.updateStatus("准备启动游戏...");
            progressDialog.updateProgress(0.9);
            
            // 启用调试模式，打印启动命令行
            this.launcher = LauncherBuilder.create().printDebugCommandline(true).build();
            
            // 添加进程监听器来捕获游戏输出
            gameProcess = launcher.launch(option, new org.to2mbn.jmccc.launch.ProcessListener() {
                 @Override
                 public void onLog(String log) {
                     System.out.println("[Minecraft] " + log);
                 }
                 
                 @Override
                 public void onErrorLog(String log) {
                     System.err.println("[Minecraft Error] " + log);
                 }
                 
                 @Override
                 public void onExit(int code) {
                     System.out.println("[Minecraft] 游戏进程已退出，退出码: " + code);
                 }
             });
             
             // 检查进程是否成功启动
             if (gameProcess == null) {
                 throw new LaunchException("游戏进程启动失败，无法获取进程句柄");
             }
             
            System.out.println("游戏进程已启动，PID: " + gameProcess.pid());
            
            // 更新进度为完成
            progressDialog.updateStatus("游戏启动完成！");
            progressDialog.updateProgress(1.0);
            Thread.sleep(1000); // 显示完成状态1秒
            progressDialog.close();
            
        } catch (Exception e) {
            System.err.println("启动游戏时发生异常: " + e.getMessage());
            e.printStackTrace();
            progressDialog.close();
            throw new LaunchException("启动游戏失败: " + e.getMessage(), e);
        }
        
        // 检查是否被用户取消
        if (progressDialog.isCancelled()) {
            stopGame();
            throw new LaunchException("启动已被用户取消");
        }
     }
 
     // 关闭游戏进程的方法
     public void stopGame() {
         if (gameProcess != null && gameProcess.isAlive()) {
             gameProcess.destroy();
         }
     }
 
     // 检查游戏是否正在运行的方法
     public boolean isRunning() {
         return gameProcess != null && gameProcess.isAlive();
     }
 }

// 在launchGame方法中添加更多的预检查
// 例如检查磁盘空间是否足够
File minecraftFolder = minecraftDir.getRoot();
long freeSpace = minecraftFolder.getFreeSpace();
long requiredSpace = 200 * 1024 * 1024; // 假设需要200MB空间
if (freeSpace < requiredSpace) {
    throw new LaunchException("磁盘空间不足，至少需要" + (requiredSpace / (1024 * 1024)) + "MB可用空间");
}
