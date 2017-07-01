package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Creates a JFrame with a table like structure. each row presents the i-th component
 * from the supplied list with 2 columns: the left contains the component name (component.getName), and
 * the right contains the component itself.
 *
 * @author RoeiRaz
 * @since 6/24/17
 */
public class GridBagTable extends JPanel {
    /**
     * @param leftLabel  the left column header
     * @param rightLabel the right column header
     * @param components components
     */
    public GridBagTable(String leftLabel, String rightLabel, java.util.List<? extends JComponent> components) {
        setLayout(new GridBagLayout());

        // add table headers
        add(makeHeaderLabel(leftLabel), makeHeaderConstraints(0));
        add(makeHeaderLabel(rightLabel), makeHeaderConstraints(1));

        // add tippers
        IntStream.range(0, components.size()).forEach(i -> {
            add(new JLabel(components.get(i).getName()), makeContentConstraints(0, 2 * i + 2, 50));
            add(components.get(i), makeContentConstraints(1, 2 * i + 2, 50));
            add(new JSeparator(JSeparator.HORIZONTAL), makeSeparatorConstraints(2 * i + 1));
        });
    }

    /**
     * Example for teammates. TODO remove this
     */
    public static void runExample() {
        JFrame frame = new JFrame(GridBagTable.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setName("checkbox");

        JTextField textfield = new JTextField("iamgroot");
        textfield.setName("textfield");

        JTable table = new JTable(3, 2);
        table.setName("table");

        frame.add(new GridBagTable("Property", "Value", Arrays.asList(checkbox, textfield, table)));
        frame.pack();
        frame.setVisible(true);
    }

    private JLabel makeHeaderLabel(String label) {
        JLabel labelPropertyId = new JLabel(label);
        labelPropertyId.setHorizontalAlignment(JLabel.CENTER);
        labelPropertyId.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        return labelPropertyId;
    }

    private GridBagConstraints makeHeaderConstraints(int gridx) {
        final int headerGridY = 0;
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = headerGridY;
        gc.weightx = 50;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);
        return gc;
    }

    private GridBagConstraints makeContentConstraints(int gridx, int gridy, int weightx) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = gridy;
        gc.weightx = weightx;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);
        return gc;
    }

    private GridBagConstraints makeSeparatorConstraints(int gridy) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = gridy;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridwidth = 2;
        return gc;
    }
}

