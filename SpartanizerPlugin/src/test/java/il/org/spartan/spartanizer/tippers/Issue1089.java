package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: Tomer Dragucki - which class does this test? Add a {@link}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-22 */
@SuppressWarnings("static-method")
public class Issue1089 {
  @Test public void a() {
    trimmingOf("ToggleGroup group = new ToggleGroup();" //
        + "(new SelectAnArea()).getAllPossibleColors().forEach(c -> { " //
        + "JFXRadioButton rbtn = new JFXRadioButton(c);" //
        + "rbtn.setToggleGroup(group);" + "});")
            .gives("ToggleGroup group = new ToggleGroup();" //
                + "(new SelectAnArea()).getAllPossibleColors().forEach(c -> {" //
                + "(new JFXRadioButton(c)).setToggleGroup(group);"//
                + "});");
  }
}