package mill.gui.controll.play.entity;

import java.util.List;

public class GameData {
    
    private List<GameLogData> logs;

    public GameData(List<GameLogData> logs) {
        this.logs = logs;
    }

    public void addLogData(GameLogData logData) {
        logs.add(logData);
    }
    
    public GameLogData getLogData(int index) {
        return logs.get(index);
    }
    
    public int getSize() {
        return logs.size();
    }
    
}