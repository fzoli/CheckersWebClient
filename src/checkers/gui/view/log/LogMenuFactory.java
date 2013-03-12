package checkers.gui.view.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import checkers.gui.controll.Controller;

public class LogMenuFactory {
    
    private static Controller controller;
    private static LogMenu deleteMenu, openDeleteMenu, refreshMenu;
    
    public static void setController(Controller aController) {
        controller = aController;
        deleteMenu = null;
        openDeleteMenu = null;
        refreshMenu = null;
    }
    
    public static final int DELETE = 1;
    public static final int OPEN_DELETE = 2;
    public static final int REFRESH = 3;
    
    public static LogMenu createLogMenu(int type) {
        if (controller != null) {
            switch(type) {
                case DELETE: return createDeleteMenu();
                case OPEN_DELETE: return createOpenDeleteMenu();
                case REFRESH: return createRefreshMenu();
            }
        }
        return null;
    }

    private static LogMenu createRefreshMenu() {
        if (refreshMenu == null) {
            refreshMenu = new LogMenu(controller);
            addRefreshItem(refreshMenu);
        }
        return refreshMenu;
    }
    
    private static LogMenu createDeleteMenu() {
        if (deleteMenu == null) {
            deleteMenu = new LogMenu(controller);
            addDeleteItem(deleteMenu);
        }
        return deleteMenu;
    }
    
    private static LogMenu createOpenDeleteMenu() {
        if (openDeleteMenu == null) {
            openDeleteMenu = new LogMenu(controller);
            addOpenItem(openDeleteMenu);
            openDeleteMenu.addSeparator();
            addDeleteItem(openDeleteMenu);
        }
        return openDeleteMenu;
    }
    
    private static void addDeleteItem(LogMenu menu) {
        addMenuItem(menu, "Töröl", "DeleteEvent");
    }
    
    private static void addOpenItem(LogMenu menu) {
        addMenuItem(menu, "Megnyit", "OpenEvent");
    }
    
    private static void addRefreshItem(LogMenu menu) {
        addMenuItem(menu, "Frissít", "RefreshEvent");
    }
    
    private static void addMenuItem(final LogMenu menu, final String text, final String eventMessage) {
        JMenuItem mi = new JMenuItem(text);
        menu.add(mi);
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                menu.sendMessage(eventMessage);
            }
            
        });
    }
    
}