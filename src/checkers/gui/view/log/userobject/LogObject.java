package checkers.gui.view.log.userobject;

import javax.swing.Icon;
import checkers.gui.view.log.LogMenu;

public class LogObject {

    private String text;
    private Icon icon;
    private LogMenu menu;

    public LogObject(String text, Icon icon) {
        this(text, icon, null);
    }

    public LogObject(String text, Icon icon, LogMenu menu) {
        this.text = text;
        this.icon = icon;
        this.menu = menu;
    }

    public String getText() {
        return text;
    }

    public Icon getIcon() {
        return icon;
    }

    public LogMenu getMenu() {
        return menu;
    }
    
}