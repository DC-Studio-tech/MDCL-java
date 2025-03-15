package org.mcdcl.launcher;

import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameLauncher {
    private String javaPath;
    private int maxMemory;
    private String jvmArgs;
    private String gameArgs;
    private Process gameProcess;
    private Launcher launcher;
    private MinecraftDirectory minecraftDir;

    public GameLauncher(String javaPath, int maxMemory, String jvmArgs, String gameArgs) {
        this.javaPath = javaPath;
        this.maxMemory = maxMemory;
        this.jvmArgs = jvmArgs;
        this.gameArgs = gameArgs;
        
        // 初始化Minecraft目录
        this.minecraftDir = new MinecraftDirectory(new File(System.getProperty("user.home"), ".minecraft"));
        
        // 初始化启动器
        LauncherBuilder builder = LauncherBuilder.create();
        if (javaPath != null && !javaPath.isEmpty()) {
            builder.setDefaultJavaPath(javaPath);
        }
        this.launcher = builder.build();
    }

    public void launch() throws IOException, LaunchException {
        // 获取已安装的版本
        File versionsDir = new File(minecraftDir.getRoot(), "versions");
        String[] versionNames = versionsDir.list();
        if (versionNames == null || versionNames.length == 0) {
            throw new LaunchException("No Minecraft version installed");
        }
        Version version = minecraftDir.getVersionById(versionNames[0]);
        
        // 创建启动选项
        LaunchOption option = new LaunchOption(
                version,
                new OfflineAuthenticator("Player"), // 这里使用离线模式，实际应该根据用户登录状态决定
                minecraftDir
        );
        
        // 添加额外的JVM参数
        if (jvmArgs != null && !jvmArgs.trim().isEmpty()) {
            option.setExtraJvmArguments(Arrays.asList(jvmArgs.trim().split("\\s+")));
        }
        
        // 添加额外的游戏参数
        if (gameArgs != null && !gameArgs.trim().isEmpty()) {
            option.setExtraMinecraftArguments(Arrays.asList(gameArgs.trim().split("\\s+")));
        }
        
        // 启动游戏
        org.to2mbn.jmccc.launch.LaunchResult result = launcher.launch(option);
        gameProcess = result.getProcess();
    }

    public void stop() {
        if (gameProcess != null && gameProcess.isAlive()) {
            gameProcess.destroy();
        }
        // JMCCC Launcher不需要显式关闭
    }

    public boolean isRunning() {
        return gameProcess != null && gameProcess.isAlive();
    }
}