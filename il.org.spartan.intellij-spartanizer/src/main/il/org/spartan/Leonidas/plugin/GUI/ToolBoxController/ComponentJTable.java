package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * @author Amir Sagiv
 * @since 14/05/2017
 */
public class ComponentJTable extends JTable {

    public ComponentJTable() {

        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }


        };
        this.setModel(model);
        this.getTableHeader().setResizingAllowed(false);
        this.getTableHeader().setReorderingAllowed(false);
        model.setColumnCount(2);
        Object[] headers = {"ID", "Value"};
        model.setColumnIdentifiers(headers);
        this.setRowHeight(30);
        for (int i = 0; i < 2; i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
            this.getColumnModel().getColumn(i).setMinWidth(150);
            this.getColumnModel().getColumn(i).setCellEditor(new CellEditor());
        }
    }

    private class CellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        /*
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof JCheckBox) {

                JCheckBox checkbox = (JCheckBox) value;
                checkbox.setBackground(getBackground());
                checkbox.setForeground(getForeground());
                checkbox.setEnabled(isEnabled());
                checkbox.setFont(getFont());
                checkbox.setFocusPainted(false);
                checkbox.setBorderPainted(true);
                checkbox.setBorder(isSelected ?
                        UIManager.getBorder(
                                "List.focusCellHighlightBorder") : noFocusBorder);
                return checkbox;
            }
            if (value instanceof JTextField) {
                JTextField text = (JTextField) value;
                text.setBackground(getBackground());
                text.setForeground(getForeground());
                text.setEnabled(isEnabled());
                text.setFont(getFont());
                return text;
            }

            if (value instanceof JList) {
                JList list = (JList) value;
                list.setBackground(getBackground());
                list.setForeground(getForeground());
                list.setEnabled(isEnabled());
                list.setFont(getFont());
                return list;
            }

            if (value instanceof JComboBox) {
                JComboBox box = (JComboBox) value;
                box.setBackground(getBackground());
                box.setForeground(getForeground());
                box.setEnabled(isEnabled());
                box.setFont(getFont());
                box.setBorder(isSelected ?
                        UIManager.getBorder(
                                "List.focusCellHighlightBorder") : noFocusBorder);
                return box;
            }


            if (value instanceof JLabel) {
                JLabel text = (JLabel) value;
                text.setBackground(getBackground());
                text.setForeground(getForeground());
                text.setEnabled(isEnabled());
                text.setFont(getFont());
                return text;
            }
            return this;
        }
    }


    class CellEditor extends AbstractCellEditor implements TableCellEditor {

        Component c;

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int rowIndex, int vColIndex) {

            if (value instanceof JCheckBox) {

                JCheckBox checkbox = (JCheckBox) value;
                c = checkbox;
                return checkbox;
            }

            if (value instanceof JTextField) {

                JTextField tf = (JTextField) value;
                c = tf;
                return tf;
            }

            if (value instanceof JList) {

                JList l = (JList) value;
                c = l;
                return l;
            }

            if (value instanceof JComboBox) {

                JComboBox b = (JComboBox) value;
                c = b;
                return b;
            }

            return null;
        }

        public Object getCellEditorValue() {
            return this.c;
        }
    }

}






