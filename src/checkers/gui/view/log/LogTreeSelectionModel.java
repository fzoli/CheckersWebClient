package checkers.gui.view.log;

import checkers.gui.view.log.userobject.LogObject;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import checkers.gui.view.log.userobject.GameLogObject;

public class LogTreeSelectionModel extends DefaultTreeSelectionModel {

    private final LogTree TREE;
    
    public LogTreeSelectionModel(LogTree tree) {
        setSelectionMode(SINGLE_TREE_SELECTION);
        TREE = tree;
        initEvent();
    }

    private void initEvent() {
        addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = getSelectionPath();
                if (path != null) {
                    LogObject uo = (LogObject) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                    TREE.setPopupMenu(uo.getMenu());
                    if (uo instanceof GameLogObject) TREE.setSelectedGame(((GameLogObject) uo).getGameKey());
                    else TREE.setSelectedGame(null);
                }
                else {
                    TREE.setRefreshMenu();
                    TREE.setSelectedGame(null);
                }
            }
            
        });
    }
    
}