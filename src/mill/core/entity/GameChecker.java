package mill.core.entity;

public class GameChecker {

    private int row, col, player;
    private String type;
    
    public GameChecker(int row, int col, int player, String type) {
        this.row = row;
        this.col = col;
        this.player = player;
        this.type = type;
    }
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public int getPlayer() {
        return player;
    }

    public String getType() {
        return type;
    }
    
}