package org.mcdcl.exception;

/**
 * 表示Minecraft目录相关操作异常的自定义异常类
 */
public class MinecraftDirectoryException extends Exception {
    
    /**
     * 创建一个带有指定错误消息的异常
     * 
     * @param message 错误消息
     */
    public MinecraftDirectoryException(String message) {
        super(message);
    }
    
    /**
     * 创建一个带有指定错误消息和原因的异常
     * 
     * @param message 错误消息
     * @param cause 原始异常
     */
    public MinecraftDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }
}