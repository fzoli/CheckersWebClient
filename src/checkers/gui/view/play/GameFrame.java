package checkers.gui.view.play;

import javax.swing.JFrame;
import checkers.gui.controll.Controller;

public class GameFrame extends PlayFrame {

    private GamePanel gamePanel;
    
    public GameFrame(Controller controller, JFrame owner) {
        super(controller, owner);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    @Override
    protected void initComponents() {
        gamePanel = new GamePanel(this, true);
        add(gamePanel);
        pack();
    }
    
}
