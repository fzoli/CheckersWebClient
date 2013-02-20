package mill.gui.controll;

import javax.swing.SwingWorker;
import mill.core.PlayRegistry;
import mill.core.ServerRegistry;
import mill.core.UserRegistry;
import mill.gui.controll.list.ListDownloader;
import mill.gui.controll.list.ListTableModel;
import mill.gui.controll.event.ListEvent;
import mill.gui.controll.play.PlayConnectFrameController;
import mill.gui.controll.play.PlayCreateFrameController;
import mill.gui.controll.play.PlayFrameController;
import mill.gui.view.ListFrame;
import mill.gui.view.core.MyFrame;
import mill.http.MessageChanger;
import mill.http.entity.Info;
import mill.http.entity.Play;

public class ListFrameController extends ChildController {

    private Info info;
    private ListFrame frList;
    private final MessageChanger MC;
    private final ServerRegistry SERVER_REGISTRY;
    private final PlayRegistry PLAY_REGISTRY;
    private final UserRegistry USER_REGISTRY;
    private ListTableModel listTableModel;
    private ListEvent listEvent;
    private ListDownloader listDownloader;
    private Play lastSelectedPlay;
    private boolean signedIn;
    private boolean isPlayFrameOpened;
    private PlayFrameController cPlay;

    public ListFrameController(Controller controller, MessageChanger mc, ServerRegistry serverRegistry, PlayRegistry playRegistry, UserRegistry userRegistry) {
        super(controller);
        MC = mc;
        SERVER_REGISTRY = serverRegistry;
        PLAY_REGISTRY = playRegistry;
        USER_REGISTRY = userRegistry;
        initListFrame();
        startWaitListEvent();
    }

    public String getUser() {
        return info.getUser();
    }
    
    public String getServer() {
        return SERVER_REGISTRY.getServerFromUrl(info.getServerUrl()).getDomain();
    }
    
    @Override
    protected MyFrame getFrame() {
        return frList;
    }
    
    public void setListFrameVisibility(boolean value) {
        frList.setVisible(value);
    }
    
    @Override
    public void receiveMessage(String message) {
        if (message.equals("PlaySelected")) playSelected();
        else if (message.equals("ListEvent")) downloadList();
        else if (message.equals("ListDownloaded")) updateList();
        else if (message.equals("DelPlay")) delPlay();
        else if (message.equals("ResetPlay")) resetPlay();
        else if (message.equals("CreatePlay")) createPlay();
        else if (message.equals("ConnectPlay")) connectPlay();
        else if (message.equals("PlayControllerClosed")) playFrameClosed();
        else if (message.equals("PlayConnectSuccess")) playConnectSuccess();
        else if (message.equals("GameRulesEvent")) showGameRules();
        else if (message.equals("LogControllerOpened")) logControllerOpened();
        else if (message.equals("LogControllerClosed")) logControllerClosed();
        else sendMessage(message);
    }
    
    private void logControllerOpened() {
        frList.setLogEnabled(false);
        sendMessage("LogControllerOpened");
    }
    
    private void logControllerClosed() {
        frList.setLogEnabled(true);
        sendMessage("LogControllerClosed");
    }
    
    private void showGameRules() {
        new GameRulesFrameController(this, MC);
    }
    
    private void playConnectSuccess() {
        setListFrameVisibility(false);
    }
    
    private void playFrameClosed() {
        if (MC.isConnected()) {
            isPlayFrameOpened = false;
            setListFrameVisibility(true);
            downloadList();
        }
    }
    
    private void createPlay() {
        isPlayFrameOpened = true;
        disablePlayAndCreateButtons();
        cPlay = new PlayCreateFrameController(this, MC);
    }
    
    private void connectPlay() {
        isPlayFrameOpened = true;
        disablePlayAndCreateButtons();
        cPlay = new PlayConnectFrameController(this, MC, PLAY_REGISTRY, USER_REGISTRY, getServer(), getUser(), lastSelectedPlay);
    }
    
    private void delPlay() {
        frList.getStatusBar().setProgress("Játszma törlése...");
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                MC.delPlay(lastSelectedPlay.getName());
                downloadList();
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void resetPlay() {
        frList.getStatusBar().setProgress("Játszma újraindítása...");
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                MC.resetPlay(lastSelectedPlay.getName());
                downloadList();
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void downloadList() {
        disablePlayAndCreateButtons();
        frList.getStatusBar().setProgress("Lista frissítése...");
        listDownloader = new ListDownloader(this, MC);
        listDownloader.execute();
    }
    
    private void updateList() {
        info = listDownloader.getInfo();
        if (info == null) return;
        if (getUser().isEmpty()) {
            signedIn = false;
            frList.getStatusBar().setError("A rendszer kijelentkeztette!");
            disposePlayController();
        }
        else {
            signedIn = true;
            listTableModel.updateList(info.getPlayList());
            frList.getStatusBar().setMessage("Az utolsó bejelentkező: " + info.getLastAction());
            if (!isPlayFrameOpened) {
                if (info.isUserOwner()) frList.disablePlayCreateButtons();
                else frList.enablePlayCreateButtons();
            }
        }
    }
    
    public void dispose() {
        stopWaitListEvent();
        disposePlayController();
        frList.dispose();
    }
    
    private void disposePlayController() {
        if (isPlayFrameOpened) {
            cPlay.dispose();
            isPlayFrameOpened = false;
        }
    }
    
    private void startWaitListEvent() {
        listEvent = new ListEvent(this, MC);
        listEvent.execute();
    }

    private void stopWaitListEvent() {
        listEvent.dispose();
        listEvent.cancel(true);
    }
    
    private void playSelected() {
        if (signedIn && !isPlayFrameOpened) {
            int index = frList.getSelectedPlayIndex();
            if (index != -1) {
                Play p = info.getPlayList().get(index);
                if (p.getOwner().equals(getUser()) || info.isAdmin()) frList.enablePlayOwnerButtons();
                else frList.disablePlayOwnerButtons();
                if (p.getPlayerNumber() < 2 && !info.isPlayerInGame()) frList.enablePlayConnectButtons();
                else frList.disablePlayConnectButtons();
                lastSelectedPlay = p;
            }
            else disablePlayButtons();
        }
    }
    
    private void disablePlayButtons() {
        frList.disablePlayConnectButtons();
        frList.disablePlayOwnerButtons();
    }
    
    private void disablePlayAndCreateButtons() {
        disablePlayButtons();
        frList.disablePlayCreateButtons();
    }
    
    private void initListFrame() {
        frList = new ListFrame(this);
        listTableModel = new ListTableModel();
        frList.setTableModel(listTableModel);
        disablePlayButtons();
        frList.setVisible(true);
        downloadList();
    }
    
}