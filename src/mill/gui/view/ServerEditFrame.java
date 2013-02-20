package mill.gui.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import mill.core.entity.ServerInfo;
import mill.gui.controll.Controller;
import mill.gui.view.core.ChildFrame;
import mill.gui.view.core.Core;

public class ServerEditFrame extends ChildFrame {

    private JPanel content;
    private boolean done;
    private JTextField tfAddres, tfPort, tfPath;
    private JCheckBox cbSecure, cbSelfSigned, cbVisible;
    private JComboBox cbServers;
    private JButton bt, btEdit, btDel;
    private int mode;
    
    public ServerEditFrame(Controller controller, JFrame owner) {
        super(controller, owner);
    }
    
    public void enableEditToolbar() {
        btEdit.setEnabled(true);
        btDel.setEnabled(true);
        cbServers.setEnabled(true);
    }
    
    public void disableEditToolbar() {
        btEdit.setEnabled(false);
        btDel.setEnabled(false);
        cbServers.setEnabled(false);
    }
    
    public void updateModel(String[] names) {
        cbServers.setModel(new DefaultComboBoxModel(names));
    }
    
    public JCheckBox getSecureCb() {
        return cbSecure;
    }
    
    public JCheckBox getSelfSignedCb() {
        return cbSelfSigned;
    }
    
    public void enableButton() {
        bt.setEnabled(true);
    }
    
    public void disableButton() {
        bt.setEnabled(false);
    }
    
    public int getSelectedServerIndex() {
        return cbServers.getSelectedIndex();
    }
    
    @Override
    protected void initComponents() {
        setTitle("Szerverek");
        initToolbar();
        initContent(ADD);
        pack(10, 10);
    }
    
    public static final int CREATE = 0;
    public static final int ADD = 1;
    public static final int DEL = 2;
    
    private void setReadonly() {
        tfAddres.setEditable(false);
        tfPath.setEditable(false);
        tfPort.setEditable(false);
        cbSecure.setEnabled(false);
        cbSelfSigned.setEnabled(false);
        cbVisible.setEnabled(false);
    }
    
    public int getMode() {
        return mode;
    }
    
    private void setMode(int mode) {
        this.mode = mode;
        switch(mode) {
            case CREATE:
                bt.setText("Módosít");
                bt.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage("CreateServer");
                    }
                    
                });
                break;
            case ADD:
                bt.setText("Hozzáad");
                bt.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage("AddServer");
                    }
                    
                });
                ActionListener l = new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cbVisible.setSelected(!cbVisible.isSelected());
                    }
                    
                };
                cbVisible.addActionListener(l);
                break;
            case DEL:
                bt.setText("Töröl");
                bt.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage("DeletteServer");
                    }
                    
                });
                setReadonly();
                break;
        }
    }
    
    private void removeContent() {
        if (content != null) {
            remove(content);
        }
    }
    
    private void initContent(int mode) {
        removeContent();
        done = false;
        content = new JPanel(new GridBagLayout());
        add(content, BorderLayout.CENTER);
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 2;
        c.fill = c.BOTH;
        JPanel urlPanel = initUrlPanel();
        urlPanel.setBorder(BorderFactory.createTitledBorder("Elérési útvonal"));
        content.add(urlPanel, c);
        
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.anchor = c.PAGE_START;
        c.fill = c.HORIZONTAL;
        JPanel connPanel = initConnectionPanel();
        connPanel.setBorder(BorderFactory.createTitledBorder("Tulajdonságok"));
        content.add(connPanel, c);
        
        c.gridy = 2;
        c.fill = c.NONE;
        c.anchor = c.CENTER;
        c.insets = new Insets(5, 0, 0, 0);
        bt = new JButton();
        bt.setEnabled(false);
        content.add(bt, c);
        
        if (mode != DEL) setEventHandlers();
        setMode(mode);
    }
    
    private JPanel initConnectionPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = c.LINE_END;
        p.add(new JLabel("Titkosított kapcsolat:"), c);
        c.gridy = 2;
        p.add(new JLabel("Önaláírt tanusítvány:"), c);
        c.gridy = 3;
        p.add(new JLabel("Listában látható:"), c);
        
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = c.LINE_START;
        cbSecure = new JCheckBox();
        p.add(cbSecure, c);
        c.gridy = 2;
        cbSelfSigned = new JCheckBox();
        p.add(cbSelfSigned, c);
        c.gridy = 3;
        cbVisible = new JCheckBox();
        cbVisible.setSelected(true);
        p.add(cbVisible, c);
        return p;
    }
    
    private void help() {
        initContent(ADD);
        fill(new ServerInfo(
                "fzoli.dyndns.org", 
                "4032", 
                "mill", 
                true, 
                true, 
                true));
    }
    
    private void create() {
        initContent(CREATE);
        fill();
    }
    
    private void delette() {
        initContent(DEL);
        fill();
    }
    
    private void add() {
        initContent(ADD);
        valueChanged();
    }
    
    private void fill() {
        sendMessage("Fill");
    }
    
    public void fill(ServerInfo i) {
        tfAddres.setText(i.getDomain());
        tfPort.setText(i.getPort());
        tfPath.setText(i.getPath());
        cbSecure.setSelected(i.isSecure());
        cbSelfSigned.setSelected(i.isSelfSigned());
        cbVisible.setSelected(i.isVisible());
        valueChanged();
    }
    
    private void initToolbar() {
        JToolBar tb = new JToolBar();
        tb.setBorder(Core.createToolbarBorder());
        tb.setFloatable(false);
        add(tb, BorderLayout.NORTH);
        
        JButton btAdd = Core.createToolbarButton("add.png", "Új szerver");
        btAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
            
        });
        tb.add(btAdd);
        JButton btHelp = Core.createToolbarButton("help.png", "Példa");
        btHelp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                help();
            }
            
        });
        tb.add(btHelp);
        tb.addSeparator();
        JPanel server = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = c.LINE_START;
        c.fill = c.HORIZONTAL;
        
        tb.add(server);
        c.weightx = 1; //emiatt nő a ComboBox hossza (ha az oprendszer támogatja) ...
        cbServers = new JComboBox();
        cbServers.setFocusable(false);
        server.add(cbServers, c);
        JToolBar tbS = new JToolBar();
        tbS.setFloatable(false);
        // ... de ha ide tenném, akkor nem nőne 
        server.add(tbS, c);
        btEdit = Core.createToolbarButton("edit.png", "Szerver szerkesztése");
        btEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                create();
            }
            
        });
        tbS.add(btEdit);
        btDel = Core.createToolbarButton("delette.png", "Szerver törlése");
        btDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delette();
            }
            
        });
        tbS.add(btDel);
    }
    
    private JPanel initUrlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = c.LINE_END;
        c.insets = new Insets(5, 5, 5, 0);
        panel.add(new JLabel("Cím:"), c);
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 0);
        panel.add(new JLabel("Port:"), c);
        c.gridy = 3;
        panel.add(new JLabel("Útvonal:"), c);
        
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = c.LINE_END;
        c.fill = c.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        tfAddres = new JTextField(10);
        panel.add(tfAddres, c);
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 5);
        tfPort = new JTextField();
        panel.add(tfPort, c);
        c.gridy = 3;
        tfPath = new JTextField();
        panel.add(tfPath, c);
        return panel;
    }
    
    private void setEventHandlers() {
        KeyAdapter ka = new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                valueChanged();
            }
            
        };
        tfAddres.addKeyListener(ka);
        tfPort.addKeyListener(ka);
        tfPath.addKeyListener(ka);
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                valueChanged();
            }
            
        };
        cbSecure.addActionListener(al);
        cbSelfSigned.addActionListener(al);
        cbVisible.addActionListener(al);
    }
    
    private void valueChanged() {
        if (!done) sendMessage("ValueChanged");
    }
    
    public ServerInfo createServerInfo() {
        return new ServerInfo(
                tfAddres.getText(),
                tfPort.getText(),
                tfPath.getText(),
                cbSecure.isSelected(),
                cbSelfSigned.isSelected(),
                cbVisible.isSelected());
    }
    
    public void done() {
        setReadonly();
        bt.setEnabled(false);
        done = true;
    }
}