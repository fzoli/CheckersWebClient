package mill.gui.controll.play.entity;

import java.util.Date;

public class GameTime {
    
    private int time;
    private Date startDate;

    public GameTime(int time, Date startDate) {
        this.time = time;
        this.startDate = startDate;
    }

    public int getTime() {
        return time;
    }

    public Date getStartDate() {
        return startDate;
    }
}