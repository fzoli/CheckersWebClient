package mill.gui.view.core;

import mill.gui.controll.Controller;

public abstract class MainFrame extends StatusBarFrame {

    public MainFrame(Controller controller) {
        super(controller);
        setLocationRelativeTo(this);
    }
    
}