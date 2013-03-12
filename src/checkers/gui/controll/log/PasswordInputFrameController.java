package checkers.gui.controll.log;

import checkers.core.InputValidator;
import checkers.gui.controll.ChildController;
import checkers.gui.controll.ChildFrameController;
import checkers.gui.view.log.PasswordInputFrame;

public class PasswordInputFrameController extends ChildFrameController {

    private PasswordInputFrame fr;
    
    public PasswordInputFrameController(ChildController controller) {
        super(controller);
        initFrame();
    }

    @Override
    public PasswordInputFrame getFrame() {
        return fr;
    }

    @Override
    public void receiveMessage(String message) {
        if (message.equals("CloseEvent")) disposeController();
        else if (message.equals("InputEvent")) inputEvent();
    }
    
    public void setInputEnabled(boolean enabled) {
        fr.setInputEnabled(enabled);
    }
    
    public String getPassword() {
        return fr.getPassword();
    }
    
    private void inputEvent() {
        if (InputValidator.isPasswordValid(getPassword()))
            sendMessage("PasswordInputEvent");
        else sendMessage("WrongPasswordEvent");
    }
    
    public void dispose() {
        fr.dispose();
    }
    
    private void disposeController() {
        sendMessage("PasswordInputDisposed");
    }
    
    private void initFrame() {
        fr = new PasswordInputFrame(this, getOwnerFrame());
        fr.setVisible(true);
    }
    

    
}