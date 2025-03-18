#!/bin/bash

# 设置Java路径，如果没有指定则使用系统默认的Java
JAVA_PATH="java"
if [ -n "$JAVA_HOME" ]; then
    JAVA_PATH="$JAVA_HOME/bin/java"
fi

# 设置Minecraft目录路径，如果需要自定义可以取消注释下面的行
# export minecraft.directory="/path/to/your/minecraft"

# 启动应用程序
"$JAVA_PATH" -jar target/MDCL-main-1.0-SNAPSHOT.jar