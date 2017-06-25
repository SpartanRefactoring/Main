package il.org.spartan.Leonidas.plugin.GUI.ViewAvailableTippersInFile;


import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileEditorManager;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Amir Sagiv
 * @since 24/04/2017
 */
@SuppressWarnings("unchecked")
class TippersList extends JList {
    protected static Border noFocusBorder =
            new EmptyBorder(1, 1, 1, 1);
    private int numOfElements;

    public TippersList(JFrame frame) {
        numOfElements = 0;
        setCellRenderer(new CellRenderer());
        this.setFixedCellHeight(40);
        this.setFont(new Font("",0,20));
        addMouseListener(new MouseAdapter() {
                             public void mouseClicked(MouseEvent e) {
                                 int index = locationToIndex(e.getPoint());
                                 if (e.getClickCount() == 2) {
                                     // double click
                                     JLabel label = (JLabel) getModel().getElementAt(index);
                                     String tipperLine = label.getText().substring(label.getText().indexOf("Line")+5);
                                     EditorImpl editor = ((EditorImpl) FileEditorManager.getInstance(Utils.getProject()).getSelectedTextEditor());
                                     editor.getCaretModel().
                                             moveToOffset(editor.logicalPositionToOffset(new LogicalPosition(Integer.parseInt(tipperLine)-1,0)));

                                     Point caretLocation = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
                                     //int scrollOffset = caretLocation.y - myCaretUpdateVShift;
                                     editor.getScrollingModel().disableAnimation();
                                     editor.getScrollingModel().scrollVertically(
                                             editor.logicalPositionToOffset(new LogicalPosition(Integer.parseInt(tipperLine)-1,0)));
                                     editor.getScrollingModel().enableAnimation();
                                            // .moveToLogicalPosition(new LogicalPosition(Integer.parseInt(tipperLine),0));
                                     frame.setState(Frame.ICONIFIED);
                                 }
                             }
                         }
        );


        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public int getNumOfElements() {
        return numOfElements;
    }

    public void addTipper(JLabel label) {
        numOfElements++;
        ListModel currentList = this.getModel();
        JLabel[] newList = new JLabel[currentList.getSize() + 1];
        for (int i = 0; i < currentList.getSize(); i++) {
            newList[i] = (JLabel) currentList.getElementAt(i);
        }
        newList[newList.length - 1] = label;
        setListData(newList);
    }


    protected class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) value;
            label.setBackground(getBackground());
            label.setForeground(getForeground());
            label.setEnabled(isEnabled());
            label.setFont(getFont());
            label.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
            return label;
        }
    }

}