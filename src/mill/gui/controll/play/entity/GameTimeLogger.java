package mill.gui.controll.play.entity;

import java.util.Map;
import java.util.TreeMap;
import mill.database.Executor;

public class GameTimeLogger {
    
    private static Map<GameKey, Integer> games = new TreeMap<GameKey, Integer>();
    
    public static int getTime(GameKey game) {
        Integer time;
        if (games.containsKey(game)) {
            time = games.get(game);
        }
        else {
            time = Executor.getLastGameTime(game);
            add(game, time);
        }
        return time == null ? 0 : time;
    }
    
    public static void add(GameKey game, Integer time) {
        games.put(game, time);
    }
    
    public static void remove(GameKey game) {
        games.remove(game);
    }
    
}