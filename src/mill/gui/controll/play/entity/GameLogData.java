package mill.gui.controll.play.entity;

import java.util.List;
import mill.core.entity.GameChecker;

public class GameLogData {
    
    private List<GameChecker> checkers;
    private String message;
    private int time;

    public GameLogData(List<GameChecker> checkers, String message, int time) {
        this.checkers = checkers;
        this.message = message;
        this.time = time;
    }

    public List<GameChecker> getCheckers() {
        return checkers;
    }

    public String getMessage() {
        return message;
    }

    public int getTime() {
        return time;
    }
    
}