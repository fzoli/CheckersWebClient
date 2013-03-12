package checkers.gui.view.core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DropDownButton extends JLabel {
    
    private boolean closed = false;
    private boolean lighter = false;
    private boolean pressed = false;
    private final JPanel P1, P2;
    private final String TEXT;
    private final String[] TEXT_CLOSES = new String[] {"megjelenítése", "elrejtése"};

    public DropDownButton(String text, JPanel p) {
        this(text, p, null);
    }
    
    public DropDownButton(String text, JPanel p1, JPanel p2) {
        super("", CENTER);
        this.P1 = p1;
        this.P2 = p2;
        this.TEXT = text;
        init();
    }
    
    private void change() {
        closed = !closed;
        draw();
        setToolTipText(TEXT + " " + TEXT_CLOSES[closed ? 0 : 1]);
        P1.setVisible(!closed);
        if (P2 != null) P2.setVisible(closed);
    }
    
    private void draw() {
        setIcon(getDownIcon());
    }
    
    private Icon getDownIcon() {
        int i = 0;
        if (pressed) i = 2;
        else if (lighter) i = 1;
        String s = closed ? "down" : "up";
        return Core.getDropDownIcon(s + i + ".png");
    }
    
    private void init() {
        change();
        MouseAdapter ma = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                draw();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                draw();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lighter = true;
                draw();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lighter = false;
                draw();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    change();
                }
            }
            
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }
    
}