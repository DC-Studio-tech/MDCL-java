package org.mcdcl.config;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 用户偏好设置类，用于保存和读取用户配置
 */
public class UserPreferences {
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);
    
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String ACCOUNTS_COUNT_KEY = "accounts_count";
    private static final String ACCOUNT_USERNAME_PREFIX = "account_username_";
    private static final String CURRENT_ACCOUNT_KEY = "current_account";
    
    /**
     * 保存用户凭据
     * 
     * @param username 用户名
     * @param password 密码
     */
    public static void saveCredentials(String username, String password) {
        prefs.put(USERNAME_KEY, username);
        prefs.put(PASSWORD_KEY, password);
        
        // 同时将用户添加到账号列表中（如果不存在）
        addAccount(username);
        // 设置为当前账号
        setCurrentAccount(username);
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
    
    /**
     * 添加账号到账号列表
     * 
     * @param username 用户名
     */
    public static void addAccount(String username) {
        if (username == null || username.isEmpty()) {
            return;
        }
        
        // 检查账号是否已存在
        List<String> accounts = getSavedAccounts();
        if (!accounts.contains(username)) {
            // 添加新账号
            int count = accounts.size();
            prefs.put(ACCOUNT_USERNAME_PREFIX + count, username);
            prefs.putInt(ACCOUNTS_COUNT_KEY, count + 1);
        }
    }
    
    /**
     * 获取保存的所有账号
     * 
     * @return 账号列表
     */
    public static List<String> getSavedAccounts() {
        int count = prefs.getInt(ACCOUNTS_COUNT_KEY, 0);
        List<String> accounts = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            String username = prefs.get(ACCOUNT_USERNAME_PREFIX + i, "");
            if (!username.isEmpty() && !accounts.contains(username)) {
                accounts.add(username);
            }
        }
        
        return accounts;
    }
    
    /**
     * 删除账号
     * 
     * @param username 要删除的用户名
     */
    public static void removeAccount(String username) {
        if (username == null || username.isEmpty()) {
            return;
        }
        
        List<String> accounts = getSavedAccounts();
        if (accounts.contains(username)) {
            accounts.remove(username);
            
            // 重新保存账号列表
            prefs.putInt(ACCOUNTS_COUNT_KEY, accounts.size());
            for (int i = 0; i < accounts.size(); i++) {
                prefs.put(ACCOUNT_USERNAME_PREFIX + i, accounts.get(i));
            }
            
            // 如果删除的是当前账号，则清除当前账号
            if (username.equals(getCurrentAccount())) {
                prefs.remove(CURRENT_ACCOUNT_KEY);
            }
        }
    }
    
    /**
     * 设置当前选中的账号
     * 
     * @param username 当前账号用户名
     */
    public static void setCurrentAccount(String username) {
        if (username != null && !username.isEmpty()) {
            prefs.put(CURRENT_ACCOUNT_KEY, username);
        }
    }
    
    /**
     * 获取当前选中的账号
     * 
     * @return 当前账号用户名，如果没有则返回空字符串
     */
    public static String getCurrentAccount() {
        return prefs.get(CURRENT_ACCOUNT_KEY, getSavedUsername());
    }
}