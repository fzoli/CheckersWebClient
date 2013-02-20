package mill.gui.view.play;

import javax.swing.JFrame;
import mill.gui.controll.Controller;

public class PlayCreateFrame extends PlayManageFrame {

    private PlayInputPanel p;
    
    public PlayCreateFrame(Controller controller, JFrame owner) {
        super(controller, owner);
    }

    @Override
    protected void initComponents() {
        setTitle("Létrehozás");
        p = new PlayInputPanel(this);
        add(p);
        pack();
    }

    @Override
    protected PlayInputPanel getInputPanel() {
        return p;
    }
    
}