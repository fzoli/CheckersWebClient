package checkers.gui.view.autocomplete;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutoCompleteCombo extends JComboBox{

    private ComboBoxModel model;
    private final JTextComponent textComponent = (JTextComponent) getEditor().getEditorComponent();
    private boolean modelFilling = false;

    private boolean updatePopup;

    public AutoCompleteCombo() {
        setEditable(true);
        setPattern(null);
        updatePopup = false;
        
        textComponent.setDocument(new AutoCompleteDocument());
        setModel(new AutoCompleteModel());
        setSelectedItem(null);
        
        textComponent.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (updatePopup && isDisplayable()) {
                    setPopupVisible(false);
                    if (model.getSize() > 0) {
                        setPopupVisible(true);
                    }
                    updatePopup = false;
                }
            }
            
        });
    }

    @Override
    public void setModel(ComboBoxModel aModel) {
        this.model = aModel;
        super.setModel(aModel);
    }

      private class AutoCompleteDocument extends PlainDocument {

        boolean arrowKeyPressed = false;

        public AutoCompleteDocument() {
            textComponent.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_UP ||
                               key == KeyEvent.VK_DOWN) {
                        arrowKeyPressed = true;
                    }
                }
                
            });
        }

        void updateModel() throws BadLocationException {
            String textToMatch = getText(0, getLength());
            
            setPattern(textToMatch);
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {

            if (modelFilling) {
                return;
            }

            super.remove(offs, len);
            if (arrowKeyPressed) {
                arrowKeyPressed = false;
            } else {
                updateModel();
            }
            clearSelection();
        }
        
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

            if (modelFilling) {
                return;
            }

            super.insertString(offs, str, a);

            String text = getText(0, getLength());
            if (arrowKeyPressed) {
                model.setSelectedItem(text);
                arrowKeyPressed = false;
            } else {
                Object o = getSelectedItem();
                if (o == null) updateModel();
                else if(!text.equalsIgnoreCase(o.toString())) {
                    updateModel();
                }
            }

            clearSelection();
        }

    }


    public void setText(String text) {
        if (((AutoCompleteModel)model).data.contains(text)) {
            setSelectedItem(text);
        } else {
            addToTop(text);
            setSelectedIndex(0);
        }
    }

    public String getText() {
        return getEditor().getItem().toString();
    }

    private String previousPattern = null;

    private void setPattern(String pattern) {

        if(pattern!=null && pattern.trim().isEmpty())
            pattern = null;

        if(previousPattern==null && pattern ==null ||
           pattern!=null && pattern.equals(previousPattern)) {
            return;
        }

        previousPattern = pattern;

        modelFilling = true;

        ((AutoCompleteModel)model).setPattern(pattern);

        modelFilling = false;
        if(pattern != null)
            updatePopup = true;
    }


    private void clearSelection() {
        int i = getText().length();
        textComponent.setSelectionStart(i);
        textComponent.setSelectionEnd(i);
    }

    public synchronized void addToTop(String aString) {
        ((AutoCompleteModel)model).addToTop(aString);
    }

}