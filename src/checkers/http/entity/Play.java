package checkers.http.entity;

public class Play {
    
    private String name, owner, state;
    private int playerNumber;
    private boolean isProtected;
    
    public Play(String name, String owner, String state, int playerNumber, boolean isProtected) {
        this.name = name;
        this.owner = owner;
        this.state = state;
        this.playerNumber = playerNumber;
        this.isProtected = isProtected;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getState() {
        return state;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isProtected() {
        return isProtected;
    }
    
}