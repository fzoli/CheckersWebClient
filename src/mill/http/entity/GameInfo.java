package mill.http.entity;

import mill.core.entity.GameChecker;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameInfo {
    
    private Date startDate;
    private String message, gamePending;
    private boolean isPlayerInGame, isGameFinished, isAbleStartStop, isGameStarted, isGameRunning;
    private List<GameChecker> checkers = new ArrayList<GameChecker>();

    public GameInfo(String message, String gamePending, Date startDate, boolean isPlayerInGame, boolean isGameFinished, boolean isAbleStartStop, boolean isGameRunning, boolean isGameStarted) {
        this.message = message;
        this.gamePending = gamePending;
        this.startDate = startDate;
        this.isPlayerInGame = isPlayerInGame;
        this.isGameFinished = isGameFinished;
        this.isAbleStartStop = isAbleStartStop;
        this.isGameRunning = isGameRunning;
        this.isGameStarted = isGameStarted;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void addChecker(GameChecker c) {
        getCheckers().add(c);
    }

    public String getMessage() {
        return message;
    }

    public String getGamePending() {
        return gamePending;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }
    
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    public boolean isPlayerInGame() {
        return isPlayerInGame;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public boolean isAbleStartStop() {
        return isAbleStartStop;
    }

    public List<GameChecker> getCheckers() {
        return checkers;
    }
    
}