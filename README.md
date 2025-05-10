# MDCL Minecraft启动器

## 简介

MDCL是一个基于Java开发的Minecraft启动器，使用JMCCC库实现Minecraft的启动功能。它提供了以下主要特性：

- 支持多版本Minecraft的安装和启动
- 支持离线模式和正版账户登录
- 自定义Java路径和内存设置
- 可配置JVM和游戏参数
- 支持直接连接服务器启动
- 实时显示启动进度
- 游戏日志输出监控

## 技术特性

- 基于JMCCC库开发，提供稳定可靠的启动支持
- 使用Java 21开发，提供现代化的性能和特性
- 支持自定义启动配置和参数设置
- 内置进度提示和错误处理机制

## 系统要求

### 运行环境
- 操作系统：Windows/Linux/macOS
- JRE或JDK 21或更高版本
- 足够的磁盘空间用于存储Minecraft游戏文件

## 开发环境要求

### 构建工具
- JDK 21或更高版本
- Maven 3.6或更高版本
- Git（用于获取源码）

### 打包步骤

1. 克隆或下载本项目到本地
2. 进入项目根目录
3. 执行Maven打包命令：

```bash
mvn clean package
```

打包完成后，可执行JAR文件将生成在`target`目录下，文件名为`MDCL-main-1.0-SNAPSHOT.jar`。

## 安装和运行

### 方法一：使用脚本运行（推荐）

1. Windows系统：双击运行`run.bat`
2. Linux/macOS系统：在终端中执行
```bash
chmod +x run.sh  # 设置执行权限
./run.sh
```

### 方法二：直接运行JAR文件

```bash
# 基本启动
java -jar MDCL-main-1.0-SNAPSHOT.jar

# 指定最大内存启动（推荐）
java -Xmx2G -jar MDCL-main-1.0-SNAPSHOT.jar
```

### 配置说明

启动器首次运行时会在同目录下生成`settings.json`配置文件，你可以在此文件中修改：
- Java路径
- 默认最大内存
- 游戏目录
- JVM参数
- 游戏参数

## 常见问题

### 1. Java相关问题

#### 找不到Java
- 确保已安装JDK 21或更高版本
- 正确设置JAVA_HOME环境变量
- 在启动器设置中指定正确的Java路径

#### 版本兼容性
- Minecraft 1.17及以上版本需要Java 17或更高版本
- 较旧版本的Minecraft推荐使用Java 8

### 2. 游戏启动问题

#### 无法启动游戏
- 检查Minecraft目录路径是否正确
- 确认已安装要启动的游戏版本
- 查看启动日志获取详细错误信息

#### 游戏崩溃
- 检查是否分配了足够的内存
- 确认Java版本与游戏版本兼容
- 检查游戏目录权限是否正确

### 3. 性能优化

#### 内存设置
- 通过启动参数设置最大内存：
```bash
java -Xmx4G -jar MDCL-main-1.0-SNAPSHOT.jar
```

#### JVM优化参数
推荐的JVM参数设置：
```bash
java -Xmx4G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -jar MDCL-main-1.0-SNAPSHOT.jar
```

## 开发计划

### 高优先级
1. 资源包管理
   - 资源包列表展示
   - 在线资源包搜索和下载
   - 本地资源包导入和管理

2. 模组管理
   - 模组列表展示和搜索
   - 模组版本兼容性检查
   - 模组配置管理

3. 整合包支持
   - 整合包安装和导入
   - 整合包版本管理
   - 自动依赖处理

### 中优先级
4. 多语言支持
   - 英文界面支持
   - 语言切换功能
   - 语言包管理

### 低优先级
5. 界面优化
   - 主题切换
   - 自定义界面布局

6. 性能优化
   - 启动速度优化
   - 资源下载优化

## 参与贡献

我们欢迎各种形式的贡献，包括但不限于：

- 提交问题和建议
- 提交代码改进
- 完善文档内容
- 修复bug
- 添加新功能

### 贡献步骤

1. Fork本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的改动 (`git commit -m 'Add some AmazingFeature'`)
4. 将您的改动推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交Pull Request

## 许可证

本项目基于MIT许可证开源。
