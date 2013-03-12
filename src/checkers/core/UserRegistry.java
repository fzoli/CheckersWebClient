package checkers.core;

import java.util.ArrayList;
import java.util.List;
import checkers.database.Executor;
import checkers.database.entity.UserData;
import checkers.database.entity.UserPasswordData;

public class UserRegistry {
    
    private List<UserData> users;

    public UserRegistry() {
        updateList();
    }
    
    public void setUserPassword(String server, String user, String password) {
        Executor.setUserPassword(server, user, password);
        updateList();
    }
    
    public void setUserPassVisibility(String user, boolean showPassword) {
        Executor.setUserPassVisibility(user, showPassword);
        updateList();
    }
    
    public void setUserLog(String user, boolean enableLog) {
        Executor.setUserLog(user, enableLog);
        updateList();
    }
    
    public boolean isPasswordEquals(String server, String user, String password) {
        return Executor.isHashEquals(password, getStoredPassword(server, user));
    }
    
    public List<UserData> getVisibleUsers() {
        List<UserData> d = new ArrayList<UserData>();
        for (UserData u : users)
            if (u.isEnableLog()) d.add(u);
        return d;
    }
    
    public boolean addUser(String username, boolean showPassword) {
        boolean b = Executor.addUser(username, showPassword);
        if (b) updateList();
        return b;
    }
    
    public boolean isUserExists(String name) {
        return getUserData(name) != null;
    }
    
    public UserData getUserData(String name) {
        for (UserData u : users) {
            if (u.getName().equalsIgnoreCase(name)) return u;
        }
        return null;
    }
    
    public UserPasswordData getPasswordData(String server, String user) {
        UserData ud = getUserData(user);
        if (ud == null) return null;
        return ud.getPasswordData(server);
    }
    
    private String getStoredPassword(String server, String user) {
        return getPasswordData(server, user).getPassword();
    }
    
    private void updateList() {
        users = Executor.getUserDataList();
    }
    
}