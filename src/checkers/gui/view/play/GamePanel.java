package checkers.gui.view.play;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import checkers.gui.view.checkerboard.Checkerboard;
import checkers.gui.view.core.Core;
import checkers.core.entity.GameChecker;

public class GamePanel extends JPanel {

    private Checkerboard cb;
    private JButton btGiveUp, btStartStop;
    private JLabel lbMessage, lbRedHit, lbBlueHit, lbTime;
    private final PlayFrame FRAME;
    private final boolean LIVE;
    private JToolBar tbLeft;
    
    public GamePanel(PlayFrame frame, boolean live) {
        FRAME = frame;
        LIVE = live;
        init();
    }
    
    public int getToolbarHeight() {
        return tbLeft.getSize().height;
    }
    
    public void setDragable(boolean dragable) {
        cb.setDragable(dragable);
    }
    
    public void setToolbarHeight(int height) {
        tbLeft.setPreferredSize(new Dimension(0, height));
    }
    
    public void updateCheckers(List<GameChecker> checkers) {
        cb.removeCheckers();
        for (GameChecker c : checkers) {
            cb.addChecker(c.getPlayer(), c.getType(), c.getRow(), c.getCol());
        }
        repaint();
    }
    
    public void setGameMessage(String msg) {
        lbMessage.setText(msg);
        lbMessage.setToolTipText(msg);
    }
    
    public void setRedHit(String value) {
        lbRedHit.setText(value);
    }
    
    public void setBlueHit(String value) {
        lbBlueHit.setText(value);
    }
    
    public void setTime(String time) {
        lbTime.setText(time);
    }
    
    public void setStartStopButton(String text, Icon icon, boolean enable) {
        btStartStop.setEnabled(enable);
        btStartStop.setToolTipText(text);
        btStartStop.setIcon(icon);
    }
    
    public void setGiveUpButton(boolean enable) {
        btGiveUp.setEnabled(enable);
    }
    
    private void init() {
        setOpaque(false);
        setLayout(new BorderLayout());
        cb = new Checkerboard(FRAME);
        add(cb, BorderLayout.CENTER);
        initToolbar();
    }
    
    public Point getMovedFrom() {
        return cb.getMovedFrom();
    }

    public Point getMovedTo() {
        return cb.getMovedTo();
    }
    
    public void setMoveable(boolean value) {
        cb.setMoveable(value);
    }
    
    private void setToolbar(JToolBar tb) {
        tb.setFloatable(false);
    }
    
    private void initToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridy = 1;
        c.fill = c.BOTH;
        setToolbar(toolbar);
        toolbar.setBorder(Core.createToolbarBorder());
        add(toolbar, BorderLayout.PAGE_START);
        
        tbLeft = new JToolBar();
        setToolbar(tbLeft);
        c.gridx = 1;
        c.weightx = 1;
        c.anchor = c.PAGE_START;
        toolbar.add(tbLeft, c);
        
        JToolBar tbRight = new JToolBar();
        setToolbar(tbRight);
        c.gridx = 2;
        c.weightx = 0;
        c.anchor = c.PAGE_END;
        toolbar.add(tbRight, c);
        
        btStartStop = Core.createToolbarButton();
        btStartStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FRAME.sendMessage("StartStopEvent");
            }

        });
       
        btGiveUp = Core.createToolbarButton("give-up.png", "Felad");
        btGiveUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Core.showConfirmDialog(FRAME, "Biztos, hogy feladja a játszmát?")) FRAME.sendMessage("GiveUpEvent");
            }

        });

        if (LIVE) {
            tbLeft.add(btStartStop);
            tbLeft.add(btGiveUp);
            tbLeft.addSeparator();
        }
        
        lbMessage = new JLabel();
        setLabelFont(lbMessage);
        lbMessage.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        tbLeft.add(lbMessage);
        
        lbRedHit = new JLabel("0");
        setLabelFont(lbRedHit);
        lbRedHit.setForeground(Color.RED);
        tbRight.add(lbRedHit);
        JLabel lb = new JLabel(" - ");
        setLabelFont(lb);
        tbRight.add(lb);
        lbBlueHit = new JLabel("0");
        lbBlueHit.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        setLabelFont(lbBlueHit);
        lbBlueHit.setForeground(Color.BLUE);
        tbRight.add(lbBlueHit);
        tbRight.addSeparator();
        lbTime = new JLabel("00:00:00");
        lbTime.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        setLabelFont(lbTime);
        tbRight.add(lbTime);
    }
    
    private void setLabelFont(JLabel lb) {
        lb.setFont(new Font("Arial", Font.PLAIN, 22));
    }
    
}