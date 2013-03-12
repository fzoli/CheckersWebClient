package checkers.gui.view.autocomplete;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class AutoCompleteModel extends AbstractListModel implements ComboBoxModel {

    String selected;
    final String delimiter = ";;;";
    final int limit = 200;
    
    class Data {

        private List<String> list = new ArrayList<String>(limit);
        private List<String> lowercase = new ArrayList<String>(limit);
        private List<String> filtered;

        void add(String s) {
            list.add(s);
            lowercase.add(s.toLowerCase());
        }

        void addToTop(String s) {
            list.add(0, s);
            lowercase.add(0, s.toLowerCase());
        }

        void remove(int index) {
            list.remove(index);
            lowercase.remove(index);
        }

        List<String> getList() {
            return list;
        }

        List<String> getFiltered() {
            if (filtered == null) {
                filtered = list;
            }
            return filtered;
        }

        int size() {
            return list.size();
        }

        void setPattern(String pattern) {
            if (pattern == null || pattern.isEmpty()) {
                if (filtered == null) {
                    setSelectedItem(null);
                }
                filtered = list;
            } else {
                filtered = new ArrayList<String>(limit);
                pattern = pattern.toLowerCase();
                for (int i = 0; i < lowercase.size(); i++) {
                    String actual = lowercase.get(i);
                    try {
                        actual = actual.substring(0, pattern.length());
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    if (actual.equals(pattern)) {
                        filtered.add(list.get(i));
                    }
                }
                setSelectedItem(pattern);
            }
        }

        boolean contains(String s) {
            if (s == null || s.trim().isEmpty()) {
                return true;
            }
            s = s.toLowerCase();
            for (String item : lowercase) {
                if (item.equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    Data data = new Data();

    public void readData(String[] values) {

        for (String v : values) {
            data.add(v);
        }

    }

    void writeData() {
        StringBuilder b = new StringBuilder(limit * 60);

        for (String url : data.getList()) {
            b.append(delimiter).append(url);
        }
        b.delete(0, delimiter.length());

    }

    public void setPattern(String pattern) {

        int size1 = getSize();

        data.setPattern(pattern);

        int size2 = getSize();

        if (size1 < size2) {
            fireIntervalAdded(this, size1, size2 - 1);
            fireContentsChanged(this, 0, size1 - 1);
        } else if (size1 > size2) {
            fireIntervalRemoved(this, size2, size1 - 1);
            fireContentsChanged(this, 0, size2 - 1);
        }
    }

    public void addToTop(String aString) {
        if (aString == null || data.contains(aString)) {
            return;
        }
        if (data.size() == 0) {
            data.add(aString);
        } else {
            data.addToTop(aString);
        }

        while (data.size() > limit) {
            int index = data.size() - 1;
            data.remove(index);
        }

        setPattern(null);
        setSelectedItem(aString);

        if (data.size() > 0) {
            writeData();
        }
    }

    public void clear() {
        data.getFiltered().clear();
        data.getList().clear();
        data.lowercase.clear();
        setPattern(null);
        setSelectedItem(null);
        writeData();
    }
    
    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if ((selected != null && !selected.equals(anObject))
                || selected == null && anObject != null) {
            selected = (String) anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public int getSize() {
        return data.getFiltered().size();
    }

    @Override
    public Object getElementAt(int index) {
        return data.getFiltered().get(index);
    }
    
}