/*
 * Created by JFormDesigner on Sat Jun 24 14:50:54 IDT 2017
 */

package il.org.spartan.Leonidas.plugin.GUI.AddTipper;

import com.intellij.util.ui.UIUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * @author Sharon LK
 */
public class AddTipperUI extends JFrame {
    public AddTipperUI() {
        initComponents();

        setSize(600, 800);
        matcherTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        replacerTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        if (UIUtil.isUnderDarcula()) {
            try {
                Theme theme = Theme.load(getClass().getResourceAsStream("/ui/dark.xml"));
                theme.apply(matcherTextArea);
                theme.apply(replacerTextArea);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buttonSaveActionPerformed(ActionEvent e) {
        CustomLeonidasTippers.getInstance().generate(nameTextField.getText(),
                descriptionTextField.getText(),
                matcherTextArea.getText(),
                replacerTextArea.getText());

        // Close window after we add the tipper
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void buttonCloseActionPerformed(ActionEvent e) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Sharon KL
        panel4 = new JPanel();
        label3 = new JLabel();
        nameTextField = new JTextField();
        label4 = new JLabel();
        descriptionTextField = new JTextField();
        separator1 = new JSeparator();
        panel1 = new JPanel();
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        matcherTextArea = new RSyntaxTextArea();
        panel2 = new JPanel();
        label2 = new JLabel();
        scrollPane2 = new JScrollPane();
        replacerTextArea = new RSyntaxTextArea();
        panel3 = new JPanel();
        buttonSave = new JButton();
        buttonClose = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0E-4};

        //======== panel4 ========
        {

            // JFormDesigner evaluation mark
            panel4.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), panel4.getBorder())); panel4.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            panel4.setLayout(new GridBagLayout());
            ((GridBagLayout)panel4.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0, 0, 0};
            ((GridBagLayout)panel4.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
            ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

            //---- label3 ----
            label3.setText("Name:");
            panel4.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 5, 10, 10), 0, 0));
            panel4.add(nameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 5, 10, 5), 0, 0));

            //---- label4 ----
            label4.setText("Description:");
            panel4.add(label4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 5, 5, 10), 0, 0));
            panel4.add(descriptionTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 5, 5, 5), 0, 0));
        }
        contentPane.add(panel4, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(separator1, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

            //---- label1 ----
            label1.setText("Matcher:");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(matcherTextArea);
            }
            panel1.add(scrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 0, 0));

        //======== panel2 ========
        {
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0, 0};
            ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
            ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

            //---- label2 ----
            label2.setText("Replacer:");
            panel2.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(replacerTextArea);
            }
            panel2.add(scrollPane2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 0, 0));

        //======== panel3 ========
        {
            panel3.setLayout(new GridBagLayout());
            ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {155, 150, 0};
            ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
            ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- buttonSave ----
            buttonSave.setText("Save");
            buttonSave.addActionListener(e -> buttonSaveActionPerformed(e));
            panel3.add(buttonSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 7), 0, 0));

            //---- buttonClose ----
            buttonClose.setText("Close");
            buttonClose.addActionListener(e -> buttonCloseActionPerformed(e));
            panel3.add(buttonClose, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        }
        contentPane.add(panel3, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Sharon KL
    private JPanel panel4;
    private JLabel label3;
    private JTextField nameTextField;
    private JLabel label4;
    private JTextField descriptionTextField;
    private JSeparator separator1;
    private JPanel panel1;
    private JLabel label1;
    private JScrollPane scrollPane1;
    private RSyntaxTextArea matcherTextArea;
    private JPanel panel2;
    private JLabel label2;
    private JScrollPane scrollPane2;
    private RSyntaxTextArea replacerTextArea;
    private JPanel panel3;
    private JButton buttonSave;
    private JButton buttonClose;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
