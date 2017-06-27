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
            static final long serialVersionUID = 1;

            @Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
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
        model.setColumnIdentifiers(new Object[] { "ID", "Value" });
        this.setRowHeight(30);
        for (int ¢ = 0; ¢ < 2; ++¢) {
            this.getColumnModel().getColumn(¢).setCellRenderer(new CellRenderer());
            this.getColumnModel().getColumn(¢).setMinWidth(150);
            this.getColumnModel().getColumn(¢).setCellEditor(new CellEditor());
        }
    }

    private class CellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1;

        /*
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable t, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof JCheckBox) {

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
                box.setBorder(!isSelected ? noFocusBorder : UIManager.getBorder("List.focusCellHighlightBorder"));
                return box;
            }


            if (!(value instanceof JLabel))
				return this;
			JLabel text = (JLabel) value;
			text.setBackground(getBackground());
			text.setForeground(getForeground());
			text.setEnabled(isEnabled());
			text.setFont(getFont());
			return text;
        }
    }


    class CellEditor extends AbstractCellEditor implements TableCellEditor {

        Component c;

        public Component getTableCellEditorComponent(JTable t, Object value, boolean isSelected,
                                                     int rowIndex, int vColIndex) {

            if (value instanceof JCheckBox) {

                JCheckBox $ = (JCheckBox) value;
                c = $;
                return $;
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

            if (!(value instanceof JComboBox))
				return null;
			JComboBox b = (JComboBox) value;
			c = b;
			return b;
        }

        public Object getCellEditorValue() {
            return this.c;
        }
    }

}






