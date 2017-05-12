package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** see Github issue thus numbered for more info unit tests for
 * {@link IfFooElseIfBarElseFoo}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
@SuppressWarnings("static-method")
public class Issue1129 {
  @Test public void a() {
    trimmingOf("if(a) f(); else if(b) g(); else f();")//
        .gives("if(a || !b) f(); else if(b) g();")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("if(a) f(); else if (x()) g(); else f();")//
        .stays()//
    ;
  }

  @Test public void c() {
    trimmingOf("if (b1) if (b2) f1(); else if (b3) f2();" //
        + "else f1(); else g();")//
            .gives("if(!b1) g(); else if (b2) f1(); else if (b3) f2(); else f1();")//
            .gives("if (!b1) g(); else if (b2 || !b3) f1(); else if (b3) f2();")//
            .stays()//
    ;
  }

  @Test public void d() {
    trimmingOf("if (b1) if (b2) f1(); else if (b3) f2();" //
        + "else f1(); else if (a2) g1(); else if (a3) g2();" //
        + "else g1();")//
            .gives("if (b1) {if (b2 || !b3) f1(); else if (b3) f2();}" //
                + "else if (a2 || !a3) g1(); else if (a3) g2();")
            .stays()//
    ;
  }
}
