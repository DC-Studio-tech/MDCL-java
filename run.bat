@echo off
REM 设置Java路径，如果没有指定则使用系统默认的Java
set JAVA_PATH=java
if defined JAVA_HOME (
    set JAVA_PATH=%JAVA_HOME%\bin\java
)

REM 设置Minecraft目录路径，如果需要自定义可以取消注释下面的行
REM set minecraft.directory=D:\path\to\your\minecraft

REM 启动应用程序
"%JAVA_PATH%" -jar target\MDCL-main-1.0-SNAPSHOT.jar
pause