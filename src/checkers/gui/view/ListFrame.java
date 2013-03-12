package checkers.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;
import checkers.gui.controll.Controller;
import checkers.gui.view.core.Core;
import checkers.gui.view.core.MainFrame;
import checkers.gui.view.list.ListTable;

public class ListFrame extends MainFrame {
    
    private ListTable table;
    private JTextField tfSearch;
    private JButton btCreate, btConnect, btReset, btDel;
    private JMenuItem miCreate, miConnect, miReset, miDel, miLog;
    
    public ListFrame(Controller controller) {
        super(controller);
        setMinimumSize(new Dimension(getPreferredSize().width, 0));
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b && isVisible()) setExtendedState(NORMAL);
    }

    public void setTableModel(TableModel tableModel) {
        table.setModel(tableModel);
    }
    
    public void setLogEnabled(boolean value) {
        miLog.setEnabled(value);
    }
    
    public void enablePlayOwnerButtons() {
        btReset.setEnabled(true);
        btDel.setEnabled(true);
        miReset.setEnabled(true);
        miDel.setEnabled(true);
    }
    
    public void disablePlayOwnerButtons() {
        btReset.setEnabled(false);
        btDel.setEnabled(false);
        miReset.setEnabled(false);
        miDel.setEnabled(false);
    }
    
    public void enablePlayConnectButtons() {
        btConnect.setEnabled(true);
        miConnect.setEnabled(true);
    }
    
    public void disablePlayConnectButtons() {
        btConnect.setEnabled(false);
        miConnect.setEnabled(false);
    }
    
    public void enablePlayCreateButtons() {
        btCreate.setEnabled(true);
        miCreate.setEnabled(true);
    }
    
    public void disablePlayCreateButtons() {
        btCreate.setEnabled(false);
        miCreate.setEnabled(false);
    }
    
    @Override
    protected void initComponents() {
        setTitle("Online Dáma");
        setSize(500, 300);
        setExitListener();
        initMenu();
        initToolbar();
        initTable();
        setPlayButtonEvents();
    }
    
    public void listTableRowSelected() {
        sendMessage("PlaySelected");
    }
    
    public int getSelectedPlayIndex() {
        return table.getSelectedRowIndex();
    }
    
    private void initTable() {
        table = new ListTable(this);
        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBorder(BorderFactory.createEmptyBorder());
        add(tablePane, BorderLayout.CENTER);
    }
    
    private void initToolbar() {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.gridy = 1;
        
        c.gridx = 1;
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new GridBagLayout());
        toolbar.setBorder(Core.createToolbarBorder());
        add(toolbar, BorderLayout.PAGE_START);
        
        JToolBar tbPanel = new JToolBar();
        tbPanel.setFloatable(false);
        c.weightx = Integer.MAX_VALUE;
        c.anchor = c.LINE_START;
        c.fill = c.BOTH;
        toolbar.add(tbPanel, c);
        btCreate = createToolbarButton("add.png", "Játszma létrehozása");
        tbPanel.add(btCreate);
        tbPanel.addSeparator();
        btConnect = createToolbarButton("connect.png", "Játszmához kapcsolódás");
        tbPanel.add(btConnect);
        btReset = createToolbarButton("reset.png", "Játszma újraindítása");
        tbPanel.add(btReset);
        btDel = createToolbarButton("delette.png", "Játszma törlése");
        tbPanel.add(btDel);
        
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        JLabel lbSearch = new JLabel("Keresés: ");
        tfSearch = new JTextField(15);
        tfSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                table.setRowFilter(tfSearch.getText());
            }
            
        });
        searchPanel.add(lbSearch);
        searchPanel.add(tfSearch);
        c.gridx = 2;
        c.weightx = 1;
        c.anchor = c.LINE_END;
        c.fill = c.VERTICAL;
        JToolBar tbSearch = new JToolBar();
        tbSearch.setFloatable(false);
        tbSearch.add(searchPanel);
        toolbar.add(tbSearch, c);
    }
    
    private JButton createToolbarButton(String icon, String tooltipText) {
        return Core.createToolbarButton(icon, tooltipText);
    }
    
    private Icon getMenuIcon(String filename) {
        return Core.getMenuIcon(filename);
    }
    
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu user = new JMenu("Felhasználó");
        menuBar.add(user);
        JMenuItem signOut = new JMenuItem("Kijelentkezik");
        user.add(signOut);
        setAccelerator(signOut, KeyEvent.VK_Q);
        signOut.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                signOut();
            }
            
        });
        JMenuItem exit = new JMenuItem("Bezár");
        user.add(exit);
        exit.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
            
        });
        
        JMenu play = new JMenu("Játszma");
        menuBar.add(play);
        miLog = new JMenuItem("Napló");
        play.add(miLog);
        setAccelerator(miLog, KeyEvent.VK_L);
        miCreate = new JMenuItem("Létrehoz");
        play.add(miCreate);
        miCreate.setIcon(getMenuIcon("add.png"));
        setAccelerator(miCreate, KeyEvent.VK_A);
        play.addSeparator();
        miConnect = new JMenuItem("Csatlakozik");
        play.add(miConnect);
        miConnect.setIcon(getMenuIcon("connect.png"));
        setAccelerator(miConnect, KeyEvent.VK_C);
        miReset = new JMenuItem("Újraindít");
        miReset.setIcon(getMenuIcon("reset.png"));
        setAccelerator(miReset, KeyEvent.VK_R);
        play.add(miReset);
        miDel = new JMenuItem("Töröl");
        play.add(miDel);
        miDel.setIcon(getMenuIcon("delette.png"));
        setAccelerator(miDel, KeyEvent.VK_D);
        
        JMenu help = new JMenu("Súgó");
        menuBar.add(help);
        JMenuItem rules = new JMenuItem("Játékszabály");
        rules.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("GameRulesEvent");
            }
            
        });
        help.add(rules);
        setAccelerator(rules, KeyEvent.VK_F1);
        JMenuItem about = new JMenuItem("Névjegy");
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
            
        });
        help.add(about);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "A programot írta: Farkas Zoltán\n"
                + "EHA-kód: FAZQACG.GDF\n"
                + "Neptun-kód: DZ54IQ",
                "Névjegy",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setAccelerator(JMenuItem item, int event) {
        item.setAccelerator(KeyStroke.getKeyStroke(event, Event.CTRL_MASK));
    }
    
    private void setPlayButtonEvents() {
        miLog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("Log");
            }
            
        });
        ActionListener alCreate = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("CreatePlay");
            }
            
        };
        btCreate.addActionListener(alCreate);
        miCreate.addActionListener(alCreate);
        ActionListener alConnect = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("ConnectPlay");
            }
            
        };
        btConnect.addActionListener(alConnect);
        miConnect.addActionListener(alConnect);
        ActionListener alDel = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog("Biztos, hogy törli a kiválasztott játszmát?")) sendMessage("DelPlay");
            }
            
        };
        btDel.addActionListener(alDel);
        miDel.addActionListener(alDel);
        ActionListener alReset = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog("Biztos, hogy újraindítja a kiválasztott játszmát?\n\n"
                        + "Ha az igent válassza, a játszmából ki lesznek léptetve a játékosok.")) sendMessage("ResetPlay");
            }
            
        };
        btReset.addActionListener(alReset);
        miReset.addActionListener(alReset);
    }
    
    private boolean showConfirmDialog(String message) {
        return Core.showConfirmDialog(this, message);
    }
    
    private void exit() {
        sendMessage("Exit");
    }
    
    private void signOut() {
        sendMessage("SignOut");
    }
    
    private void iconify() {
        sendMessage("ListIconified");
    }
    
    private void setExitListener() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                iconify();
            }
            
        });
    }
    
    
    
}