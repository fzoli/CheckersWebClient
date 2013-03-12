package checkers.gui.view.core;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JPanel;
import checkers.gui.controll.Controller;

public abstract class StatusBarFrame extends MyFrame {

    private JPanel content;
    private StatusBar statusBar = new StatusBar();
    
    public StatusBarFrame(Controller controller) {
        super(controller);
        initStatusBar();
        initComponents();
    }
    
    public StatusBar getStatusBar() {
        return statusBar;
    }
    
    protected abstract void initComponents();
    
    private void initStatusBar() {
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.PAGE_END);
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        this.content = content;
    }

    @Override
    public Container getContentPane() {
        if (content != null) return content;
        else return super.getContentPane();
    }
    
}