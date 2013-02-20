package mill.gui.controll;

import javax.swing.SwingWorker;
import mill.gui.view.GameRulesFrame;
import mill.gui.view.core.MyFrame;
import mill.http.MessageChanger;

public class GameRulesFrameController extends ChildFrameController {

    private GameRulesFrame frRules;
    
    public GameRulesFrameController(ChildController controller, MessageChanger mc) {
        super(controller);
        initRulesFrame(mc);
    }

    @Override
    protected MyFrame getFrame() {
        return frRules;
    }

    @Override
    public void receiveMessage(String message) {
        ;
    }

    private void initRulesFrame(final MessageChanger mc) {
        frRules = new GameRulesFrame(this, getOwnerFrame());
        frRules.getStatusBar().setProgress("Oldal betöltése...");
        
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                frRules.updateHtmlCode(mc.getGameRule(), mc.getGameUrl());
                frRules.getStatusBar().reset();
                return null;
            }
            
        };
        sw.execute();
    }
    
}