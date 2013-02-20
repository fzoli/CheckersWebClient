package mill.gui.view.checkerboard;

public class Common {
    
    public static boolean isGap(int row, int col) {
        if (row < 1 || row > 8 || col < 1 || col > 8) return true;
        return !((row % 2 != 0 && col % 2 == 0) || (row % 2 == 0 && col % 2 != 0));
    }
    
}