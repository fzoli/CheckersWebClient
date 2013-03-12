package checkers.gui.view.play;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import checkers.gui.controll.play.entity.GameKey;
import checkers.gui.view.autocomplete.AutoCompleteCombo;
import checkers.gui.view.autocomplete.AutoCompleteModel;
import checkers.gui.view.core.ChildFrame;
import checkers.gui.view.core.Core;
import checkers.gui.view.log.LogTree;

public class LogPanel extends JPanel {
    
    private boolean updateProgress;
    private JToolBar tb;
    private LogTree tree;
    private JButton btPrev, btNext;
    private AutoCompleteCombo combo;
    private final ChildFrame OWNER;
    
    public LogPanel(ChildFrame owner) {
        OWNER = owner;
        init();
    }
    
    public int getSelectedCombo() {
        if (combo.getSelectedIndex() == -1) return -1;
        return Integer.parseInt((String) combo.getSelectedItem()) - 1;
    }
    
    private void setToolbarEnabled(boolean enabled) {
        setPrevBtEnabled(enabled);
        setNextBtEnabled(enabled);
        combo.setEnabled(enabled);
        combo.setEditable(enabled);
    }
    
    public void updateToolbar(int count) {
        setToolbarEnabled(true);
        updateProgress = true;
        for (int i = count; i >= 1; i--)
            getComboModel().addToTop(Integer.toString(i));
        updateProgress = false;
    }
    
    public void clearToolbar() {
        setToolbarEnabled(false);
        getComboModel().clear();
    }
    
    public void setSelectedCombo(int number) {
        number++;
        combo.setSelectedItem(Integer.toString(number));
    }
    
    private AutoCompleteModel getComboModel() {
        return (AutoCompleteModel) combo.getModel();
    }
    
    public void setPrevBtEnabled(boolean enabled) {
        btPrev.setEnabled(enabled);
    }
    
    public void setNextBtEnabled(boolean enabled) {
        btNext.setEnabled(enabled);
    }
    
    private void sendMessage(String msg) {
        OWNER.sendMessage(msg);
    }
    
    public void setLogSelectEnabled(boolean enabled) {
        tb.setEnabled(enabled);
    }
    
    public void setLogTreeMode(boolean enabled, boolean expand) {
        tree.setMode(enabled, expand);
    }
    
    public void updateLogTreeModel(List<GameKey> games) {
        tree.updateModel(games);
    }
    
    public GameKey getSelectedGame() {
        return tree.getSelectedGame();
    }
    
    public GameKey getExpandedGame() {
        return tree.getExpandedGame();
    }
    
    public int getToolbarHeight() {
        return tb.getSize().height;
    }
    
    public void setToolbarHeight(int height) {
        tb.setPreferredSize(new Dimension(0, height));
    }
    
    private void init() {
        updateProgress = false;
        setLayout(new BorderLayout());
        add(createToolbar(), BorderLayout.NORTH);
        add(createTree(), BorderLayout.CENTER);
    }
    
    private JScrollPane createTree() {
        tree = new LogTree();
        JScrollPane sp = new JScrollPane(tree);
        sp.setBorder(null);
        return sp;
    }
    
    private void setToolbar(JToolBar tb) {
        tb.setLayout(new GridBagLayout());
        tb.setFloatable(false);
    }
    
    private JToolBar createToolbar() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.fill = c.HORIZONTAL;
        
        JToolBar outer = new JToolBar();
        setToolbar(outer);
        outer.setBorder(Core.createToolbarBorder());
        
        tb = new JToolBar();
        outer.add(tb, c);
        setToolbar(tb);
        
        c.weightx = 0;
        c.insets = new Insets(0, 5, 0, 0);
        btPrev = Core.createToolbarButton("back.png", "Vissza");
        btPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("PreviousLog");
            }
            
        });
        tb.add(btPrev, c);
        
        c.weightx = 1;
        c.insets = new Insets(0, 5, 0, 5);
        combo = new AutoCompleteCombo();
        combo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updateProgress) sendMessage("ComboSelectEvent");
            }
            
        });
        tb.add(combo, c);
        
        c.weightx = 0;
        c.fill = c.NONE;
        c.insets = new Insets(0, 0, 0, 5);
        btNext = Core.createToolbarButton("next.png", "Előre");
        btNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("NextLog");
            }
            
        });
        tb.add(btNext, c);
        
        return outer;
    }
    
}