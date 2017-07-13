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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sharon LK
 */
@SuppressWarnings({"FieldCanBeLocal", "Convert2MethodRef"})
public class AddTipperUI extends JFrame {
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
    private JPanel panel5;
    private JLabel label5;
    private JScrollPane scrollPane3;
    private RSyntaxTextArea constraintsTextArea;
    private JPanel panel3;
    private JButton buttonSave;
    private JButton buttonClose;
    private JPanel hSpacer1;
    private JLabel labelHelp;
    public AddTipperUI() {
        initComponents();

        setSize(600, 600);
        setTitle("Add New Tipper");
        setLocationRelativeTo(null);
        matcherTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        replacerTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        constraintsTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        if (UIUtil.isUnderDarcula())
			try {
				Theme theme = Theme.load(getClass().getResourceAsStream("/ui/dark.xml"));
				theme.apply(matcherTextArea);
				theme.apply(replacerTextArea);
                theme.apply(constraintsTextArea);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void buttonSaveActionPerformed(ActionEvent e) {
        if (CustomLeonidasTippers.getInstance().generate(nameTextField.getText(),
                descriptionTextField.getText(),
                matcherTextArea.getText(),
                replacerTextArea.getText(),
                constraintsTextArea.getText()))
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void buttonCloseActionPerformed(ActionEvent e) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void labelHelpMouseClicked(MouseEvent e) {
        if (Desktop.isDesktopSupported())
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/TechnionYP5777/Leonidas-FTW/wiki/Leonidas"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }

    private void initComponents() {
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
        panel5 = new JPanel();
        label5 = new JLabel();
        scrollPane3 = new JScrollPane();
        constraintsTextArea = new RSyntaxTextArea();
        panel3 = new JPanel();
        buttonSave = new JButton();
        buttonClose = new JButton();
        hSpacer1 = new JPanel(null);
        labelHelp = new JLabel();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
        panel4.setLayout(new GridBagLayout());
        ((GridBagLayout) panel4.getLayout()).columnWidths = new int[]{0, 0, 0};
        ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) panel4.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0E-4};
        ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};
        label3.setText("Name:");
        panel4.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 10, 10), 0, 0));
        panel4.add(nameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 10, 5), 0, 0));
        label4.setText("Description:");
        panel4.add(label4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 10), 0, 0));
        panel4.add(descriptionTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        contentPane.add(panel4, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(separator1, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel1.setLayout(new GridBagLayout());
        ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{0.0, 1.0E-4};
        ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};
        label1.setText("Matcher:");
        panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
        scrollPane1.setViewportView(matcherTextArea);
        panel1.add(scrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(panel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panel2.setLayout(new GridBagLayout());
        ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{0.0, 1.0E-4};
        ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};
        label2.setText("Replacer:");
        panel2.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
        scrollPane2.setViewportView(replacerTextArea);
        panel2.add(scrollPane2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(panel2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panel5.setLayout(new GridBagLayout());
        ((GridBagLayout) panel5.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) panel5.getLayout()).rowHeights = new int[]{0, 80, 0};
        ((GridBagLayout) panel5.getLayout()).columnWeights = new double[]{0.0, 1.0E-4};
        ((GridBagLayout) panel5.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};
        label5.setText("Constraints:");
        panel5.add(label5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
        scrollPane3.setViewportView(constraintsTextArea);
        panel5.add(scrollPane3, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(panel5, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panel3.setLayout(new GridBagLayout());
        ((GridBagLayout) panel3.getLayout()).columnWidths = new int[]{155, 155, 0, 0, 0};
        ((GridBagLayout) panel3.getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout) panel3.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};
        ((GridBagLayout) panel3.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};
        buttonSave.setText("Save");
        buttonSave.addActionListener(e -> buttonSaveActionPerformed(e));
        panel3.add(buttonSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 7), 0, 0));
        buttonClose.setText("Close");
        buttonClose.addActionListener(e -> buttonCloseActionPerformed(e));
        panel3.add(buttonClose, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 7), 0, 0));
        panel3.add(hSpacer1, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
        labelHelp.setText("help");
        labelHelp.setForeground(new Color(0, 204, 255));
        labelHelp.setFont(labelHelp.getFont().deriveFont(Font.BOLD | labelHelp.getFont().getStyle()));
        labelHelp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelHelpMouseClicked(e);
            }
        });
        panel3.add(labelHelp, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        contentPane.add(panel3, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
