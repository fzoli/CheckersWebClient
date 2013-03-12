package checkers.database.table;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name;
    private boolean enableLog, showPassword;

    public User() {
    }
    
    public User(String name, boolean showPassword) {
        this.name = name;
        this.showPassword = showPassword;
        this.enableLog = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }
    
}