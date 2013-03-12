package checkers.gui.view.log.userobject;

import checkers.gui.controll.play.entity.DateKey;
import checkers.gui.controll.play.entity.GameKey;

public class DayLogObject extends CalendarLogObject {

    public DayLogObject(int day, int month, int year, String play, String user, String server) {
        super(new GameKey(play, user, server, new DateKey(year, month, day)));
    }
    
}