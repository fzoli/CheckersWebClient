package mill.gui.controll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SwingWorker;
import mill.core.PlayRegistry;
import mill.core.ServerRegistry;
import mill.core.UserRegistry;
import mill.core.entity.GameChecker;
import mill.database.table.Server;
import mill.gui.controll.log.PasswordInputFrameController;
import mill.gui.controll.play.entity.GameData;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.controll.play.entity.GameLogData;
import mill.gui.controll.play.entity.MoreGameInfo;
import mill.gui.controll.play.entity.TimeCounter;
import mill.gui.view.LogFrame;
import mill.gui.view.core.Core;
import mill.gui.view.core.MyFrame;
import mill.gui.view.core.StatusBar;
import mill.gui.view.log.LogMenuFactory;
import mill.gui.view.play.GamePanel;
import mill.gui.view.play.LogPanel;
import mill.http.MessageChanger;
import mill.http.entity.ValidityInfo;

public class LogFrameController extends ChildFrameController {
    
    private List<GameKey> verifiedUsers = new ArrayList<GameKey>();
    
    private int logIndex;
    private boolean validated, expandValidate;
    private GameData gameData;
    private final PlayRegistry PLAY_REGISTRY;
    private final ServerRegistry SERVER_REGISTRY;
    private final UserRegistry USER_REGISTRY;
    private PasswordInputFrameController cPassword;
    private LogFrame frLog;
    
    public LogFrameController(ChildController controller, PlayRegistry playRegistry, ServerRegistry serverRegistry, UserRegistry userRegistry) {
        super(controller);
        PLAY_REGISTRY = playRegistry;
        SERVER_REGISTRY = serverRegistry;
        USER_REGISTRY = userRegistry;
        LogMenuFactory.setController(this);
        sendMessage("LogControllerOpened");
        initFrame();
    }

    public void addVerifiedUser(String server, String user) {
        verifiedUsers.add(new GameKey(user, server));
    }
    
    @Override
    protected MyFrame getFrame() {
        return frLog;
    }

    @Override
    public void receiveMessage(String message) {
        if (message.equals("finish")) finish();
        else if (message.equals("PasswordInputEvent")) checkPassword();
        else if (message.equals("WrongPasswordEvent")) wrongPassword();
        else if (message.equals("PasswordInputDisposed")) disposePasswordInput();
        else if (message.equals("OpenEvent")) openEvent();
        else if (message.equals("ClickEvent")) openEvent();
        else if (message.equals("DeleteEvent")) deleteEvent();
        else if (message.equals("RefreshEvent")) refreshEvent();
        else if (message.equals("ExpandEvent")) expandEvent();
        else if (message.equals("NextLog")) nextLog();
        else if (message.equals("PreviousLog")) previousLog();
        else if (message.equals("ComboSelectEvent")) selectLog();
    }
    
    private void updateLogToolbar(int count) {
        frLog.getLogPanel().updateToolbar(count);
    }
    
    private void clearLogToolbar() {
        frLog.getLogPanel().clearToolbar();
    }
    
    private GameKey getExpandedGame() {
        return frLog.getLogPanel().getExpandedGame();
    }
    
    private GameKey getSelectedGame() {
        return frLog.getLogPanel().getSelectedGame();
    }
    
    private void selectLog() {
        int index = frLog.getLogPanel().getSelectedCombo();
        if (index > -1) loadGameLog(index);
    }
    
    private void nextLog() {
        int i = gameData.getSize() - 1;
        if (i > logIndex) loadGameLog(++logIndex);
        if (logIndex == i) setNextButtonEnabled(false);
        setPreviousButtonEnabled(true);
    }
    
    private void previousLog() {
        if (logIndex > 0)
        loadGameLog(--logIndex);
        if (logIndex == 0) setPreviousButtonEnabled(false);
        setNextButtonEnabled(true);
    }
    
    private void deleteEvent() {
        expandValidate = false;
        final GameKey gk = getSelectedGame();
        if (verifiedUsers.contains(new GameKey(gk.getUser(), gk.getServer()))) {
            boolean b = Core.showConfirmDialog(frLog, "Biztos, hogy törli a kiválasztott elemet?\nMinden, amit tartalmaz végleg törlődik!");
            if (b) {
                SwingWorker sw = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        frLog.getStatusBar().setProgress("Törlés folyamatban...");
                        if(PLAY_REGISTRY.delPlay(gk)) {
                            updateLogTreeModel();
                            frLog.getStatusBar().reset();
                        }
                        else {
                            frLog.getStatusBar().setError("Sikertelen törlés.");
                        }
                        return null;
                    }
                    
                };
                sw.execute();
            }
        }
        else createPasswordInput();
    }
    
    private void refreshEvent() {
        updateLogTreeModel();
    }
    
    private void openEvent() {
        final GameKey gkSel = getSelectedGame();
        if (gkSel != null && gkSel.getType() == GameKey.KEY) {
            SwingWorker sw = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    frLog.getStatusBar().setProgress("Játszma megnyitása folyamatban...");
                    openPlayLog(gkSel);
                    frLog.getStatusBar().reset();
                    return null;
                }
                
            };
            sw.execute();
        }
    }
    
    private void expandEvent() {
        expandValidate = true;
        GameKey gkExp = getExpandedGame();
        if (gkExp != null && gkExp.getType() == GameKey.USER) {
            if (!verifiedUsers.contains(gkExp)) createPasswordInput();
        }
    }
    
    private void setPreviousButtonEnabled(boolean enabled) {
        frLog.getLogPanel().setPrevBtEnabled(enabled);
    }
    
    private void setNextButtonEnabled(boolean enabled) {
        frLog.getLogPanel().setNextBtEnabled(enabled);
    }
    
    private void openPlayLog(GameKey gk) {
        gameData = PLAY_REGISTRY.getGameData(gk);
        int size = gameData.getSize();
        if (size > 0) {
            clearLogToolbar();
            updateLogToolbar(size);
            setLogSelectEnabled(true);
            setNextButtonEnabled(size > 1);
            loadGameLog(0);
        }
        else {
            setNextButtonEnabled(false);
            resetGameData();
        }
        setPreviousButtonEnabled(false);
    }
    
    private void loadGameLog(int index) {
        logIndex = index;
        GamePanel panel = frLog.getGamePanel();
        GameLogData log = gameData.getLogData(index);
        frLog.getLogPanel().setSelectedCombo(index);
        int redHit = MoreGameInfo.getHit(log.getCheckers(), 1);
        int blueHit = MoreGameInfo.getHit(log.getCheckers(), 0);
        String time = TimeCounter.dateFormat(log.getTime());
        panel.setRedHit(Integer.toString(redHit));
        panel.setBlueHit(Integer.toString(blueHit));
        panel.setGameMessage(log.getMessage());
        panel.updateCheckers(log.getCheckers());
        panel.setTime(time);
        int size = gameData.getSize() -1;
        setNextButtonEnabled(logIndex < size);
        setPreviousButtonEnabled(logIndex > 0);
    }
    
    private void resetGameData() {
        GamePanel panel = frLog.getGamePanel();
        panel.setGameMessage("");
        panel.setTime("00:00:00");
        panel.updateCheckers(new ArrayList<GameChecker>());
        setLogSelectEnabled(false);
        clearLogToolbar();
    }
    
    private void setLogSelectEnabled(boolean e) {
        LogPanel lp = frLog.getLogPanel();
        lp.setLogSelectEnabled(e);
    }
    
    private void checkPassword() {
        final StatusBar sb = cPassword.getFrame().getStatusBar();
        sb.setProgress("Jelszó ellenőrzése...");
        cPassword.setInputEnabled(false);
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                boolean check;
                MessageChanger mc = getSelectedMessageChanger();
                boolean connectSuccess = mc.testConnection();
                if (connectSuccess) {
                    check = checkRemotePassword(mc);
                    sb.setProgress("Távoli jelszó ellenőrzés...");
                }
                else {
                    sb.setProgress("Helyi jelszó ellenőrzés...");
                    check = checkLocalPassword();
                }
                validated = check;
                if (!validated) {
                    sb.setError("Hibás jelszó!");
                    cPassword.setInputEnabled(true);
                }
                else {
                    disposePasswordInput();
                    verifiedUsers.add(getCheckKey());
                    manageEvent();
                }
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void wrongPassword() {
        sendMessage("WrongPassword");
    }
    
    private boolean checkRemotePassword(MessageChanger mc) {
        GameKey gk = getCheckKey();
        ValidityInfo vi = mc.validateUser(gk.getUser(), cPassword.getPassword());
        if (vi.isExists()) {
            if (vi.isValid()) {
                USER_REGISTRY.setUserPassword(gk.getServer(), gk.getUser(), cPassword.getPassword());
                return true;
            }
            return false;
        }
        else return checkLocalPassword();
    }
    
    private GameKey getCheckKey() {
        return expandValidate ? getExpandedGame() : getSelectedGame();
    }
    
    private boolean checkLocalPassword() {
        GameKey gk = getCheckKey();
        return USER_REGISTRY.isPasswordEquals(gk.getServer(), gk.getUser(), cPassword.getPassword());
    }
    
    private MessageChanger getSelectedMessageChanger() {
        Server server = SERVER_REGISTRY.getServer(getExpandedGame().getServer());
        String url = ServerRegistry.getServerUrl(server);
        return new MessageChanger(this, url, server.isValidCert());
    }
    
    private void createPasswordInput() {
        validated = false;
        disposePasswordInput();
        setLogTreeMode(false, expandValidate);
        cPassword = new PasswordInputFrameController(this);
    }
    
    private void disposePasswordInput() {
        if (cPassword != null) {
            setLogTreeMode(true, validated && expandValidate);
            cPassword.dispose();
            cPassword = null;
        }
    }
    
    private void manageEvent() {
        if (validated && !expandValidate) deleteEvent();
    }
    
    private void setLogTreeMode(boolean enabled, boolean expanded) {
        if (frLog != null) frLog.getLogPanel().setLogTreeMode(enabled, expanded);
    }
    
    private void updateLogTreeModel() {
        frLog.getLogPanel().updateLogTreeModel(PLAY_REGISTRY.getGameList());
        resetGameData();
    }
    
    private void finish() {
        sendMessage("LogControllerClosed");
        disposeFrame();
        disposePasswordInput();
    }
    
    private void disposeFrame() {
        getOwnerFrame().setVisible(true);
        frLog.dispose();
        frLog = null;
    }
    
    private void initFrame() {
        frLog = new LogFrame(this, getOwnerFrame());
        frLog.setVisible(true);
        getOwnerFrame().setVisible(false);
        updateLogTreeModel();
        resetGameData();
    }
    
}