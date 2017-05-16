package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Amir on 14-05-2017.
 */
public class EditTipper extends JFrame{
    private JPanel mainPanel;
    private JButton button1;
    private JButton button2;
    private JPanel tempPane;
    private JScrollPane TablePanel;


    public EditTipper() {
        super("Edit Tipper");

        //model.setRowCount(4);
        //model.setColumnCount(2);
        ComponentJTable table = new ComponentJTable();
        //((DefaultTableModel)table.getModel()).addRow();
        ((DefaultTableModel)table.getModel()).setRowCount(2);
        table.getModel().setValueAt(new JLabel("CheckBox"),0,0);
        table.getModel().setValueAt(new JCheckBox(),0,1);
        table.getModel().setValueAt(new JLabel("Text"),1,0);
        table.getModel().setValueAt(new JTextField(),1,1);
        //JList<String> table = new JList<>();
        TablePanel.setViewportView(table);
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        pack();
        setVisible(true);
    }
}
