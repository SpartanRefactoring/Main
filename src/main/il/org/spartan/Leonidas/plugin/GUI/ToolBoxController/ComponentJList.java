package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Anna Belozovsky
 * @since 15/05/2017
 */
public class ComponentJList extends JList {
    protected static Border noFocusBorder =
            new EmptyBorder(1, 1, 1, 1);
    private int numOfElements;

    public ComponentJList() {
        numOfElements = 0;
        setCellRenderer(new CellRenderer());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                Object component = getModel().getElementAt(index);
                if (component instanceof JCheckBox) {
                    //if there is a checkbox
                    if (index != -1) {
                        JCheckBox checkbox = (JCheckBox) component;
                        checkbox.setSelected(
                                !checkbox.isSelected());
                        repaint();
                    }
                } else {
                    //if there is a textfield
                    if (index != -1) {
                        TextFieldComponent field = (TextFieldComponent) component;
                        field.pressed();
                    }
                }
            }
        });
    }

    public int getNumOfElements() {
        return numOfElements;
    }

    public void addComponent(Component component) {
        numOfElements++;
        ListModel currentList = this.getModel();
        Component[] newList = new Component[currentList.getSize() + 1];
        for (int i = 0; i < currentList.getSize(); i++) {
            if (currentList.getElementAt(i) instanceof JCheckBox)
                newList[i] = (JCheckBox) currentList.getElementAt(i);
            else
                newList[i] = (TextFieldComponent) currentList.getElementAt(i);
        }
        newList[newList.length - 1] = component;
        setListData(newList);
    }


    protected class CellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ((Component) value).setBackground(getBackground());
            ((Component) value).setForeground(getForeground());
            ((Component) value).setEnabled(isEnabled());
            ((Component) value).setFont(getFont());
            if (value instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox) value;
                checkbox.setFocusPainted(false);
                checkbox.setBorderPainted(true);
                checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
                return checkbox;
            } else {
                TextFieldComponent field = (TextFieldComponent) value;
                field.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
                return field;
            }
        }
    }


}