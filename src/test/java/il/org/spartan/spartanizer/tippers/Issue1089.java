package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Testing disabling lambda expression tipping
 * {@link FragmentInitializerStatementTerminatingScope}
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

  @Test public void b() {
    trimmingOf("Object o = new Object();" + "l.forEach(λ -> a(o));").stays();
  }

  @Test public void c() {
    trimmingOf("" //
        + " final Int n = new Int();" //
        + "        $ = Arrays.asList(u.getProblems()).stream().map(λ -> ++n.inner).reduce((x, y) -> x + y).get();")//
            .stays();
  }
}