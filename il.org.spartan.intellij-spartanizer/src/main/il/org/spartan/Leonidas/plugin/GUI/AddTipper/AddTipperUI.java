/*
 * Created by JFormDesigner on Sat Jun 24 14:50:54 IDT 2017
 */

package il.org.spartan.Leonidas.plugin.GUI.AddTipper;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Sharon LK
 */
public class AddTipperUI extends JFrame {
    public AddTipperUI() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Sharon KL
        matcherLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        matcherTextPane = new JTextPane();
        replacerLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        replacerTextPane = new JTextPane();
        saveButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //---- matcherLabel ----
        matcherLabel.setText("Matcher:");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(matcherTextPane);
        }

        //---- replacerLabel ----
        replacerLabel.setText("Replacer:");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(replacerTextPane);
        }

        //---- saveButton ----
        saveButton.setText("Save");

        //---- cancelButton ----
        cancelButton.setText("Cancel");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                        .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(matcherLabel)
                                .addComponent(replacerLabel)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 235, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(matcherLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(replacerLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton)
                        .addComponent(saveButton))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Sharon KL
    private JLabel matcherLabel;
    private JScrollPane scrollPane1;
    private JTextPane matcherTextPane;
    private JLabel replacerLabel;
    private JScrollPane scrollPane2;
    private JTextPane replacerTextPane;
    private JButton saveButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
