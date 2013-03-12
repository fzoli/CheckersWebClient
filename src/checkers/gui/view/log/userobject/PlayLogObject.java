package checkers.gui.view.log.userobject;

import javax.swing.Icon;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.core.Core;

public class PlayLogObject extends GameLogObject {

    private static final Icon ICON = Core.getLogIcon("play.png");
    
    public PlayLogObject(String play, String user, String server) {
        super(new GameKey(play, user, server), ICON);
    }
    
}