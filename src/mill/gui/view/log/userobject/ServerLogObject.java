package mill.gui.view.log.userobject;

import javax.swing.Icon;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.view.core.Core;
import mill.gui.view.log.LogMenuFactory;

public class ServerLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("server.png");
    
    public ServerLogObject(String server) {
        super(new GameKey(server), ICON, LogMenuFactory.createLogMenu(LogMenuFactory.REFRESH));
    }
    
}