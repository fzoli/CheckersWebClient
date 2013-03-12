package checkers.gui.view.log;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import checkers.gui.controll.Controller;
import checkers.gui.view.core.ChildFrame;

public class PasswordInputFrame extends ChildFrame {

    private JButton btOk;
    private JPasswordField lbPass;
    
    public PasswordInputFrame(Controller controller, JFrame owner) {
        super(controller, owner);
        setCloseEvent();
    }

    @Override
    protected void initComponents() {
        setTitle("Jelszómegadás");
        setLayout(new GridBagLayout());
        JPanel pane = new JPanel(new FlowLayout());
        pane.add(new JLabel("Jelszó:"));
        lbPass = new JPasswordField(12);
        pane.add(lbPass);
        btOk = new JButton("OK");
        pane.add(btOk);
        add(pane);
        pack(10, 10);
        setMinimumSize(getSize());
        setInputEvent();
    }

    public String getPassword() {
        return lbPass.getText();
    }
    
    public void setInputEnabled(boolean enabled) {
        btOk.setEnabled(enabled);
        lbPass.setEnabled(enabled);
    }
    
    private void setInputEvent() {
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("InputEvent");
            }
            
        };
        btOk.addActionListener(al);
        lbPass.addActionListener(al);
    }
    
    private boolean closing = false;

    @Override
    public void dispose() {
        closing = true;
        super.dispose();
    }
    
    private void setCloseEvent() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowDeactivated(WindowEvent e) {
                if (!closing) setVisible(true);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                closing = true;
                sendMessage("CloseEvent");
            }
            
        });
    }
    
}