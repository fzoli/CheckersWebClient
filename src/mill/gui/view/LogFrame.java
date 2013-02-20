package mill.gui.view;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import mill.gui.controll.Controller;
import mill.gui.view.play.GamePanel;
import mill.gui.view.play.LogPanel;
import mill.gui.view.play.PlayFrame;

public class LogFrame extends PlayFrame {

    private GamePanel gp;
    private LogPanel lp;
    
    public LogFrame(Controller controller, JFrame owner) {
        super(controller, owner);
    }

    public GamePanel getGamePanel() {
        return gp;
    }

    public LogPanel getLogPanel() {
        return lp;
    }
    
    @Override
    protected void initComponents() {
        setTitle("Napló");
        add(createPane());
        pack(10, 10);
    }

    @Override
    public void pack() {
        super.pack();
        setEqualToolbarHeight();
        setLogPanelBigger();
    }
    
    private JSplitPane createPane() {
        JSplitPane jsp = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createLogPanel(),
                createGamePanel()
        );
        jsp.setContinuousLayout(true);
        jsp.setDividerSize(2);
        jsp.setBorder(null);
        return jsp;
    }
    
    private GamePanel createGamePanel() {
        gp = new GamePanel(this, false);
        return gp;
    }
    
    private LogPanel createLogPanel() {
        lp = new LogPanel(this);
        return lp;
    }
    
    private void setEqualToolbarHeight() {
        int height = Math.max(gp.getToolbarHeight(), lp.getToolbarHeight());
        gp.setToolbarHeight(height);
        lp.setToolbarHeight(height);
    }

    private void setLogPanelBigger() {
        lp.setMinimumSize(new Dimension(lp.getSize().width + 5, 0));
    }

}