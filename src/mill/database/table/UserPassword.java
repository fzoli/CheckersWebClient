package mill.database.table;

import java.io.Serializable;

public class UserPassword implements Serializable {

    private User user;
    private Server server;
    private int id, passwordLength;
    private String password;

    public UserPassword() {
    }
    
    public UserPassword(Server server, User user, String password, int passwordLength) {
        this.user = user;
        this.server = server;
        this.password = password;
        this.passwordLength = passwordLength;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}