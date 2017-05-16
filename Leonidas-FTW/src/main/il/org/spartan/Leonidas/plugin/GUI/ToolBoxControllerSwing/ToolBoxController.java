package il.org.spartan.Leonidas.plugin.GUI.ToolBoxControllerSwing;




import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;


/**
 * @author Amir Sagiv
 * @since 24/04/2017
 */
public class ToolBoxController extends JFrame{
    private JPanel mainPanel;
    private JButton applyButton;
    private JButton clearAllButton;
    private JButton selectAllButton;
    private JSplitPane splitPanel;
    private JPanel rightPannel;
    private JPanel infoPanel;
    private JPanel buttonsPanel;
    private JScrollPane tippersPane;
    private JTextArea textArea1;

    private CheckBoxList list;


    public ToolBoxController() {
        super("Spartanizer ToolBox Controller");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        pack();
        setVisible(true);
        list = new CheckBoxList();
        List<Tipper> tipsList = Toolbox.getAllTippers();
        tipsList.forEach(tip -> {
            list.addCheckbox(new JCheckBox(tip.description()));
        });
        list.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());

                if (index > -1 && index < list.getNumOfElements()) {
                    JCheckBox checkbox = (JCheckBox)
                            list.getModel().getElementAt(index);
                    textArea1.setText(checkbox.getText());
                }
            }
        });
        textArea1.setEditable(false);
        tippersPane.setViewportView(list);
        selectAllButton.addActionListener(e->selectAllListener());
        clearAllButton.addActionListener(e -> clearAllListener());

    }

    private void clearAllListener() {
        list.setAllCheckBoxes(false);
    }

    private void selectAllListener() {
        list.setAllCheckBoxes(true);
    }




}


