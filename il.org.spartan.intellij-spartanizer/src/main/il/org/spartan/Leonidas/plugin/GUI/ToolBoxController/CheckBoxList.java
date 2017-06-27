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
                                 if (e.getClickCount() == 2)
									new EditTipper(((JCheckBox) getModel().getElementAt(index)).getText());
                             }
                         }
        );


        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public int getNumOfElements() {
        return numOfElements;
    }

    public void addCheckbox(JCheckBox b) {
        ++numOfElements;
        ListModel currentList = this.getModel();
        JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
        for (int ¢ = 0; ¢ < currentList.getSize(); ++¢)
			newList[¢] = (JCheckBox) currentList.getElementAt(¢);
        newList[newList.length - 1] = b;
        setListData(newList);
    }

    public void setAllCheckBoxes(boolean flag) {
        ListModel currentList = this.getModel();
        JCheckBox[] newList = new JCheckBox[currentList.getSize()];
        for (int ¢ = 0; ¢ < currentList.getSize(); ++¢) {
            newList[¢] = (JCheckBox) currentList.getElementAt(¢);
            newList[¢].setSelected(flag);
        }
        setListData(newList);
    }

    protected class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox $ = (JCheckBox) value;
            $.setBackground(getBackground());
            $.setForeground(getForeground());
            $.setEnabled(isEnabled());
            $.setFont(getFont());
            $.setFocusPainted(false);
            $.setBorderPainted(true);
            $.setBorder(!isSelected ? noFocusBorder : UIManager.getBorder("List.focusCellHighlightBorder"));
            return $;
        }
    }

}