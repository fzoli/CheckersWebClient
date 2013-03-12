package checkers.gui.view.checkerboard;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import checkers.gui.view.core.MyFrame;

public class Checkerboard extends JLayeredPane {

    private final MyFrame OWNER;
    private static final int SIZE = 572;
    private static final int BORDER_SIZE = CheckerboardBackground.getBorderSize();
    private boolean dragable;
    private Graphics2D grChecker, grDrag;
    private JLabel lbBackground, lbCheckers, lbDrag;
    
    private List<Checker> checkers = new ArrayList<Checker>() {

        @Override
        public boolean remove(Object o) {
            ((Checker)o).dispose();
            return super.remove(o);
        }

        @Override
        public Checker remove(int index) {
            Checker c = this.get(index);
            this.remove(c);
            return c;
        }

        @Override
        public void clear() {
            int size = size();
            for (int i = 0; i < size; i++) {
                this.remove(0);
            }
        }
        
    };
    
    private ComponentAdapter resizeListener = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            setLayersToMiddle();
        }
        
    };
    
    private MouseAdapter mouseListener = new MouseAdapter() {
        
        private Checker c;
        private int x, y, startX, startY;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (!dragable) return;
            x = e.getX();
            y = e.getY();
            c = getSelectedChecker();
            if (c == null) return;
            startX = c.getX();
            startY = c.getY();
            c.switchGraphics();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (c == null || !dragable) return;
            int cX = c.getX() + e.getX() - x;
            int cY = c.getY() + e.getY() - y;
            c.move(cX, cY);
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (c == null || !dragable) return;
            Point p = getCheckerCellPosition(x, y);
            if (Common.isGap(p.x, p.y) || !isCellEmpty(c, p.x, p.y)) {
                c.move(startX, startY);
            }
            else {
                Point to = getCheckerAreaPosition(p.x, p.y);
                c.move(to.x, to.y);
                checkerMoved(c, startX, startY);
            }
            c.switchGraphics();
        }
        
        private Checker getSelectedChecker() {
            return getChecker(this.x - BORDER_SIZE, this.y - BORDER_SIZE);
        }
        
    };
    
    public Checkerboard(MyFrame owner) {
        this.OWNER = owner;
        this.dragable = false;
        initLayers();
        setMouseListener();
        addComponentListener(resizeListener);
        setSize();
    }
    
    private void setSize() {
        Dimension d = new Dimension(580, 580);
        setPreferredSize(d);
        setMinimumSize(d);
    }
    
    public void setDragable(boolean value) {
        dragable = value;
    }
    
    public void addChecker(int player, String type, int row, int col) {
        Point p = getCheckerAreaPosition(row, col);
        Color borderColor = type.equals("basic") ? Color.BLACK : Color.WHITE;
        Checker c = new Checker(this, player, borderColor, p.x, p.y);
        checkers.add(c);
    }
    
    public void removeCheckers() {
        checkers.clear();
    }
    
    protected Graphics2D getCheckerGraphics() {
        return grChecker;
    }
    
    protected Graphics2D getDragGraphics() {
        return grDrag;
    }
    
    protected void setGraphicsToDraw(Graphics2D g) {
        g.setComposite(
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)
        );
    }
    
    protected void setGraphicsToClear(Graphics2D g) {
        g.setComposite(
            AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f)
        );
    }
    
    private boolean isAbleMove = false;
    
    public void setMoveable(boolean value) {
        isAbleMove = value;
    }
    
    private Point movedTo;
    private Point movedFrom;
    
    public Point getMovedFrom() {
        return movedFrom;
    }

    public Point getMovedTo() {
        return movedTo;
    }
    
    private void checkerMoved(Checker c, int fromX, int fromY) {
        if (isAbleMove) {
            movedTo = getCheckerCellPosition(c);
            movedFrom = getCheckerCellPosition(fromX, fromY);
            OWNER.sendMessage("CheckerMoved");
        }
        else c.move(fromX, fromY);
    }
    
    private Checker getChecker(int x, int y) {
        for (Checker c : checkers) {
            if (isPointOnChecker(c, x, y))
                return c;
        }
        return null;
    }
    
    private boolean isPointOnChecker(Checker c, int x, int y) {
        int u = c.getX();
        int v = c.getY();
        int r = c.getSize() / 2;
        return sqr(x-u) + sqr(y-v) < sqr(r);
    }
    
    private double sqr(int number) {
        return Math.pow(number, 2);
    }
    
    private void setMouseListener() {
        lbCheckers.addMouseListener(mouseListener);
        lbCheckers.addMouseMotionListener(mouseListener);
    }
    
    private Point getCheckerAreaPosition(int row, int col) {
        int x = getNumberAreaPosition(col);
        int y = getNumberAreaPosition(row);
        return new Point(x, y);
    }
    
    private int getNumberAreaPosition(int number) {
        int cellSize = getCellSize();
        return (cellSize * number) - (cellSize / 2);
    }
    
    private Point getCheckerCellPosition(Checker c) {
        return getCheckerCellPosition(c.getX(), c.getY());
    }
    
    private Point getCheckerCellPosition(int x, int y) {
        int col = getNumberCellPosition(x);
        int row = getNumberCellPosition(y);
        return new Point(row, col);
    }
    
    private int getNumberCellPosition(int number) {
        return (number - BORDER_SIZE) / getCellSize() + 1;
    }
    
    private int getCellSize() {
        return getAreaSize() / 8;
    }
    
    private boolean isCellEmpty(Checker checker, int row, int col) {
        for (Checker c : checkers) {
            if (c != checker) {
                Point p = getCheckerCellPosition(c);
                if (p.x == row && p.y == col)
                    return false;
            }
        }
        return true;
    }
    
    private void initLayers() {
        CheckerboardArea imgBackground = new CheckerboardBackground(SIZE);
        lbBackground = setLayer(imgBackground, JLayeredPane.DEFAULT_LAYER);
        CheckerboardArea imgCheckers = createLayer();
        grChecker = imgCheckers.getGraphics2D();
        lbCheckers = setLayer(imgCheckers, JLayeredPane.PALETTE_LAYER);
        CheckerboardArea imgDrag = createLayer();
        grDrag = imgDrag.getGraphics2D();
        lbDrag = setLayer(imgDrag, JLayeredPane.POPUP_LAYER);
        setRenderingHints();
    }
    
    private void setLayersToMiddle() {
        setLayerToMiddle(lbBackground);
        setLayerToMiddle(lbCheckers);
        setLayerToMiddle(lbDrag);
    }
    
    private CheckerboardArea createLayer() {
        return new CheckerboardArea(getAreaSize());
    }
    
    private int getAreaSize() {
        return SIZE - BORDER_SIZE * 2;
    }
    
    private JLabel setLayer(CheckerboardArea img, int position) {
        JLabel lb = new JLabel(new ImageIcon(img));
        add(lb);
        setLayer(lb, position);
        return lb;
    }
    
    private void setLayerToMiddle(JLabel lb) {
        Dimension s = getPaneSize();
        int x = (s.width - SIZE) / 2;
        int y = (s.height - SIZE) / 2;
        lb.setBounds(x, y, SIZE, SIZE);
    }
    
    private Dimension getPaneSize() {
        Dimension answer = new Dimension();
        Dimension s = getSize();
        answer.width = s.width < SIZE ? SIZE : s.width;
        answer.height = s.height < SIZE ? SIZE : s.height;
        return answer;
    }
    
    private void setRenderingHints() {
        setRenderingHint(grChecker);
        setRenderingHint(grDrag);
    }
    
    private void setRenderingHint(Graphics2D g) {
        g.addRenderingHints(
              new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON)
        ); 
    }
    
}