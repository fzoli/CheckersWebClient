package mill.gui.controll.play;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Icon;
import javax.swing.SwingWorker;
import mill.core.PlayRegistry;
import mill.core.UserRegistry;
import mill.gui.controll.ChildController;
import mill.gui.controll.event.Event;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.controll.play.entity.GameLogData;
import mill.gui.controll.play.entity.MoreGameInfo;
import mill.gui.controll.play.entity.TimeCounter;
import mill.gui.controll.play.event.GameEventSignal;
import mill.gui.controll.play.event.SignSender;
import mill.gui.view.core.Core;
import mill.gui.view.core.MyFrame;
import mill.gui.view.play.GameFrame;
import mill.gui.view.play.GamePanel;
import mill.http.MessageChanger;
import mill.core.entity.GameChecker;
import mill.http.entity.GameInfo;

public class GameFrameController extends PlayFrameController {

    private GameFrame frGame;
    private final String PLAY_NAME;
    private final TimeCounter TIME_COUNTER;
    private final PlayFrameController CONTROLLER;
    private final PlayRegistry PLAY_REGISTRY;
    private final UserRegistry USER_REGISTRY;
    private final String USER;
    private final String SERVER;
    private SignSender signSender;
    private GameEventSignal eventSignal;
    
    private MoreGameInfo lastInfo;
    
    public GameFrameController(ChildController controller, MessageChanger mc, PlayRegistry playRegistry, UserRegistry userRegistry, String server, String user, String playName, MyFrame owner) {
        super(controller, mc);
        USER = user;
        SERVER = server;
        PLAY_NAME = playName;
        PLAY_REGISTRY = playRegistry;
        USER_REGISTRY = userRegistry;
        TIME_COUNTER = new TimeCounter(this);
        CONTROLLER = (PlayFrameController) controller;
        initGameFrame(owner);
        startEvents();
    }

    @Override
    public void dispose() {
        CONTROLLER.finish();
    }

    public void disposeController() {
        logTime();
        stopEvents();
        frGame.dispose();
    }

    private void logTime() {
        if (!lastInfo.getGameInfo().isGameFinished()) addGameTime();
        else removeGameTime();
    }
    
    private void addGameTime() {
        TIME_COUNTER.addGameTimeLog();
    }
    
    private void removeGameTime() {
        TIME_COUNTER.removeGameTimeLog();
    }
    
    private GameKey getGameKey() {
        return new GameKey(PLAY_NAME, USER, SERVER, getStartDate());
    }
    
    @Override
    public void receiveMessage(String message) {
        if (message.equals("PlayEvent")) updateGUI();
        else if (message.equals("StartStopEvent")) startStop();
        else if (message.equals("GiveUpEvent")) giveUp();
        else if (message.equals("TimeCounterEvent")) setPlayTime();
        else if (message.equals("CheckerMoved")) checkerMoved();
        else super.receiveMessage(message);
    }
    
    @Override
    protected MyFrame getFrame() {
        return frGame;
    }

    private void checkerMoved() {
        GamePanel gp = frGame.getGamePanel();
        final Point from = gp.getMovedFrom();
        final Point to = gp.getMovedTo();
        frGame.getStatusBar().setProgress("Lépés küldése...");
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                getMessageChanger().moveChecker(from, to);
                resetProgress();
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void startStop() {
        setProgressMsg();
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                resetProgress();
                getMessageChanger().startStopPlay();
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void giveUp() {
        setProgressMsg();
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                resetProgress();
                getMessageChanger().giveUpPlay();
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void initGameFrame(MyFrame owner) {
        frGame = new GameFrame(this, owner);
        frGame.setTitle(PLAY_NAME + " - játszma");
        frGame.setVisible(true);
        updateGUI();
    }
    
    private void setPlayTime() {
        frGame.getGamePanel().setTime(TIME_COUNTER.getTime());
    }
    
    private void updateGUI(MoreGameInfo info) {
        GameInfo gi = info.getGameInfo();
        if (gi.isPlayerInGame()) {
            resetProgress();
            manageInfo(info);
            GamePanel gp = frGame.getGamePanel();
            gp.updateCheckers(gi.getCheckers());
            String pending = gi.getGamePending();
            gp.setStartStopButton(pending, getStartStopIcon(), gi.isAbleStartStop());
            gp.setGiveUpButton(!gi.isGameFinished());
            gp.setGameMessage(gi.getMessage());
            gp.setRedHit(Integer.toString(info.getHit(1)));
            gp.setBlueHit(Integer.toString(info.getHit(2)));
            gp.setMoveable(!gi.isGameFinished());
            gp.setDragable(gi.isGameRunning());
        }
        else dispose();
        managePlayTime();
    }
    
    private void manageInfo(MoreGameInfo info) {
        boolean dataChanged = isDataChanged(info);
        boolean wasRunning = isPlayRunning();
        Date lastStartDate = getStartDate();
        lastInfo = info;
        boolean isRunning = isPlayRunning();
        if (isRunning && !wasRunning) TIME_COUNTER.start();
        if (wasRunning && !isRunning) TIME_COUNTER.stop();
        if (lastStartDate == null && getStartDate() != null) gameStartRightNow();
        if (dataChanged) logActualData();
    }
    
    private boolean isLogEnabled() {
        return USER_REGISTRY.getUserData(USER).isEnableLog();
    }
    
    private void logActualData() {
        if (isLogEnabled()) PLAY_REGISTRY.addGameLog(getGameKey(), getGameLogData());
    }
    
    private GameLogData getGameLogData() {
        GameInfo i = lastInfo.getGameInfo();
        return new GameLogData(i.getCheckers(), i.getMessage(), TIME_COUNTER.getCounter());
    }
    
    private boolean isDataChanged(MoreGameInfo newInfo) {
        if (lastInfo == null) return false;
        GameInfo actualInfo = newInfo.getGameInfo();
        GameInfo prevInfo = lastInfo.getGameInfo();
        List<GameChecker> actualCheckers = actualInfo.getCheckers();
        List<GameChecker> prevCheckers = prevInfo.getCheckers();
        return actualInfo.isGameFinished() != prevInfo.isGameFinished() ||
               actualCheckers.size() != prevCheckers.size() ||
               isCheckersMoved(actualCheckers, prevCheckers);
    }
    
    private boolean isCheckersMoved(List<GameChecker> act, List<GameChecker> prev) {
        List<Point> actPoints = getCheckerPoints(act);
        List<Point> prevPoints = getCheckerPoints(prev);
        boolean answer = true;
        for (Point p : actPoints) {
            answer &= prevPoints.contains(p);
            if (!answer) break;
        }
        return !answer;
    }
    
    private List<Point> getCheckerPoints(List<GameChecker> checkers) {
        List<Point> points = new ArrayList<Point>();
        for (GameChecker c : checkers)
            points.add(new Point(c.getRow(), c.getCol()));
        return points;
    }
    
    private Date getStartDate() {
        return lastInfo == null ? null : lastInfo.getGameInfo().getStartDate();
    }
    
    private void gameStartRightNow() {
        if (isLogEnabled()) PLAY_REGISTRY.addPlay(getGameKey());
    }
    
    private void managePlayTime() {
        if (TIME_COUNTER.getCounter() == 0)
            TIME_COUNTER.setGameKey(getGameKey());
        setPlayTime();
    }
    
    private Icon getStartStopIcon() {
        String icon;
        switch (getPending()) {
            case 1:
                icon = "play.png";
                break;
            case 2:
                icon = "pause.png";
                break;
            default:
                icon = "accept.png";
                break;
        }
        return Core.getToolbarIcon(icon);
    }
    
    private int getPending() {
        GameInfo info = lastInfo.getGameInfo();
        if (info.isGameStarted() && !info.isGameRunning()) return 1;
        else if (info.isGameStarted() && info.isGameRunning()) return 2;
        else if (!info.isGameStarted()) return 0;
        else return -1;
    }
    
    private boolean isPlayRunning() {
        return lastInfo == null ? false : lastInfo.getGameInfo().isGameRunning();
    }
    
    private boolean error = false;
    
    private void updateGUI() {
        setProgressMsg();
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    GameInfo gi = getMessageChanger().getGameInfo();
                    updateGUI(new MoreGameInfo(gi));
                    error = false;
                }
                catch(Exception ex) {
                    boolean wasError = error;
                    error = true;
                    if (wasError) dispose();
                    else updateGUI();
                }
                return null;
            }
            
        };
        sw.execute();
    }
    
    private void setProgressMsg() {
        frGame.getStatusBar().setProgress("Frissítés...");
    }
    
    private void resetProgress() {
        frGame.getStatusBar().reset();
    }
    
    private void startEvents() {
        MessageChanger mc = getMessageChanger();
        signSender = new SignSender(this, mc);
        eventSignal = new GameEventSignal(this, mc);
        signSender.execute();
        eventSignal.execute();
    }
    
    private void stopEvents() {
        if (signSender != null) stopEvent(signSender);
        if (eventSignal != null) stopEvent(eventSignal);
    }
    
    private void stopEvent(Event e) {
        e.dispose();
        e.cancel(true);
    }
    
}