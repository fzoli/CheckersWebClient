package mill.gui.view.play;

import javax.swing.JFrame;
import mill.gui.controll.Controller;

public abstract class PlayManageFrame extends PlayFrame {
    
    public PlayManageFrame(Controller controller, JFrame owner) {
        super(controller, owner);
    }

    public String getPlayName() {
        return getInputPanel().getPlayName();
    }
    
    public String getPlayPassword() {
        return getInputPanel().getPlayPassword();
    }
    
    public void enableFields() {
        getInputPanel().enableFields();
    }
    
    public void disableFields() {
        getInputPanel().disableFields();
    }

    protected abstract PlayInputPanel getInputPanel();

}