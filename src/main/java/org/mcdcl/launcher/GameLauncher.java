package org.mcdcl.launcher;

import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;
import org.to2mbn.jmccc.version.VersionParser;

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

    // 构造函数
    public GameLauncher(String javaPath, int maxMemory, String jvmArgs, String gameArgs) {
        this.javaPath = javaPath;
        this.maxMemory = maxMemory;
        this.jvmArgs = jvmArgs;
        this.gameArgs = gameArgs;
        this.minecraftDir = new MinecraftDirectory(new File("/Users/zhuangweiwei/.minecraft/versions/骗粉服整合包（必装）")); // 替换为实际的Minecraft目录路径
        this.launcher = LauncherBuilder.create().build();
    }

    // 启动游戏的方法
    public void launchGame(String versionName) throws LaunchException, IOException {
        // 使用 VersionParser 解析版本号
        Version version = VersionParser.parseVersion(versionName);

        // 创建 LaunchOption
        LaunchOption option = new LaunchOption(version, new OfflineAuthenticator("testMCDL"), minecraftDir);

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