package checkers.gui.view.checkerboard;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CheckerboardArea extends BufferedImage {
    
    public CheckerboardArea(int size) {
        super(size, size, TYPE_INT_ARGB);
    }
    
    public Graphics2D getGraphics2D() {
        return (Graphics2D) getGraphics();
    }
    
}