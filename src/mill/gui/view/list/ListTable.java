package mill.gui.view.list;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import mill.gui.view.ListFrame;

public class ListTable extends JTable {

    private final ListFrame FRAME;
    private int selectedRowIndex = -1;
    
    public ListTable(ListFrame frame) {
        FRAME = frame;
        setFillsViewportHeight(true);
        setColumnSelectionAllowed(false);
        setColumnHeaderTextToCenter();
        initSelectionModel();
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    private void setColumnHeaderTextToCenter() {
        ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void initSelectionModel() {
        ListSelectionModel model = getSelectionModel();
        model.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedRowIndex = getRealSelectedRow();
                FRAME.listTableRowSelected();
            }
            
            private int getRealSelectedRow() {
                int selectedRow = getSelectedRow();
                return selectedRow == -1 ? -1 : getRowSorter().convertRowIndexToModel(selectedRow);
            }
            
        });
    }

    public void setRowFilter(String filter) {
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) getRowSorter();
        if (filter.length() == 0) {
            sorter.setRowFilter(null);
        }
        else {
            ArrayList<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
            filters.add(RowFilter.regexFilter(filter, 0));
            filters.add(RowFilter.regexFilter(filter, 1));
            RowFilter<Object,Object> rowFilter = RowFilter.orFilter(filters);
            sorter.setRowFilter(rowFilter);
        }
    }
    
    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(getModel());
        setRowSorter(sorter);
    }
    
}