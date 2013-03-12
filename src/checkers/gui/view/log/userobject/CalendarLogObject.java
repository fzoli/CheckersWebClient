package checkers.gui.view.log.userobject;

import javax.swing.Icon;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.core.Core;

public class CalendarLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("calendar.png");
    
    public CalendarLogObject(GameKey game) {
        super(game, ICON);
    }
    
}