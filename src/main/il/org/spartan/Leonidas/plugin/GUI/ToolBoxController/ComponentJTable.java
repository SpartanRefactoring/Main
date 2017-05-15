package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * @author Amir Sagiv
 * @since 14/05/2017
 */
public class ComponentJTable extends JTable {

    public ComponentJTable(){
        String columnNames[] = new String[2];
        columnNames[0] = "1";
        columnNames[1] = "2";
        DefaultTableModel model = new DefaultTableModel(columnNames, 4){
            private static final long serialVersionUID = 1L;

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }


        };
        this.setModel(model);
        this.getTableHeader().setResizingAllowed(false);
        this.getTableHeader().setReorderingAllowed(false);
        model.setRowCount(4);
        model.setColumnCount(2);
        this.setRowHeight(20);
        for(int i = 0; i < 2; i++){
            this.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
            this.getColumnModel().getColumn(i).setMinWidth(150);
            this.getColumnModel().getColumn(i).setCellEditor(new CellEditor());
        }
        model.setValueAt(new JCheckBox("hi!"),0,1);
        model.setValueAt(new JCheckBox("hi!"),1,1);
        model.setValueAt(new JTextField("Bye"),2,1);
    }

    private class CellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        /*
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if( value instanceof JCheckBox){

                JCheckBox checkbox = (JCheckBox) value;
                checkbox.setBackground(isSelected ?
                        getSelectionBackground() : getBackground());
                checkbox.setForeground(isSelected ?
                        getSelectionForeground() : getForeground());
                checkbox.setEnabled(isEnabled());
                checkbox.setFont(getFont());
                checkbox.setFocusPainted(false);
                checkbox.setBorderPainted(true);
                checkbox.setBorder(isSelected ?
                        UIManager.getBorder(
                                "List.focusCellHighlightBorder") : noFocusBorder);
                return checkbox;
            }
            if( value instanceof JTextField){
                JTextField text = (JTextField)value;
                text.setBackground(isSelected ?
                        getSelectionBackground() : getBackground());
                text.setForeground(isSelected ?
                        getSelectionForeground() : getForeground());
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

            if( value instanceof JCheckBox){

                JCheckBox checkbox = (JCheckBox) value;
                c = checkbox;
                return checkbox;
            }
            return null;
        }

        public Object getCellEditorValue() {
            return this.c;
        }
    }

}






