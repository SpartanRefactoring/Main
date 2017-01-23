package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for centification of a single parameter to a function even if it
 * defines a {@link wizard.return¢} variable
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0222 {
  @Test public void chocolate1() {
    trimmingOf("static List<E> o(final I x) { if (x == null) return null; int y = x; ++y;\n"
        + " final List<E> $ = new ArrayList<>(); $.add(left(x)); $.add(right(x)); if (x.hasExtendedOperands())\n"
        + " $.addAll(step.eo(x)); return $;}\n")//
            .stays();
  }

  @Test public void chocolate2() {
    trimmingOf("private boolean continue¢(final List<V> fs) {for (final V $ : fs){\n" + "int b = f($);f($,b);return g($,b,f());}return true;}")//
        .stays();
  }

  @Test public void chocolate3() {
    trimmingOf("int f(int i) {for (int b: fs)return 0;return 1;}").gives("int f(int __) {for (int b: fs)return 0;return 1;}")//
        .stays();
  }

  @Test public void vanilla() {
    trimmingOf("static List<E> o(final I x) { if (x == null) return null;\n"
        + " final List<E> $ = new ArrayList<>(); $.add(left(x)); $.add(right(x)); if (x.hasExtendedOperands())\n"
        + " $.addAll(step.eo(x)); return $;}\n")
            .gives(
                "static List<E> o(final I ¢) { if (¢ == null) return null;\n"
                    + " final List<E> $ = new ArrayList<>(); $.add(left(¢)); $.add(right(¢));\n"
                    + " if (¢.hasExtendedOperands()) $.addAll(step.eo(¢)); return $;}\n")
            .stays();
  }
}
