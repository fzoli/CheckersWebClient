package mill.gui.view.log.userobject;

import mill.gui.controll.play.entity.DateKey;
import mill.gui.controll.play.entity.GameKey;

public class YearLogObject extends CalendarLogObject {

    public YearLogObject(int year, String play, String user, String server) {
        super(new GameKey(play, user, server, new DateKey(year)));
    }
    
}