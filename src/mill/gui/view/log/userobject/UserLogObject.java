package mill.gui.view.log.userobject;

import javax.swing.Icon;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.view.core.Core;

public class UserLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("user.png");
    
    public UserLogObject(String user, String server) {
        super(new GameKey(user, server), ICON);
    }
    
}