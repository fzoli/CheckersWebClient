package mill.gui.view.checkerboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Checker {

    private final int PLAYER;
    private final Checkerboard BOARD;
    private final Color BORDER_COLOR;
    private static final int SIZE = 62;
    private Graphics2D gr;
    private int x, y;
    private boolean disposed;
    
    private static final Color[] COLORS = {Color.RED, Color.BLUE};
    
    public Checker(Checkerboard board, int player, Color borderColor, int x, int y) {
        this.gr = board.getCheckerGraphics();
        this.disposed = false;
        this.PLAYER = player;
        this.BOARD = board;
        this.BORDER_COLOR = borderColor;
        setX(x);
        setY(y);
        show();
    }
    
    private Color getColor() {
        return COLORS[PLAYER - 1];
    }
    
    public int getPlayer() {
        return PLAYER;
    }
    
    public void dispose() {
        disposed = true;
        hide();
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public int getX() {
        return x + getOffset();
    }
    
    public int getY() {
        return y + getOffset();
    }
    
    public void move(int x, int y) {
        hide();
        setX(x);
        setY(y);
        show();
        BOARD.repaint();
    }
    
    public void switchGraphics() {
        hide();
        gr = (gr == BOARD.getCheckerGraphics()) ? BOARD.getDragGraphics() : BOARD.getCheckerGraphics();
        show();
    }
    
    private void setX(int x) {
        this.x = x - getOffset();
    }
    
    private void setY(int y) {
        this.y = y - getOffset();
    }
    
    private int getOffset() {
        return SIZE / 2;
    }
    
    private void show() {
        if (!disposed) {
            BOARD.setGraphicsToDraw(gr);
            Shape shape = new Ellipse2D.Float(x, y, SIZE, SIZE);
            BasicStroke stroke = new BasicStroke(3);
            gr.setColor(getColor());
            gr.fill(shape);
            gr.setColor(BORDER_COLOR);
            gr.fill(stroke.createStrokedShape(shape));
        }
    }
    
    private void hide() {
        BOARD.setGraphicsToClear(gr);
        Shape shape = new Ellipse2D.Float(x - 3, y - 3, SIZE + 6, SIZE + 6);
        gr.fill(shape);
    }
    
}