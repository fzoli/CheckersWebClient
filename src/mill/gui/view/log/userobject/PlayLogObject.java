package mill.gui.view.log.userobject;

import javax.swing.Icon;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.view.core.Core;

public class PlayLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("play.png");
    
    public PlayLogObject(String play, String user, String server) {
        super(new GameKey(play, user, server), ICON);
    }
    
}