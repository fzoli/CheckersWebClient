package checkers.gui.view.log.userobject;

import javax.swing.Icon;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.core.Core;
import checkers.gui.view.log.LogMenuFactory;

public class ServerLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("server.png");
    
    public ServerLogObject(String server) {
        super(new GameKey(server), ICON, LogMenuFactory.createLogMenu(LogMenuFactory.REFRESH));
    }
    
}