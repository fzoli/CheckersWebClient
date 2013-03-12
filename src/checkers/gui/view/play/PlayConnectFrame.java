package checkers.gui.view.play;

import javax.swing.JFrame;
import checkers.gui.controll.Controller;

public class PlayConnectFrame extends PlayManageFrame {

    private PlayInputPanel p;
    
    public PlayConnectFrame(Controller controller, JFrame owner, String playName) {
        super(controller, owner);
        p.setPlayName(playName);
    }

    @Override
    protected void initComponents() {
        setTitle("Kapcsolódás");
        p = new PlayInputPanel(this, false);
        add(p);
        pack();
    }

    @Override
    protected PlayInputPanel getInputPanel() {
        return p;
    }
    
}