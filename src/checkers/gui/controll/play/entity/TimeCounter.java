package checkers.gui.controll.play.entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.Timer;
import checkers.gui.controll.Controller;

public class TimeCounter {
    
    private int counter = 0;
    private GameKey gameKey;
    private final Controller C;
    
    private Timer timer = new Timer(1000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            counter++;
            C.receiveMessage("TimeCounterEvent");
        }
        
    });

    public TimeCounter(Controller controller) {
        C = controller;
    }
    
    public int getCounter() {
        return counter;
    }
    
    public void setGameKey(GameKey gameKey) {
        this.gameKey = gameKey;
        counter = getLoggedGameTime();
    }
    
    public static String dateFormat(int time) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(new Date(time * 1000));
    }
    
    private int getLoggedGameTime() {
        return GameTimeLogger.getTime(gameKey);
    }
    
    public void addGameTimeLog() {
        GameTimeLogger.add(gameKey, counter);
    }
    
    public void removeGameTimeLog() {
        GameTimeLogger.remove(gameKey);
    }
    
    public void start() {
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    public String getTime() {
        return dateFormat(counter);
    }
    
}