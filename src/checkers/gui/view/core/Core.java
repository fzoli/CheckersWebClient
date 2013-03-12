package checkers.gui.view.core;

import com.thebuzzmedia.imgscalr.Scalr;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Core {
    
    public static Icon getLogIcon(String file) {
        return getIcon("logicons/" + file);
    }
    
    public static BufferedImage getCheckersImage() {
        return getImage("checkers-icon.png");
    }
    
    public static Icon getDropDownIcon(String file) {
        return getIcon("dropdownicons/" + file);
    }
    
    public static boolean showConfirmDialog(Component component, String message) {
        Object[] options = {"Igen", "Nem"};
        int n = JOptionPane.showOptionDialog(
                    component,
                    message,
                    "Megerősítés",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
        return n == 0;
    }
    
    public static Border createToolbarBorder() {
        Border b1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border b2 = BorderFactory.createEmptyBorder(-2, -2, 0, -2);
        return BorderFactory.createCompoundBorder(b2, b1);
    }
    
    public static JButton createToolbarButton() {
        return createToolbarButton(null, null);
    }
    
    public static JButton createToolbarButton(String icon, String tooltipText) {
        JButton bt = new JButton();
        if (icon != null) bt.setIcon(getToolbarIcon(icon));
        if (tooltipText != null) bt.setToolTipText(tooltipText);
        bt.setFocusable(false);
        bt.setOpaque(false);
        return bt;
    }
    
    public static Icon getMenuIcon(String filename) {
        return getIcon(Scalr.resize(getToolbarImage(filename), Scalr.Method.QUALITY, 16, 16, Scalr.OP_ANTIALIAS));
    }
    
    public static Icon getToolbarIcon(String filename) {
        return getIcon(getToolbarImage(filename));
    }
    
    private static BufferedImage getToolbarImage(String filename) {
        return getImage("toolbaricons/" + filename);
    }
    
    public static BufferedImage getBusyImage(String filename) {
        return getImage("busyicons/" + filename);
    }
    
    private static Icon getIcon(String name) {
        return getIcon(getImage(name));
    }
    
    private static Icon getIcon(Image img) {
        return new ImageIcon(img);
    }
    
    private static BufferedImage getImage(String path) {
        try {
            return ImageIO.read(Core.class.getResource(path));
        }
        catch (Exception ex) {
            return new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
        }
    }
    
    public static void setLAF() {
        try {
            if (isOs("linux"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            else if(isOs("windows"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            else if (isOs("mac"))
                UIManager.setLookAndFeel("javax.swing.plaf.mac.MacLookAndFeel");
            else UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {}
    }
    
    public static boolean isOs(String os) {
        String osName = System.getProperty("os.name");
        os = os.toLowerCase();
        osName = osName.toLowerCase();
        return osName.lastIndexOf(os) > -1;
    }
    
}