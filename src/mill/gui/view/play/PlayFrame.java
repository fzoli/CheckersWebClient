package mill.gui.view.play;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import mill.gui.controll.Controller;
import mill.gui.view.core.ChildFrame;

public abstract class PlayFrame extends ChildFrame {
    
    public PlayFrame(Controller controller, JFrame owner) {
        super(controller, owner);
        setExitEvent();
    }
    
    private void setExitEvent() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                sendMessage("finish");
            }
            
        });
    }

}