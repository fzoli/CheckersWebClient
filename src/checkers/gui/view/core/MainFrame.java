package checkers.gui.view.core;

import checkers.gui.controll.Controller;

public abstract class MainFrame extends StatusBarFrame {

    public MainFrame(Controller controller) {
        super(controller);
        setLocationRelativeTo(this);
    }
    
}