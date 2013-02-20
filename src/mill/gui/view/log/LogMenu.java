package mill.gui.view.log;

import javax.swing.JPopupMenu;
import mill.gui.controll.Controller;

public class LogMenu extends JPopupMenu {

    private final Controller CONTROLLER;
    
    public LogMenu(Controller controller) {
        CONTROLLER = controller;
        setMenuVisibility();
    }
    
    private void setMenuVisibility() {
        setOpaque(true);
        setLightWeightPopupEnabled(true);
    }
    
    public void sendMessage(String message) {
        CONTROLLER.receiveMessage(message);
    }
    
}