package checkers.core;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import checkers.database.Executor;
import checkers.gui.controll.play.entity.GameData;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.controll.play.entity.GameLogData;

public class PlayRegistry {

    List<GameKey> games;
    Map<GameKey, GameData> datas = new TreeMap<GameKey, GameData>();
    
    public PlayRegistry() {
        load();
    }
    
    public List<GameKey> getGameList() {
        return games;
    }
    
    public void addPlay(GameKey play) {
        if (Executor.addPlay(play))
            games.add(play);
    }
    
    public boolean delPlay(GameKey play) {
        if(Executor.delGame(play)) {
            load();
            return true;
        }
        return false;
    }
    
    public void addGameLog(GameKey play, GameLogData log) {
        if(Executor.addGameLog(play, log)) {
            GameData gd = getGameData(play);
            if (gd != null) gd.addLogData(log);
        }
    }
    
    public GameData getGameData(GameKey play) {
        if (datas.containsKey(play)) {
            return datas.get(play);
        }
        else {
            GameData data = Executor.getGameData(play);
            datas.put(play, data);
            return data;
        }
    }
    
    private void load() {
        games = Executor.getGameKeyList();
    }
    
}