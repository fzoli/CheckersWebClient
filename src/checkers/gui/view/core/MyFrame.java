package checkers.gui.view.core;

import java.awt.Dimension;
import javax.swing.JFrame;
import checkers.gui.controll.Controller;

public class MyFrame extends JFrame {

    private final Controller CONTROLLER;
    
    public MyFrame(Controller controller) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.CONTROLLER = controller;
        Core.setLAF();
        setIcon();
    }
        
    public void pack(int marginWidth, int marginHeight) {
        pack();
        Dimension size = getSize();
        setSize(size.width + marginWidth, size.height + marginHeight);
    }
    
    public void sendMessage(String message) {
        CONTROLLER.receiveMessage(message);
    }
    
    private void setIcon() {
        setIconImage(Core.getCheckersImage());
    }
    
}