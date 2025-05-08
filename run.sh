#!/bin/bash

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java运行环境，请先安装Java 21或更高版本"
    exit 1
fi

# 获取Java版本
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
version_number=$(echo $java_version | cut -d'.' -f1)

# 检查Java版本
if [ "$version_number" -lt "21" ]; then
    echo "错误: Java版本过低，需要Java 21或更高版本"
    echo "当前版本: $java_version"
    exit 1
fi

# 运行JAR文件
java --module-path . \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.media \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.io=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED \
     --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED \
     --add-exports javafx.base/com.sun.javafx.runtime=ALL-UNNAMED \
     -jar MDCL-main-1.0-SNAPSHOT-jar-with-dependencies.jar