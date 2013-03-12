package checkers.gui.view.log.userobject;

import javax.swing.Icon;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.core.Core;
import checkers.gui.view.log.LogMenuFactory;

public class TimeLogObject extends GameLogObject {
    
    private static final Icon ICON = Core.getLogIcon("clock.png");
    
    public TimeLogObject(GameKey game) {
        super(game,
              ICON,
              LogMenuFactory.createLogMenu(LogMenuFactory.OPEN_DELETE));
    }
    
}