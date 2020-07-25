package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Test;

import il.org.spartan.athenizer.zoomers.MethodInvocationTernaryBloater;

/** Unit tests for {@link MethodInvocationTernaryBloater}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0984 {
  @Test public void a() {
    bloatingOf("o.f(x ? a : b);")//
        .gives("if (x)" //
            + "  o.f(a);" //
            + "else " //
            + "  o.f(b);");
  }
  @Test public void b() {
    bloatingOf("o.f(p, x ? a : b);")//
        .gives("if (x)" //
            + "  o.f(p, a);" //
            + "else" //
            + "  o.f(p, b);");
  }
  @Test public void c() {
    bloatingOf("o.f(y ? a1 : b1, x ? a2 : b2);")
        .gives("if (y)" //
            + "  o.f(a1, x ? a2 : b2);" //
            + "else" //
            + "  o.f(b1, x ? a2 : b2);")
        .gives("if(y){o.f(a1,x?a2:b2);}else{o.f(b1,x?a2:b2);}")//
        .gives("if(y){if(x)o.f(a1,a2);else o.f(a1,b2);}else{if(x)o.f(b1,a2);else o.f(b1,b2);}") //
        .gives("if(y){if(x){o.f(a1,a2);}else{o.f(a1,b2);}}else{if(x) {o.f(b1,a2);} else {o.f(b1,b2);}}");
  }
  @Test public void d() {
    bloatingOf("o.f(y ? a1 : b1, pp, x ? a2 : b2);")
        .gives("if (y)" //
            + "  o.f(a1, pp, x ? a2 : b2);" //
            + "else" //
            + "  o.f(b1, pp, x ? a2 : b2);")
        .gives("if(y){o.f(a1,pp,x?a2:b2);}else{o.f(b1,pp,x?a2:b2);}")//
        .gives("if(y){if(x)o.f(a1,pp,a2);else o.f(a1,pp,b2);}else{if(x)o.f(b1,pp,a2);else o.f(b1,pp,b2);}");
  }
  @Test public void e() {
    bloatingOf("setSelection((¢ == null) || (¢.textSelection == null) ? null : ¢.textSelection);") //
        .stays();
  }
}
