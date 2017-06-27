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
        IntStream.range(0, components.size()).forEach(λ -> {
            add(new JLabel(components.get(λ).getName()), makeContentConstraints(0, 2 * λ + 2, 50));
            add(components.get(λ), makeContentConstraints(1, 2 * λ + 2, 50));
            add(new JSeparator(JSeparator.HORIZONTAL), makeSeparatorConstraints(2 * λ + 1));
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
        JLabel $ = new JLabel(label);
        $.setHorizontalAlignment(JLabel.CENTER);
        $.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        return $;
    }

    private GridBagConstraints makeHeaderConstraints(int gridx) {
        final int headerGridY = 0;
        GridBagConstraints $ = new GridBagConstraints();
        $.gridx = gridx;
        $.gridy = headerGridY;
        $.weightx = 50;
        $.fill = GridBagConstraints.HORIZONTAL;
        $.insets = new Insets(5, 5, 5, 5);
        return $;
    }

    private GridBagConstraints makeContentConstraints(int gridx, int gridy, int weightx) {
        GridBagConstraints $ = new GridBagConstraints();
        $.gridx = gridx;
        $.gridy = gridy;
        $.weightx = weightx;
        $.anchor = GridBagConstraints.FIRST_LINE_START;
        $.fill = GridBagConstraints.HORIZONTAL;
        $.insets = new Insets(5, 5, 5, 5);
        return $;
    }

    private GridBagConstraints makeSeparatorConstraints(int gridy) {
        GridBagConstraints $ = new GridBagConstraints();
        $.gridx = 0;
        $.gridy = gridy;
        $.weightx = 1;
        $.fill = GridBagConstraints.HORIZONTAL;
        $.anchor = GridBagConstraints.FIRST_LINE_START;
        $.gridwidth = 2;
        return $;
    }
}

