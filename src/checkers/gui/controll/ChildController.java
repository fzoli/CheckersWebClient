package checkers.gui.controll;

import checkers.gui.view.core.MyFrame;

public abstract class ChildController implements Controller {

    private final Controller CONTROLLER;
    
    public ChildController(Controller controller){
        CONTROLLER = controller;
    }
    
    public void sendMessage(String message) {
        CONTROLLER.receiveMessage(message);
    }
    
    protected abstract MyFrame getFrame();
    
}