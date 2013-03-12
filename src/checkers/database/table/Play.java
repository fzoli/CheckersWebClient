package checkers.database.table;

import java.io.Serializable;
import java.util.Date;

public class Play implements Serializable {

    private long id;
    private Server server;
    private String name;
    private Date start;
    private User user;

    public Play() {
    }

    public Play(Server server, User user, String name, Date start) {
        this.server = server;
        this.name = name;
        this.user = user;
        this.start = start;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
    
}