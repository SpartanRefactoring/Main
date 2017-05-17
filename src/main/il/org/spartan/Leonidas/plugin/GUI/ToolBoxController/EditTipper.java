package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import il.org.spartan.Leonidas.plugin.leonidas.LeonidasUtils;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import org.reflections.Reflections;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Amir Sagiv, Anna Belozovsky
 * @since 14-05-2017
 */
public class EditTipper extends JFrame {
    private JPanel mainPanel;
    private JButton button1;
    private JButton button2;
    private JPanel tempPane;
    private JScrollPane TablePanel;
    private LeonidasUtils tipperAnnotation;

    public EditTipper(String tipperName) {
        super("Edit Tipper");
        (new Reflections(LeonidasTipperDefinition.class)).getSubTypesOf(LeonidasTipperDefinition.class)
                .forEach(c -> {
//                    JOptionPane.showMessageDialog(this, c.getSimpleName() + ", " + tipperName);
                    if (c.getSimpleName().equals(tipperName)) {
                        Annotation[] annotations = c.getAnnotations();
                        for (Annotation annotation : annotations) {

//                            System.err.println(annotation.annotationType().getSimpleName());
                            if (annotation.annotationType().getSimpleName().equals("LeonidasUtils")) {
                                tipperAnnotation = (LeonidasUtils) annotation;
                            }
                        }
                    }
                });

        int i = 0;
        ComponentJTable table = new ComponentJTable();
        Method[] methods = tipperAnnotation.getClass().getDeclaredMethods();
        ((DefaultTableModel) table.getModel()).setRowCount(methods.length);
        for (Method method : methods) {
            if (method.getName().equals("toString")) {
                continue;
            }
            Class returnType = method.getReturnType();
            try {
                if (returnType.isPrimitive() && returnType.getName().equals("boolean")) {
                    table.getModel().setValueAt(new JLabel(method.getName()), i, 0);
                    table.getModel().setValueAt(new JCheckBox("", (Boolean) method.invoke(tipperAnnotation)), i++, 1);
                    continue;
                }
                Object obj = returnType.newInstance();
                if (obj instanceof String) {

                    table.getModel().setValueAt(new JLabel(method.getName()), i, 0);
                    table.getModel().setValueAt(new JTextField((String) method.invoke(tipperAnnotation)), i++, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //model.setRowCount(4);
        //model.setColumnCount(2);

        //((DefaultTableModel)table.getModel()).addRow();

//        table.getModel().setValueAt(new JLabel("CheckBox"), 0, 0);
//        table.getModel().setValueAt(new JCheckBox(), 0, 1);
//        table.getModel().setValueAt(new JLabel("Text"), 1, 0);
//        table.getModel().setValueAt(new JTextField(), 1, 1);
        //JList<String> table = new JList<>();
        TablePanel.setViewportView(table);
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        pack();
        setVisible(true);
    }
}
