package checkers.database.entity;

import java.util.Map;

public class UserData {
    
    private String name;
    private Map<String, UserPasswordData> passwords;
    private boolean showPassword, enableLog;

    public UserData(String name, Map<String, UserPasswordData> passwords, boolean showPassword, boolean enableLog) {
        this.name = name;
        this.passwords = passwords;
        this.showPassword = showPassword;
        this.enableLog = enableLog;
    }

    public String getName() {
        return name;
    }

    public boolean isShowPassword() {
        return showPassword;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public UserPasswordData getPasswordData(String server) {
        return passwords.get(server);
    }
    
    public boolean isPasswordDataEmpty() {
        return passwords.isEmpty();
    }
    
}