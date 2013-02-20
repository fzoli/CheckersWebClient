package mill.gui.controll;

import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.SwingWorker;
import mill.core.InputValidator;
import mill.core.ServerRegistry;
import mill.core.UserRegistry;
import mill.database.entity.UserData;
import mill.database.entity.UserPasswordData;
import mill.gui.view.SignInFrame;
import mill.gui.view.core.MyFrame;
import mill.http.MessageChanger;

public class SignInFrameController extends ChildController {

    private boolean signIn, signedIn, exit;
    private SignInFrame frSignIn;
    private UserRegistry userRegistry;
    private ServerRegistry serverRegistry;
    private MessageChanger mc;
    private SwingWorker swSignIn;
    private ServerEditFrameController cServer;
    
    public SignInFrameController(Controller controller) {
        super(controller);
        initSignInFrame();
    }

    public boolean isSignedIn() {
        return signedIn;
    }
    
    public ServerRegistry getServerRegistry() {
        return serverRegistry;
    }
    
    public UserRegistry getUserRegistry() {
        return userRegistry;
    }
    
    @Override
    protected MyFrame getFrame() {
        return frSignIn;
    }
    
    @Override
    public void receiveMessage(String message) {
        if (message.equals("signIn")) {
            if (signIn) {
                signIn();
            }
            else {
                swSignIn.cancel(true);
                setSignInFrameToDefault();
            }
        }
        else {
            if (message.equals("ServerEditEvent")) serverEdit();
            else if (message.equals("ServerEditClose")) loadSignInFrame();
            else if (message.equals("CheckboxChange")) checkboxChange();
            else if (message.equals("UsernameEvent")) usernameEvent();
            else if (message.equals("PasswordEvent")) managePasswordCheckbox();
            else if (message.equals("ServerSelected")) serverSelected();
            else sendMessage(message);
        }
    }
    
    private void serverSelected() {
        String server = getSelectedServerName();
        if (server != null) {
            frSignIn.enablePassword();
        }
        else {
            frSignIn.disablePassword();
        }
        usernameEvent();
    }
    
    private void usernameEvent() {
        String id = frSignIn.getId();
        if (userRegistry.isUserExists(id)) {
            UserData data = getUserData();
            managePassword(data);
            loadCheckboxValues();
        }
        else managePassword(null);
    }
    
    private void managePassword(UserData data) {
        if (data == null) {
            managePassword(null, false);
        }
        else {
            if (data.isShowPassword()) {
                boolean b = getPasswordData() != null;
                managePassword(data, b);
            }
            else {
                managePassword(data, false);
            }
        }
    }
    
    private boolean savedPassword = false;
    
    private void managePassword(UserData data, boolean showPassword) {
        if (showPassword) {
            savedPassword = true;
            frSignIn.disablePassword();
            UserPasswordData upd = data.getPasswordData(getSelectedServerName());
            frSignIn.setPassword(createHiddenPassword(upd.getPasswordLength()));
        }
        else {
            if (savedPassword) {
                savedPassword = false;
                frSignIn.enablePassword();
                frSignIn.setPassword("");
                resetCheckboxValues();
            }
        }
        managePasswordCheckbox();
    }
    
    private String getSelectedServerName() {
        try {
            return serverRegistry.getServerInfo(frSignIn.getServerIndex()).getDomain();
        }
        catch(Exception ex) {
            return null;
        }
    }
    
    private boolean isPasswordInvalid() {
        return !(isPasswordSecured() || InputValidator.isPasswordValid(frSignIn.getPassword()));
    }
    
    private void managePasswordCheckbox() {
        UserData ud = getUserData();
        if (ud != null && !ud.isPasswordDataEmpty() && ud.isShowPassword()) {
            frSignIn.getPasswordShowCb().setEnabled(true);
        }
        else {
            if (isPasswordInvalid()) {
                frSignIn.getPasswordShowCb().setEnabled(false);
                frSignIn.getPasswordShowCb().setSelected(false);
            }
            else {
                frSignIn.getPasswordShowCb().setEnabled(true);
            }
        }
    }
    
    private String createHiddenPassword(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("*");
        }
        return sb.toString();
    }
    
    private void checkboxChange() {
        JCheckBox log = frSignIn.getLogEnabledCb();
        JCheckBox pass = frSignIn.getPasswordShowCb();
        if (pass.isSelected()) {
            log.setSelected(true);
        }
        saveCheckboxValues();
    }
    
    private void loadCheckboxValues() {
        String id = frSignIn.getId();
        if (userRegistry.isUserExists(id)) {
            UserData data = getUserData();
            frSignIn.getLogEnabledCb().setSelected(data.isEnableLog());
            frSignIn.getPasswordShowCb().setSelected(data.isShowPassword());
        }
    }
    
    private void resetCheckboxValues() {
        frSignIn.getLogEnabledCb().setSelected(false);
        frSignIn.getPasswordShowCb().setSelected(false);
    }
    
    private void saveCheckboxValues() {
        String id = frSignIn.getId();
        if (userRegistry != null && userRegistry.isUserExists(id)) {
            UserData data = getUserData();
            boolean logEnabled = frSignIn.getLogEnabledCb().isSelected();
            boolean showPass = frSignIn.getPasswordShowCb().isSelected();
            if (logEnabled != data.isEnableLog() || showPass != data.isShowPassword()) {
                
                if (!showPass  && savedPassword) hidePasswordValue();
                
                if (!showPass) frSignIn.enablePassword();
                
                if (logEnabled != data.isEnableLog()) {
                    userRegistry.setUserLog(id, logEnabled);
                    updateUserComboBox();
                }
                
            }
        }
    }
    
    private void hidePasswordValue() {
        String id = frSignIn.getId();
        
        frSignIn.setPassword("");
        savedPassword = false;
        frSignIn.enablePassword();
        frSignIn.getPasswordShowCb().setEnabled(false);
        
        userRegistry.setUserPassVisibility(id, false);
    }
    
    private void serverEdit() {
        if (cServer == null) {
            cServer = new ServerEditFrameController(this, serverRegistry);
            frSignIn.setVisible(false);
        }
    }
    
    public void showFrame() {
        frSignIn.setVisible(true);
    }
    
    public void signOut(boolean end) {
        exit = end;
        signedIn = false;
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                if (mc != null) mc.signOut();
                return null;
            }

            @Override
            protected void done() {
                if (exit) frSignIn.dispose();
            }
            
        };
        sw.execute();
    }
    
    protected MessageChanger getMessageChanger() {
        return mc;
    }
    
    private void initServerRegistry() {
        userRegistry = new UserRegistry();
    }
    
    private void initSignInFrame() {
        frSignIn = new SignInFrame(this);
        frSignIn.setMode(frSignIn.PROGRESS, "Lista betöltése...");
        loadSignInFrame();
    }

    private void loadSignInFrame() {
        frSignIn.setVisible(true);
        initServerRegistry();
        updateUserComboBox();
        serverRegistry = new ServerRegistry();
        String[] servers = serverRegistry.getVisibleServerNames();
        if (servers.length != 0)
            setSignInFrameToDefault();
        else {
            frSignIn.setMode(frSignIn.MESSAGE, "Válassza a szerverlista utolsó opcióját!");
            frSignIn.disableConnectButton();
        }
        frSignIn.setServerComboBox(servers);
        cServer = null;
    }
    
    private void setSignInFrameToDefault() {
        signIn = true;
        if (!savedPassword) frSignIn.enablePassword();
        frSignIn.setMode(frSignIn.MESSAGE, "Kérem, adja meg az adatokat.");
        frSignIn.setButtonText("Bejelentkezik");
    }
    
    private boolean isPasswordSecured() {
        UserData u = getUserData();
        if (u == null) return false;
        return u.getPasswordData(getSelectedServerName()) != null && u.isShowPassword();
    }
    
    private UserData getUserData() {
        return userRegistry.getUserData(frSignIn.getId());
    }
    
    private String getRealPassword() {
        return isPasswordSecured() ? getPasswordData().getPassword() : frSignIn.getPassword();
    }
    
    private UserPasswordData getPasswordData() {
        return userRegistry.getPasswordData(getSelectedServerName(), frSignIn.getId());
    }
    
    private void signIn() {
        signedIn = false;
        if (!InputValidator.isUserIdValid(frSignIn.getId())) {
            frSignIn.setMode(frSignIn.WARNING, "Hibás felhasználónév!");
            sendMessage("WrongUsername");
        }
        else if (isPasswordInvalid()) {
            frSignIn.setMode(frSignIn.WARNING, "Hibás jelszó!");
            sendMessage("WrongPassword");
        }
        else {
            signIn = false;
            frSignIn.setFocusToButton();
            frSignIn.setButtonText("Mégsem");
            frSignIn.setMode(frSignIn.PROGRESS, "Bejelentkezés...");
            frSignIn.setButtonEnabled(true);
            int serverIndex = frSignIn.getServerIndex();
            String serverUrl = serverRegistry.getServerUrl(serverIndex);
            boolean isValidCert = serverRegistry.isValidCert(serverIndex);
            mc = new MessageChanger(this, serverUrl, isValidCert);
            swSignIn = new SwingWorker() {

                @Override
                protected void done() {
                    mc.abortSignIn();
                }

                @Override
                protected Object doInBackground() throws Exception {
                    int code = mc.signIn(frSignIn.getId(), getRealPassword(), isPasswordSecured());
                    if (!signIn) {
                        setSignInFrameToDefault();
                        switch(code) {
                            case 0:
                                signedIn = true;
                                frSignIn.setVisible(false);
                                if(!regUserIfNeed()) saveUserPasswordIfNeed();
                                sendMessage("SignInDone");
                                break;
                            case 1:
                                hidePasswordValue();
                                frSignIn.setMode(frSignIn.WARNING, "Hibás jelszó!");
                                break;
                            case 2:
                                frSignIn.setMode(frSignIn.WARNING, "Sikertelen kapcsolódás!");
                                break;
                        }
                    }
                    return null;
                }
                
            };
            swSignIn.execute();
        }
    }

    private boolean regUserIfNeed() {
        String user = frSignIn.getId();
        if (frSignIn.getLogEnabledCb().isSelected() && !userRegistry.isUserExists(user)) {
            boolean showPass = frSignIn.getPasswordShowCb().isSelected();
            userRegistry.addUser(user, showPass);
            saveUserPassword();
            updateUserComboBox();
            return true;
        }
        return false;
    }

    private void saveUserPasswordIfNeed() {
        if (!isPasswordSecured()) {
            saveUserPassword();
        }
    }
    
    private void saveUserPassword() {
        String user = frSignIn.getId();
        String pass = frSignIn.getPassword();
        userRegistry.setUserPassword(getSelectedServerName(), user, pass);
        boolean showPass = frSignIn.getPasswordShowCb().isSelected();
        userRegistry.setUserPassVisibility(user, showPass);
    }
    
    private void updateUserComboBox() {
        List<UserData> ul = userRegistry.getVisibleUsers();
        String[] users = new String[ul.size()];
        for (int i = 0; i < users.length; i++) {
            users[i] = ul.get(i).getName();
        }
        String id = frSignIn.getId();
        frSignIn.setUserComboBox(users);
        frSignIn.setId(id);
    }
    
}