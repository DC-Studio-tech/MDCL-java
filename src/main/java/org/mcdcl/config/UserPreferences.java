package org.mcdcl.config;

import java.util.prefs.Preferences;

/**
 * 用户偏好设置类，用于保存和读取用户配置
 */
public class UserPreferences {
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);
    
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    
    /**
     * 保存用户凭据
     * 
     * @param username 用户名
     * @param password 密码
     */
    public static void saveCredentials(String username, String password) {
        prefs.put(USERNAME_KEY, username);
        prefs.put(PASSWORD_KEY, password);
    }
    
    /**
     * 获取保存的用户名
     * 
     * @return 保存的用户名，如果没有则返回空字符串
     */
    public static String getSavedUsername() {
        return prefs.get(USERNAME_KEY, "");
    }
    
    /**
     * 获取保存的密码
     * 
     * @return 保存的密码，如果没有则返回空字符串
     */
    public static String getSavedPassword() {
        return prefs.get(PASSWORD_KEY, "");
    }
    
    /**
     * 清除保存的凭据
     */
    public static void clearCredentials() {
        prefs.remove(USERNAME_KEY);
        prefs.remove(PASSWORD_KEY);
    }
}