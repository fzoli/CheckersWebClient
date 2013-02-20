package mill.gui.view.log.userobject;

import javax.swing.Icon;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.view.core.Core;
import mill.gui.view.log.LogMenuFactory;

public class TimeLogObject extends GameLogObject {
    
    private static final Icon ICON = Core.getLogIcon("clock.png");
    
    public TimeLogObject(GameKey game) {
        super(game,
              ICON,
              LogMenuFactory.createLogMenu(LogMenuFactory.OPEN_DELETE));
    }
    
}