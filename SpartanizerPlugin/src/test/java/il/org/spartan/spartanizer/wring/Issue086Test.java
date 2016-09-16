package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue086Test {
  ThrowAndStatement t;
  Statement s;

  @Before public void init() {
    t = new ThrowAndStatement();
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

  @Test public void findFirstThrow() {
    azzert.that(findFirst.throwStatement(s), instanceOf(ThrowStatement.class));
  }

  @Ignore @Test public void vanillaThrow() {
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