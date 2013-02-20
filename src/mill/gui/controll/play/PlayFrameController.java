package mill.gui.controll.play;

import mill.core.InputValidator;
import mill.gui.controll.ChildController;
import mill.gui.controll.ChildFrameController;
import mill.gui.view.core.StatusBar;
import mill.http.MessageChanger;

public abstract class PlayFrameController extends ChildFrameController {
    
    private final MessageChanger MC;
    
    public PlayFrameController(ChildController controller, MessageChanger mc) {
        super(controller);
        MC = mc;
    }
    
    @Override
    public void receiveMessage(String message) {
        if (message.equals("finish")) finish();
        else sendMessage(message);
    }
    
    public void finish() {
        dispose();
        sendMessage("PlayControllerClosed");
    }
    
    public boolean checkInput(StatusBar sb, String name, String password, boolean gamePassword) {
        if (!InputValidator.isGameNameValid(name)) {
            sendMessage("WrongPlayname");
            sb.setError("Hibás játszma elnevezés!");
            return false;
        }
        boolean validPass = gamePassword ? InputValidator.isGamePasswordValid(password) : InputValidator.isPasswordValid(password);
        if (!validPass) {
            sendMessage("WrongPassword");
            sb.setError("Hibás jelszó!");
            return false;
        }
        return true;
    }
    
    public MessageChanger getMessageChanger() {
        return MC;
    }
    
    public abstract void dispose();
    
}