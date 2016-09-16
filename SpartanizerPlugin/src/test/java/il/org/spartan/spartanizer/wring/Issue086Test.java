package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.engine.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue086Test {
  ThrowAndStatement t;
  Statement s;

  @Before public void init() {
    s = into.s("{"//
        + " if (false) "//
        + "   i++; "//
        + " else { "//
        + "   g(i); "//
        + "   throw new RuntimeException(); "//
        + " } "//
        + " f();" //
        + " a = 3;" //
        + " return 2;" + "}");//
  }

  @Before public void init1() {
    t = new ThrowAndStatement();
  }

  @Test public void vanillaThrow() {
    trimming("int f() {"//
        + " if (false) "//
        + "   i++; "//
        + " else { "//
        + "   g(i); "//
        + "   throw new RuntimeException(); "//
        + " } "//
        + " f();" //
        + " a = 3;" //
        + " return 2;" + "}"//
    )//
        .to("int f(){{g(i);throw new RuntimeException();}f();a=3;return 2;}") //
        .to("int f(){g(i);throw new RuntimeException();f();a=3;return 2;}") //
        .to("int f(){g(i);throw new RuntimeException();a=3;return 2;}") //
        .to("int f(){g(i);throw new RuntimeException();}") //
        .stays();
    ;
  }
}