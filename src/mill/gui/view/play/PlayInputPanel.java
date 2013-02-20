package mill.gui.view.play;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import mill.gui.view.core.Core;
import mill.gui.view.core.MyFrame;

public class PlayInputPanel extends JPanel {

    private final MyFrame FRAME;
    private JTextField tfName;
    private JPasswordField tfPass;
    private JButton bt;
    
    public PlayInputPanel(MyFrame frame) {
        this(frame, true);
    }
    
    public PlayInputPanel(MyFrame frame, boolean createMode) {
        FRAME = frame;
        init(createMode);
    }
    
    public String getPlayName() {
        return tfName.getText();
    }
    
    public String getPlayPassword() {
        return tfPass.getText();
    }
    
    protected void enableFields() {
        bt.setEnabled(true);
        tfName.setEnabled(true);
        tfPass.setEnabled(true);
    }
    
    protected void disableFields() {
        bt.setEnabled(false);
        tfName.setEnabled(false);
        tfPass.setEnabled(false);
    }
    
    protected void setPlayName(String text) {
        tfName.setText(text);
    }
    
    private void init(boolean createMode) {
        Core.setLAF();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final boolean CREATE = createMode;
        
        c.gridx = 1;
        c.gridwidth = 1;
        c.anchor = c.LINE_END;
        c.insets = new Insets(0, 0, 10, 20);
        add(new JLabel("Név:"), c);
        add(new JLabel("Jelszó:"), c);
        
        c.gridx = 2;
        c.fill = c.HORIZONTAL;
        c.anchor = c.LINE_START;
        c.insets = new Insets(0, 0, 10, 0);
        tfName = new JTextField(15);
        if (!CREATE) tfName.setEditable(false);
        add(tfName, c);
        tfPass = new JPasswordField();
        add(tfPass, c);
        
        if (CREATE) initAddPanel();
        else initConnectButton();
        initEvent();
        
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
    }
    
    private void initConnectButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridwidth = 2;
        bt = new JButton("Kapcsolódik");
        add(bt, c);
    }
    
    private void initAddPanel() {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        bt = new JButton("Létrehoz");
        
        c.insets = new Insets(10, 0, 0, 0);
        addPanel.add(bt, c);
        
        c.insets = new Insets(20, 0, 0, 0);
        addPanel.add(new JLabel("A jelszó megadása opcionális."), c);
        
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        add(addPanel, c);
    }
    
    private void initEvent() {
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FRAME.sendMessage("ButtonEvent");
            }
            
        };
        tfName.addActionListener(al);
        tfPass.addActionListener(al);
        bt.addActionListener(al);
    }
    
}