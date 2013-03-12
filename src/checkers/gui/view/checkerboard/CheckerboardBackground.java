package checkers.gui.view.checkerboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class CheckerboardBackground extends CheckerboardArea {
    
    private final int SIZE;
    private static final int BORDER_SIZE = 6;
    private static final Color CELL_COLOR = new Color(50, 110, 50);
    private static final Color GAP_COLOR = new Color(250, 200, 30);
    private static final BasicStroke BORDER_STROKE = new BasicStroke(11); //nem értem, miért 11-gyel rajzol 6 pixel vastagon
    private final Shape BORDER = new Rectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1);
    
    public CheckerboardBackground(int size) {
        super(size);
        SIZE = size - (2 * BORDER_SIZE);
        draw();
    }

    public static int getBorderSize() {
        return BORDER_SIZE;
    }
    
    private void draw() {
        int cellSize = SIZE / 8;
        Graphics2D g = (Graphics2D) getGraphics();
        int col = 0;
        for (int i = BORDER_SIZE; i < SIZE; i += cellSize) {
            for (int row = 0; row < 8; row++) {
                g.setColor(Common.isGap(row + 1, col + 1) ? GAP_COLOR : CELL_COLOR);
                g.fillRect(BORDER_SIZE + row * cellSize, i, cellSize, cellSize);
            }
            col++;
        }
        g.setColor(Color.BLACK);
        g.fill(BORDER_STROKE.createStrokedShape(BORDER));
    }

}