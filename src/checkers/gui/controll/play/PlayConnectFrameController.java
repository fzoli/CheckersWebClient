package checkers.gui.controll.play;

import javax.swing.SwingWorker;
import checkers.core.PlayRegistry;
import checkers.core.UserRegistry;
import checkers.gui.controll.ChildController;
import checkers.gui.view.core.MyFrame;
import checkers.gui.view.core.StatusBar;
import checkers.gui.view.core.StatusBarFrame;
import checkers.gui.view.play.PlayConnectFrame;
import checkers.http.MessageChanger;
import checkers.http.entity.Play;

public class PlayConnectFrameController extends PlayFrameController {

    private PlayConnectFrame frConnect;
    private GameFrameController cGame;
    private SwingWorker connect;
    private final Play PLAY;
    private final String SERVER, USER;
    private final PlayRegistry PLAY_REGISTRY;
    private final UserRegistry USER_REGISTRY;
    
    public PlayConnectFrameController(ChildController controller, MessageChanger mc, PlayRegistry playRegistry, UserRegistry userRegistry, String server, String user, Play play) {
        super(controller, mc);
        PLAY_REGISTRY = playRegistry;
        USER_REGISTRY = userRegistry;
        PLAY = play;
        SERVER = server;
        USER = user;
        initFrame();
    }

    @Override
    public void receiveMessage(String message) {
        if (message.equals("ButtonEvent")) {
            connectPlay();
        }
        else super.receiveMessage(message);
    }

    @Override
    public void dispose() {
        disposeGameController();
        disposeFrame();
        leavePlay();
    }
    
    private void disposeGameController() {
        if (cGame != null) cGame.disposeController();
    }
    
    private void disposeFrame() {
        if (getFrame() != null) getFrame().dispose();
    }
    
    private StatusBarFrame getListFrame() {
        return (StatusBarFrame) getOwnerFrame();
    }
    
    private void initGameController() {
        cGame = new GameFrameController(this, getMessageChanger(), PLAY_REGISTRY, USER_REGISTRY, SERVER, USER, PLAY.getName(), getOwnerFrame());
    }
    
    private void connectPlay() {
        boolean check = true;
        final StatusBar sb;
        if (PLAY.isProtected()) {
            String name = frConnect.getPlayName();
            String password = frConnect.getPlayPassword();
            sb = frConnect.getStatusBar();
            check = checkInput(sb, name, password, false);
        }
        else {
            sb = getListFrame().getStatusBar();
        }
        connect = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                String name = PLAY.getName();
                String password = PLAY.isProtected() ? frConnect.getPlayPassword() : "";
                boolean success = getMessageChanger().joinPlay(name, password);
                if (success) {
                    initGameController();
                    sendMessage("PlayConnectSuccess");
                    disposeFrame();
                }
                else {
                    String message = "Sikertelen játszmához kapcsolódás!";
                    if (!PLAY.isProtected()) {
                        sendMessage("PlayControllerClosed");
                        sb.setError(message);
                    }
                    else {
                        sb.setError(message);
                    }
                }
                return null;
            }
            
        };
        if (check) {
            sb.setProgress("Kapcsolódás játszmához...");
            connect.execute();
        }
    }
    
    private void leavePlay() {
        cancelConnect();
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                getMessageChanger().leavePlay();
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void cancelConnect() {
        if (connect != null && connect.getState() == SwingWorker.StateValue.STARTED)
            connect.cancel(true);
    }
    
    private void initFrame() {
        if (PLAY.isProtected()) {
            frConnect = new PlayConnectFrame(this, getOwnerFrame(), PLAY.getName());
            frConnect.setVisible(true);
        }
        else connectPlay();
    }

    @Override
    protected MyFrame getFrame() {
        return frConnect;
    }
    
}