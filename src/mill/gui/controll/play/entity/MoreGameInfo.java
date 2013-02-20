package mill.gui.controll.play.entity;

import java.util.List;
import mill.core.entity.GameChecker;
import mill.http.entity.GameInfo;

public class MoreGameInfo {

    private final GameInfo GAME_INFO;
    
    public MoreGameInfo(GameInfo gameInfo) {
        GAME_INFO = gameInfo;
    }
    
    public GameInfo getGameInfo() {
        return GAME_INFO;
    }
    
    public int getHit(int player) {
        return getHit(getGameInfo().getCheckers(), player);
    }
    
    public static int getHit(List<GameChecker> checkers, int player) {
        int p = player == 1 ? 2 : 1;
        return 12 - getCheckerNumber(checkers, p);
    }
    
    private static int getCheckerNumber(List<GameChecker> checkers, int player) {
        int counter = 0;
        for (GameChecker c : checkers) {
            if (c.getPlayer() == player) counter++;
        }
        return counter;
    }
    
}