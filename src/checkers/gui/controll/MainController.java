package checkers.gui.controll;

import checkers.core.hackshield.HackShield;
import checkers.core.PlayRegistry;
import checkers.core.ServerRegistry;
import checkers.core.UserRegistry;
import checkers.gui.view.SystemTrayIcon;
import checkers.http.MessageChanger;

public class MainController implements Controller {
    
    private SignInFrameController cSignIn;
    private ListFrameController cList;
    private PlayRegistry playRegistry;
    private SystemTrayIcon stIcon;
    private HackShield shield;
    
    public MainController() {
        hackShieldTest();
        cSignIn = new SignInFrameController(this);
        stIcon = new SystemTrayIcon(this);
    }

    @Override
    public void receiveMessage(String message) {
        if (message.equals("SignInDone")) {
            initListController();
        }
        else if (message.equals("SignOut")) {
            signOut(false);
        }
        else if (message.equals("Exit")) {
            exit();
        }
        else if (message.equals("Log")) {
            showLog();
        }
        else if (message.equals("ConnectError")) {
            signOut(true);
        }
        else if (stIcon != null && stIcon.isSupported()) {
            if (message.equals("ListIconified")) {
                setListFrameVisibility(false);
            }
            else if (message.equals("TrayEvent")) {
                setListFrameVisibility(true);
            }
            else if (message.equals("LogControllerOpened")) {
                stIcon.setLogEnabled(false);
            }
            else if (message.equals("LogControllerClosed")) {
                stIcon.setLogEnabled(true);
            }
            else if (message.equals("DuplicatedApplication")) {
                 stIcon.showDuplicatedApplicationMessage();
            }
            else if (message.equals("WrongUsername")) {
                 stIcon.showWrongUsernameMessage();
            }
            else if (message.equals("WrongPlayname")) {
                 stIcon.showWrongPlaynameMessage();
            }
            else if (message.equals("WrongPassword")) {
                 stIcon.showWrongPasswordMessage();
            }
        }
    }
    
    private boolean isSignedIn() {
        return cSignIn.isSignedIn();
    }
    
    private void hackShieldTest() {
        shield = new HackShield(this);
    }
    
    private void setListFrameVisibility(boolean value) {
        if (cList != null) cList.setListFrameVisibility(value);
    }
    
    private void initListController() {
        cList = new ListFrameController(this, getMessageChanger(), getServerRegistry(), getPlayRegistry(), getUserRegistry());
    }
    
    private MessageChanger getMessageChanger() {
        return cSignIn.getMessageChanger();
    }
    
    private PlayRegistry getPlayRegistry() {
        if (playRegistry == null)
            playRegistry = new PlayRegistry();
        return playRegistry;
    }
    
    private ServerRegistry getServerRegistry() {
        return cSignIn.getServerRegistry();
    }
    
    private UserRegistry getUserRegistry() {
        return cSignIn.getUserRegistry();
    }
    
    private void signOut(boolean connectError) {
        if (isSignedIn()) {
            closeListFrame();
            if (!connectError) cSignIn.signOut(false);
            cSignIn.showFrame();
        }
    }
    
    private void exit() {
        closeListFrame();
        cSignIn.signOut(true);
        stIcon.dispose();
        shield.dispose();
        System.exit(0);
    }
    
    private void closeListFrame() {
        if (cList != null) {
            cList.dispose();
            cList = null;
        }
    }
    
    private void showLog() {
        LogFrameController cLog = new LogFrameController(getLogOwnerController(), getPlayRegistry(), getServerRegistry(), getUserRegistry());
        if (isSignedIn())
            cLog.addVerifiedUser(cList.getServer(), cList.getUser());
    }
    
    private ChildController getLogOwnerController() {
        return cList == null ? cSignIn : cList;
    }
    
    public static void main(String[] args) {
        new MainController();
    }
    
}