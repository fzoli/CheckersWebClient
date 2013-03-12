package checkers.gui.view;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import checkers.core.InputValidator;
import checkers.gui.controll.Controller;
import checkers.gui.view.core.Core;

public class SystemTrayIcon {

    private Controller controller;
    private SystemTray st;
    private MenuItem miLog;
    private TrayIcon icon = new TrayIcon(Core.getCheckersImage());
    
    public SystemTrayIcon(Controller controller) {
        this.controller = controller;
        init();
    }
    
    public void setLogEnabled(boolean value) {
        miLog.setEnabled(value);
    }
    
    public boolean isSupported() {
        return SystemTray.isSupported();
    }
    
    public void showWrongUsernameMessage() {
        showWrongMessage(InputValidator.getUserIdConstrain());
    }
    
    public void showWrongPlaynameMessage() {
        showWrongMessage(InputValidator.getGameNameConstrain());
    }
    
    public void showWrongPasswordMessage() {
        showWrongMessage(InputValidator.getPasswordConstrain());
    }
    
    private void showWrongMessage(String message) {
        icon.displayMessage("Bevitel hiba!", message, TrayIcon.MessageType.WARNING);
    }
    
    public void showDuplicatedApplicationMessage() {
        icon.displayMessage("Hack Shield", "A program már fut.", TrayIcon.MessageType.INFO);
    }
    
    private void setDoubleClickEvent() {
        icon.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.receiveMessage("TrayEvent");
            }

        });
    }
    
    private void init() {
        if (isSupported()) {
            st = SystemTray.getSystemTray();
            setDoubleClickEvent();
            try {
                st.add(icon);
                icon.setToolTip("Online Dáma");
                icon.setImageAutoSize(true);
                PopupMenu menu = new PopupMenu();
                icon.setPopupMenu(menu);
                miLog = new MenuItem("Napló");
                menu.add(miLog);
                miLog.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.receiveMessage("Log");
                    }
                });
                menu.addSeparator();
                MenuItem exit = new MenuItem("Kilép");
                menu.add(exit);
                exit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.receiveMessage("Exit");
                    }
                });
            }
            catch(AWTException ex) {}
        }
    }
    
    public void dispose() {
        if (isSupported()) st.remove(icon);
    }
    
}