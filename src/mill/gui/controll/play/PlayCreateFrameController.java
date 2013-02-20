package mill.gui.controll.play;

import javax.swing.SwingWorker;
import mill.gui.controll.ChildController;
import mill.gui.view.core.MyFrame;
import mill.gui.view.core.StatusBar;
import mill.gui.view.play.PlayCreateFrame;
import mill.http.MessageChanger;
import mill.http.entity.Message;

public class PlayCreateFrameController extends PlayFrameController {

    private PlayCreateFrame frCreate;
    SwingWorker create;
    
    public PlayCreateFrameController(ChildController controller, MessageChanger mc) {
        super(controller, mc);
        initFrame();
    }

    @Override
    public void receiveMessage(String message) {
        if (message.equals("ButtonEvent")) {
            createPlay();
        }
        else super.receiveMessage(message);
    }
    
    @Override
    public void dispose() {
        getFrame().dispose();
        cancelCreatePlay();
    }
    
    private void playCreateFinished(Message message) {
        if (message.isSuccess()) {
            finish();
        }
        else {
            frCreate.enableFields();
            StatusBar sb = frCreate.getStatusBar();
            sb.setError(message.getMessage());
        }
    }
    
    private void createPlay() {
        final String name = frCreate.getPlayName();
        final String password = frCreate.getPlayPassword();
        StatusBar sb = frCreate.getStatusBar();
        if (checkInput(sb, name, password, true)) {
            frCreate.disableFields();
            sb.setProgress("Játszma létrehozása...");
            create = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    Message m = getMessageChanger().addPlay(name, password);
                    playCreateFinished(m);
                    return null;
                }

            };
            create.execute();
        }
    }
    
    private void cancelCreatePlay() {
        if (create != null && create.getState() == SwingWorker.StateValue.STARTED)
            create.cancel(true);
    }
    
    private void initFrame() {
        frCreate = new PlayCreateFrame(this, getOwnerFrame());
        frCreate.setVisible(true);
    }

    @Override
    protected MyFrame getFrame() {
        return frCreate;
    }
    
}