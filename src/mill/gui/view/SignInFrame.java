package mill.gui.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import mill.gui.controll.Controller;
import mill.gui.view.autocomplete.AutoCompleteCombo;
import mill.gui.view.autocomplete.AutoCompleteModel;
import mill.gui.view.core.DropDownButton;
import mill.gui.view.core.MainFrame;
import mill.gui.view.core.StatusBar;

public class SignInFrame extends MainFrame {

    private AutoCompleteCombo tfId;
    private JPasswordField tfPass;
    private ServerBox cbServer;
    private JButton btSignIn;
    private JCheckBox cbName, cbPass;
    
    private class ServerBox extends JComboBox {

        public ServerBox() {
            super(new ServerModel(new String[0]));
        }

        @Override
        public int getSelectedIndex() {
            return getModel().getIndexOf(getSelectedItem());
        }

        @Override
        public ServerModel getModel() {
            return (ServerModel) super.getModel();
        }
        
    }
    
    private class ServerModel extends DefaultComboBoxModel {
        
        public ServerModel(String[] items) {
            super(items);
        }
        
    }
    
    public SignInFrame(Controller controller) {
        super(controller);
        setUsernameListener();
        setTextFieldListener();
        setSignInButtonListener();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public String getId() {
        return tfId.getText();
    }
    
    public String getPassword() {
        return tfPass.getText();
    }
    
    public void setId(String text) {
        tfId.setText(text);
    }
    
    public void setPassword(String text) {
        tfPass.setText(text);
    }
    
    public int getServerIndex() {
        return cbServer.getSelectedIndex();
    }
    
    public JCheckBox getLogEnabledCb() {
        return cbName;
    }
    
    public JCheckBox getPasswordShowCb() {
        return cbPass;
    }

    public void setUserComboBox(String[] users) {
        AutoCompleteModel m = new AutoCompleteModel();
        m.readData(users);
        tfId.setModel(m);
    }
    
    public void setServerComboBox(String[] servers) {
        ServerModel cbm = new ServerModel(initComboItems(servers));
        cbServer.setModel(cbm);
        setComboEvent();
    }
    
    private void setComboEvent() {
        cbServer.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                comboEvent();
            }
            
        });
    }
    
    private void comboEvent() {
        int size = cbServer.getModel().getSize();
        int selected = cbServer.getSelectedIndex();
        if (selected == size - 1) serverEditEvent();
        else if (selected == size - 2) disableConnectButton();
        else enableConnectButton();
        sendMessage("ServerSelected");
    }
    
    public void disableConnectButton() {
        btSignIn.setEnabled(false);
    }
    
    private void enableConnectButton() {
        btSignIn.setEnabled(true);
    }
    
    private void serverEditEvent() {
        sendMessage("ServerEditEvent");
        cbServer.setSelectedIndex(cbServer.getModel().getSize() - 2);
    }
    
    private String[] initComboItems(String[] items) {
        String[] i = Arrays.copyOf(items, items.length + 2);
        i[i.length - 2] = "";
        i[i.length - 1] = "<Szerkesztés>";
        return i;
    }
    
    public static final int PROGRESS = 0;
    public static final int MESSAGE = 1;
    public static final int WARNING = 2;
    
    public void setMode(int mode, String message) {
        StatusBar sb = getStatusBar();
        switch (mode) {
            case PROGRESS:
                disableFields();
                sb.setProgress(message);
                break;
            case MESSAGE:
                enableFields();
                sb.setMessage(message);
                break;
            case WARNING:
                enableFields();
                sb.setError(message);
                break;
        }
    }
    
    public void setButtonEnabled(boolean enabled) {
        btSignIn.setEnabled(enabled);
    }
    
    public void setButtonText(String text) {
        btSignIn.setText(text);
    }
    
    public void setFocusToButton() {
        btSignIn.requestFocus();
    }
    
    private void enableFields() {
        tfId.setEnabled(true);
        cbServer.setEnabled(true);
        enableConnectButton();
    }
    
    private void disableId() {
        tfId.setEnabled(false);
    }
    
    public void enablePassword() {
        tfPass.setEnabled(true);
    }
    
    public void disablePassword() {
        tfPass.setEnabled(false);
    }
    
    private void disableFields() {
        disableId();
        disablePassword();
        disableServerBox();
        disableConnectButton();
    }
    
    private void disableServerBox() {
        cbServer.setEnabled(false);
    }
    
    @Override
    protected void initComponents() {
        setTitle("Bejelentkezés");
        setLayout(new GridBagLayout());
        
        JPanel input = new JPanel();
        input.setBorder(BorderFactory.createTitledBorder("Online Dáma felhasználó"));
        input.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = c.LINE_END;
        c.insets = new Insets(0, 5, 10, 20);
        input.add(new JLabel("Felhasználónév:"), c);
        c.gridy = 2;
        input.add(new JLabel("Jelszó:"), c);
        c.gridy = 3;
        input.add(new JLabel("Szerver:"), c);
        
        c.gridx = 2;
        c.gridy = 1;
        c.fill = c.HORIZONTAL;
        c.anchor = c.LINE_START;
        c.insets = new Insets(0, 0, 10, 5);
        tfId = new AutoCompleteCombo();
        input.add(tfId, c);
        tfPass = new JPasswordField(15);
        c.gridy = 2;
        input.add(tfPass, c);
        
        cbServer = new ServerBox();
        c.gridy = 3;
        input.add(cbServer, c);
        
        c.insets = new Insets(0, 0, 10, 2);
        c.anchor = c.LINE_START;
        c.fill = c.NONE;
        c.gridx = 3;
        c.gridy = 3;
        JPanel pBt = new JPanel(new GridBagLayout());
        JPanel pLogin = initHiddenPanel();
        input.add(new DropDownButton("Bejelentkezési adatok", pLogin, pBt), c);
        
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 3;
        c.fill = c.BOTH;
        input.add(pLogin, c);
        
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = c.CENTER;
        c.insets = new Insets(0, 0, 10, 0);
        c.gridheight = 1;
        add(input, c);
        
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = c.NONE;
        btSignIn = new JButton("Bejelentkezik");
        pBt.add(btSignIn, c);
        
        c.insets = new Insets(10, 0, 0, 0);
        c.gridy = 3;
        pBt.add(new JLabel("Az első bejelentkezés egyben regisztráció is."), c);
        
        add(pBt, c);
        packFrame(pLogin, pBt);
    }

    private JPanel initHiddenPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = c.HORIZONTAL;
        cbName = createCheckbox("Naplózás engedélyezése");
        p.add(cbName, c);
        c.gridy = 2;
        cbPass = createCheckbox("Jelszó tárolása");
        cbPass.setEnabled(false);
        p.add(cbPass, c);
        setCbEvent();
        return p;
    }
    
    private void setCbEvent() {
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("CheckboxChange");
            }
            
        };
        cbName.addActionListener(al);
        cbPass.addActionListener(al);
    }
    
    private JCheckBox createCheckbox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setFocusable(false);
        return cb;
    }
    
    private void packFrame(JPanel pLogin, JPanel pBt) {
        pLogin.setVisible(true);
        pack(25, 0);
        int width = pLogin.getSize().width;
        int height = pBt.getSize().height;
        pLogin.setVisible(false);
        pLogin.setPreferredSize(new Dimension(width, height));
    }
    
    private void signInEvent() {
        if (btSignIn.isEnabled()) sendMessage("signIn");
    }

    private void setSignInButtonListener() {
        btSignIn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                signInEvent();
            }
            
        });
    }
    
    private void setUsernameListener() {
        tfId.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("UsernameEvent");
            }
            
        });
    }
    
    private void setTextFieldListener() {
        KeyAdapter a = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signInEvent();
                }
            }
            
        };
        tfPass.addKeyListener(a);
        cbServer.addKeyListener(a);
        tfPass.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                sendMessage("PasswordEvent");
            }
            
        });
    }
    
}