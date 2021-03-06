package checkers.gui.view.core;

import javax.swing.JFrame;
import checkers.gui.controll.Controller;

public abstract class ChildFrame extends StatusBarFrame {

    private JFrame owner;
    
    public ChildFrame(Controller controller, JFrame owner) {
        super(controller);
        this.owner = owner;
        setLocationRelativeTo(owner);
    }
    
    @Override
    public JFrame getOwner() {
        return owner;
    }
    
}