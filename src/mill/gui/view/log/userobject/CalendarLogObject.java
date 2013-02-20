package mill.gui.view.log.userobject;

import javax.swing.Icon;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.view.core.Core;

public class CalendarLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("calendar.png");
    
    public CalendarLogObject(GameKey game) {
        super(game, ICON);
    }
    
}