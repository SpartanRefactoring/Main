package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;


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
class CheckBoxList extends JList {
    protected static Border noFocusBorder =
            new EmptyBorder(1, 1, 1, 1);
    private int numOfElements;

    public CheckBoxList() {
        numOfElements = 0;
        setCellRenderer(new CellRenderer());
        addMouseListener(new MouseAdapter() {
                             public void mouseClicked(MouseEvent e) {
                                 int index = locationToIndex(e.getPoint());
                                 if (index != -1) {
                                     JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                                     checkbox.setSelected(!checkbox.isSelected());
                                     repaint();
                                 }
                                 if (e.getClickCount() == 2) {
                                     // double click
                                     new EditTipper(((JCheckBox) getModel().getElementAt(index)).getText());
                                 }
                             }
                         }
        );


        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public int getNumOfElements() {
        return numOfElements;
    }

    public void addCheckbox(JCheckBox checkBox) {
        ++numOfElements;
        ListModel currentList = this.getModel();
        JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
        for (int i = 0; i < currentList.getSize(); ++i) {
            newList[i] = (JCheckBox) currentList.getElementAt(i);
        }
        newList[newList.length - 1] = checkBox;
        setListData(newList);
    }

    public void setAllCheckBoxes(boolean flag) {
        ListModel currentList = this.getModel();
        JCheckBox[] newList = new JCheckBox[currentList.getSize()];
        for (int i = 0; i < currentList.getSize(); ++i) {
            newList[i] = (JCheckBox) currentList.getElementAt(i);
            newList[i].setSelected(flag);
        }
        setListData(newList);
    }

    protected class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(getBackground());
            checkbox.setForeground(getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(!isSelected ? noFocusBorder : UIManager.getBorder("List.focusCellHighlightBorder"));
            return checkbox;
        }
    }

}