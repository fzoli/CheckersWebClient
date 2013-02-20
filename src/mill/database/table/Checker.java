package mill.database.table;

import java.io.Serializable;

public class Checker implements Serializable {
    
    private long id;
    private Log log;
    private String type;
    private int player, row, col;

    public Checker() {
        super(); //nem mintha kellene, csak jobban néz ki, ha nem üres :D
    }
    
    public Checker(Log log, String type, int player, int row, int col) {
        this.log = log;
        this.player = player;
        this.row = row;
        this.col = col;
        this.type = type;
    }   
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
}