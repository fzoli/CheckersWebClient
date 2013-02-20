package mill.gui.controll.list;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mill.http.entity.Play;

public class ListTableModel extends AbstractTableModel {
    
    private final String[] COL_NAMES = new String[] {"Név", "Tulajdonos", "Jelszó", "Belépett", "Státusz"};
    private int count = 0;
    List<Play> playes;
    
    public void updateList(List<Play> list) {
        playes = list;
        count = playes.size();
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (count == 0) return Object.class;
        return getValueAt(0, columnIndex).getClass();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public int getRowCount() {
        return count;
    }
    
    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Play p = playes.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getName();
            case 1: return p.getOwner();
            case 2: return p.isProtected();
            case 3: return p.getPlayerNumber();
            case 4: return p.getState();
            default: return "";
        }
    }
    
}