package checkers.http.entity;

import java.util.ArrayList;
import java.util.List;

public class Info {

    private int timeout;
    private String serverUrl, user, lastAction;
    private boolean isPlayerInGame, isAdmin;
    private List<Play> playes = new ArrayList<Play>();

    public Info(String serverUrl, String user, String lastAction, int timeout, boolean isPlayerInGame, boolean isAdmin) {
        this.serverUrl = serverUrl;
        this.user = user;
        this.lastAction =lastAction;
        this.timeout = timeout;
        this.isPlayerInGame = isPlayerInGame;
        this.isAdmin = isAdmin;
    }
    
    public void addPlay(Play play) {
        getPlayList().add(play);
    }
    
    public List<Play> getPlayList() {
        return playes;
    }

    public String getServerUrl() {
        return serverUrl;
    }
    
    public int getTimeout() {
        return timeout;
    }

    public String getUser() {
        return user;
    }

    public String getLastAction() {
        return lastAction;
    }

    public boolean isPlayerInGame() {
        return isPlayerInGame;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    
    public boolean isUserOwner() {
        for (Play p : playes) {
            if (p.getOwner().equals(getUser()))
                return true;
        }
        return false;
    }
    
}