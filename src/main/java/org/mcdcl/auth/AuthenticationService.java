package org.mcdcl.auth;

import org.mcdcl.config.UserPreferences;

import jmccc.microsoft.MicrosoftAuthenticator;
import jmccc.microsoft.entity.MicrosoftSession;

/**
 * 认证服务类，用于处理用户登录验证
 */
public class AuthenticationService {
    
    // 微软验证的ClientId
    private static final String MICROSOFT_CLIENT_ID = "e1e383f9-59d9-4aa2-bf5e-73fe83b15ba0";
    
    static {
        // 设置微软验证的ClientId
        try {
            java.lang.reflect.Field clientIdField = MicrosoftAuthenticator.class.getDeclaredField("clientId");
            clientIdField.setAccessible(true);
            clientIdField.set(null, MICROSOFT_CLIENT_ID);
        } catch (Exception e) {
            System.err.println("无法设置微软ClientId: " + e.getMessage());
        }
    }
    
    /**
     * 验证用户凭据
     * 
     * @param username 用户名
     * @param password 密码
     * @return 验证是否成功
     * @throws AuthenticationException 如果验证过程中发生错误
     */
    public boolean authenticate(String username, String password) throws AuthenticationException {
        // 这里应该实现实际的认证逻辑
        // 目前仅做简单示例，任何非空用户名和密码都视为有效
        return username != null && !username.isEmpty() 
                && password != null && !password.isEmpty();
    }
    
    /**
     * 使用微软账号进行身份验证
     * 
     * @param callback 用于显示验证链接和代码的回调函数
     * @return 验证成功后的MicrosoftAuthenticator对象
     * @throws AuthenticationException 如果验证过程中发生错误
     */
    public MicrosoftAuthenticator authenticateWithMicrosoft(java.util.function.Consumer<jmccc.microsoft.entity.MicrosoftVerification> callback) throws AuthenticationException {
        try {
            // 尝试使用保存的会话信息登录
            String currentAccount = UserPreferences.getCurrentAccount();
            if (!currentAccount.isEmpty() && UserPreferences.ACCOUNT_TYPE_MICROSOFT.equals(UserPreferences.getAccountType(currentAccount))) {
                MicrosoftSession savedSession = UserPreferences.getMicrosoftSession(currentAccount);
                if (savedSession != null) {
                    try {
                        return MicrosoftAuthenticator.session(savedSession, callback);
                    } catch (Exception e) {
                        // 如果保存的会话无效，则进行新的登录
                        System.out.println("保存的微软会话已过期，需要重新登录");
                    }
                }
            }
            
            // 执行新的登录流程
            return MicrosoftAuthenticator.login(callback);
        } catch (Exception e) {
            throw new AuthenticationException("微软登录失败: " + e.getMessage(), e);
        }
    }
}