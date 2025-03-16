package org.mcdcl.auth;

/**
 * 认证异常类，用于表示认证过程中发生的异常
 */
public class AuthenticationException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建一个新的认证异常
     */
    public AuthenticationException() {
        super();
    }
    
    /**
     * 创建一个带有指定详细消息的认证异常
     * 
     * @param message 详细消息
     */
    public AuthenticationException(String message) {
        super(message);
    }
    
    /**
     * 创建一个带有指定详细消息和原因的认证异常
     * 
     * @param message 详细消息
     * @param cause 原因
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 创建一个带有指定原因的认证异常
     * 
     * @param cause 原因
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}