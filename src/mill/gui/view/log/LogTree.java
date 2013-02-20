package mill.gui.view.log;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.view.log.userobject.GameLogObject;
import mill.gui.view.log.userobject.LogObject;

public class LogTree extends JTree {
    
    private LogMenu menu;
    private GameKey selectedGame, expandedGame;
    private TreePath expandedPath;
    private boolean enableExpandEvent;
    private boolean enableEvents;
    
    public LogTree() {
        setMouseEvent();
        setExpandEvent();
        setRootVisible(false);
        setEnableEvents(true);
        setShowsRootHandles(true);
        setModel(new LogTreeModel());
        setCellRenderer(new LogTreeCellRenderer());
        setSelectionModel(new LogTreeSelectionModel(this));
    }
    
    private LogTreeModel getLogTreeModel() {
        return ((LogTreeModel)getModel());
    }
    
    public void updateModel(List<GameKey> games) {
        getLogTreeModel().update(games);
    }
    
    public GameKey getSelectedGame() {
        return selectedGame;
    }
    
    public GameKey getExpandedGame() {
        return expandedGame;
    }
    
    public void setSelectedGame(GameKey game) {
        selectedGame = game;
    }
    
    public void setPopupMenu(LogMenu menu) {
        this.menu = menu;
    }
    
    private void setEnableEvents(boolean enabled) {
        enableEvents = enabled;
    }
    
    public void setMode(boolean enabled, boolean expand) {
        setEnableEvents(enabled);
        if (expand) setExpansion(enabled);
        setEnabled(enabled);
    }
    
    private void setExpansion(boolean expand) {
        if (expandedPath != null) {
            enableExpandEvent = false;
            if (expand) expandPath(expandedPath);
            else collapsePath(expandedPath);
            enableExpandEvent = true;
        }
    }
    
    private void setExpandEvent() {
        enableExpandEvent = true;
        addTreeExpansionListener(new TreeExpansionListener() {

            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                if (enableEvents && enableExpandEvent) {
                    expandedPath = event.getPath();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)expandedPath.getLastPathComponent();
                    LogObject lo = (LogObject) node.getUserObject();
                    if (lo instanceof GameLogObject) {
                        expandedGame = ((GameLogObject)lo).getGameKey();
                        sendMessage("ExpandEvent");
                    }
                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                ;
            }
            
        });
    }
    
    public void setRefreshMenu() {
        setPopupMenu(LogMenuFactory.createLogMenu(LogMenuFactory.REFRESH));
    }
    
    private void setMouseEvent() {
        setRefreshMenu();
        
        addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (enableEvents) {
                    int row = getRowForLocation(e.getX(), e.getY());
                    if (SwingUtilities.isRightMouseButton(e)) {
                        setSelectionRow(row);
                        if (menu != null) menu.show((JComponent) e.getSource(), e.getX(), e.getY());
                    }
                    else if (e.getClickCount() == 2) {
                        sendMessage("ClickEvent");
                    }
                }
            }
                
        });
        
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    sendMessage("OpenEvent");
            }
            
        });
    }
    
    private void sendMessage(String message) {
        if (menu != null) menu.sendMessage(message);
    }
    
}