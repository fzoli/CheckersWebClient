package checkers.gui.view.log;

import java.util.List;
import checkers.gui.view.log.userobject.LogObject;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import checkers.gui.controll.play.entity.DateInfo;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.core.Core;
import checkers.gui.view.log.userobject.DayLogObject;
import checkers.gui.view.log.userobject.GameLogObject;
import checkers.gui.view.log.userobject.MonthLogObject;
import checkers.gui.view.log.userobject.PlayLogObject;
import checkers.gui.view.log.userobject.ServerLogObject;
import checkers.gui.view.log.userobject.TimeLogObject;
import checkers.gui.view.log.userobject.UserLogObject;
import checkers.gui.view.log.userobject.YearLogObject;

public class LogTreeModel extends DefaultTreeModel {

    private DefaultMutableTreeNode root;
    
    public LogTreeModel() {
        super(null);
        initRootNode();
    }
    
    public void update(List<GameKey> games) {
        root.removeAllChildren();
        
        for (GameKey game : games) {
            String server = game.getServer();
            String user = game.getUser();
            String play = game.getName();
            DateInfo dateInfo = game.getDateInfo();
            Integer year = dateInfo.getYear();
            Integer month = dateInfo.getMonth();
            Integer day = dateInfo.getDay();
            
            DefaultMutableTreeNode serverNode = getGameNode(new ServerLogObject(server));
            root.add(serverNode);
            DefaultMutableTreeNode userNode = getGameNode(new UserLogObject(user, server));
            serverNode.add(userNode);
            DefaultMutableTreeNode playNode = getGameNode(new PlayLogObject(play, user, server));
            userNode.add(playNode);
            DefaultMutableTreeNode yearNode = getGameNode(new YearLogObject(year, play, user, server));
            playNode.add(yearNode);
            DefaultMutableTreeNode monthNode = getGameNode(new MonthLogObject(month, year, play, user, server));
            yearNode.add(monthNode);
            DefaultMutableTreeNode dayNode = getGameNode(new DayLogObject(day, month, year, play, user, server));
            monthNode.add(dayNode);
            DefaultMutableTreeNode timeNode = getGameNode(new TimeLogObject(game));
            dayNode.add(timeNode);
        }
        
        reload();
    }
    
    private DefaultMutableTreeNode getGameNode(GameLogObject object) {
        GameKey gk = object.getGameKey();
        DefaultMutableTreeNode node = findNode(gk);
        if (node == null) return createNode(object);
        return node;
    }
    
    private DefaultMutableTreeNode findNode(GameKey gameKey) {
        return findNode(gameKey, root);
    }
    
    private DefaultMutableTreeNode findNode(GameKey gameKey, TreeNode node) {
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            TreeNode tn = node.getChildAt(i);
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tn;
            GameLogObject glo = (GameLogObject) dmtn.getUserObject();
            if (glo.getGameKey().compareTo(gameKey) == 0) return dmtn;
            DefaultMutableTreeNode n = findNode(gameKey, tn);
            if (n != null) return n;
        }
        return null;
    }
    
    private void initRootNode() {
        root = createNode(new LogObject("Napló", Core.getLogIcon("log.png")));
        setRoot(root);
    }
    
    private DefaultMutableTreeNode createNode(LogObject label) {
        return new DefaultMutableTreeNode(label);
    }
    
}