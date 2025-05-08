@echo off
setlocal

:: 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java运行环境，请先安装Java 21或更高版本
    pause
    exit /b 1
)

:: 获取Java版本
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)
set JAVA_VERSION=%JAVA_VERSION:"=%
for /f "delims=. tokens=1" %%a in ("%JAVA_VERSION%") do set VERSION_NUMBER=%%a

:: 检查Java版本
if %VERSION_NUMBER% LSS 21 (
    echo 错误: Java版本过低，需要Java 21或更高版本
    echo 当前版本: %JAVA_VERSION%
    pause
    exit /b 1
)

:: 运行JAR文件
java --module-path . ^
     --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.media ^
     --add-opens java.base/java.lang=ALL-UNNAMED ^
     --add-opens java.base/java.io=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED ^
     --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED ^
     --add-exports javafx.base/com.sun.javafx.runtime=ALL-UNNAMED ^
     -jar MDCL-main-1.0-SNAPSHOT-jar-with-dependencies.jar

pause