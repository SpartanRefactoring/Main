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
//                super.mousePressed(e);
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
                        repaint();
                    }
                }
            }
        });
    }


    protected class CellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return null;
        }
    }


}