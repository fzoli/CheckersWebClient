package mill.gui.view.log;

import mill.gui.view.log.userobject.LogObject;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class LogTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        LogObject uo = (LogObject)((DefaultMutableTreeNode)value).getUserObject();
        setText(uo.getText());
        setIcon(uo.getIcon());
        setDisabledIcon(uo.getIcon());
        return this;
    }

}