package checkers.gui.view.log.userobject;

import javax.swing.Icon;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.log.LogMenu;
import checkers.gui.view.log.LogMenuFactory;

public class GameLogObject extends LogObject {

    private final GameKey GAME_KEY;
    
    public GameLogObject(GameKey game, Icon icon) {
        this(game, icon, LogMenuFactory.createLogMenu(LogMenuFactory.DELETE));
    }
    
    public GameLogObject(GameKey game, Icon icon, LogMenu menu) {
        super(game.toString(), icon, menu);
        GAME_KEY = game;
    }

    public GameKey getGameKey() {
        return GAME_KEY;
    }
    
}