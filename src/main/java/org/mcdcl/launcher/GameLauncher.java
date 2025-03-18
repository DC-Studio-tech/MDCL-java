package org.mcdcl.launcher;

import java.io.File;
import java.io.IOException;

import org.mcdcl.config.UserPreferences;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;

public class GameLauncher {
    private String javaPath;
    private int maxMemory;
    private String jvmArgs;
    private String gameArgs;
    private Process gameProcess;
    private Launcher launcher;
    private MinecraftDirectory minecraftDir;

    // 构造函数
    public GameLauncher(String javaPath, int maxMemory, String jvmArgs, String gameArgs) {
        this.javaPath = javaPath;
        this.maxMemory = maxMemory;
        this.jvmArgs = jvmArgs;
        this.gameArgs = gameArgs;
        
        // 使用用户主目录下的.minecraft目录作为默认目录
        String userHome = System.getProperty("user.home");
        File minecraftFolder = new File(userHome, ".minecraft");
        
        // 如果指定了自定义目录，则使用自定义目录
        String customPath = System.getProperty("minecraft.directory");
        if (customPath != null && !customPath.isEmpty()) {
            minecraftFolder = new File(customPath);
        }
        
        this.minecraftDir = new MinecraftDirectory(minecraftFolder);
        this.launcher = LauncherBuilder.create().build();
    }

    // 启动游戏的方法
    public void launchGame(String versionName) throws LaunchException, IOException {
        // 使用版本名称获取版本对象
        Version version = org.to2mbn.jmccc.version.parsing.Versions.resolveVersion(minecraftDir, versionName);
        if (version == null) {
            throw new LaunchException("找不到版本: " + versionName);
        }
        
        // 获取当前选定的账号，如果没有则使用默认值
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
        if (gameArgs != null && !gameArgs.isEmpty()) {
            for (String arg : gameArgs.split(" ")) {
                option.extraMinecraftArguments().add(arg);
            }
        }
        
        // 启动游戏
        gameProcess = launcher.launch(option);
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