# MDCL Minecraft启动器

## 简介

MDCL是一个基于Java开发的Minecraft启动器，使用JMCCC库实现Minecraft的启动功能。

## 打包说明

### 环境要求

- JDK 21或更高版本
- Maven 3.6或更高版本

### 打包步骤

1. 克隆或下载本项目到本地
2. 进入项目根目录
3. 执行Maven打包命令：

```bash
mvn clean package
```

打包完成后，可执行JAR文件将生成在`target`目录下，文件名为`MDCL-main-1.0-SNAPSHOT.jar`。

## 运行说明

### 方法一：使用脚本运行

#### Windows系统

双击运行`run.bat`脚本。

#### macOS/Linux系统

```bash
chmod +x run.sh  # 赋予执行权限（仅首次运行需要）
./run.sh
```

### 方法二：直接运行JAR文件

```bash
java -jar target/MDCL-main-1.0-SNAPSHOT.jar
```

## 自定义Minecraft目录

默认情况下，启动器使用用户主目录下的`.minecraft`文件夹作为Minecraft目录。如果需要自定义Minecraft目录，可以通过以下方式设置：

### 方法一：修改启动脚本

编辑`run.sh`或`run.bat`脚本，取消注释相关行并设置自定义路径。

### 方法二：设置系统属性

```bash
java -Dminecraft.directory=/path/to/your/minecraft -jar target/MDCL-main-1.0-SNAPSHOT.jar
```

## 常见问题

### 1. 找不到Java

确保已安装JDK 21或更高版本，并正确设置了JAVA_HOME环境变量。

### 2. 无法启动游戏

检查Minecraft目录路径是否正确，以及是否已安装了要启动的游戏版本。

### 3. 内存不足

可以通过修改启动脚本，增加JVM内存参数来解决：

```bash
java -Xmx2G -jar target/MDCL-main-1.0-SNAPSHOT.jar
```

## 许可证

本项目基于MIT许可证开源。