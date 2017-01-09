package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit tests for {@link MethodInvocationTernaryExpander} Issue #984
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0984 {
  @Test public void a() {
    zoomingInto("o.f(x ? a : b);").gives("if (x)" //
        + "  o.f(a);" //
        + "else " //
        + "  o.f(b);");
  }

  @Test public void b() {
    zoomingInto("o.f(p, x ? a : b);").gives("if (x)" //
        + "  o.f(p, a);" //
        + "else" //
        + "  o.f(p, b);");
  }

  @Test public void c() {
    zoomingInto("o.f(y ? a1 : b1, x ? a2 : b2);")
        .gives("if (y)" //
            + "  o.f(a1, x ? a2 : b2);" //
            + "else" //
            + "  o.f(b1, x ? a2 : b2);")
        .gives("if(y){o.f(a1,x?a2:b2);}else{o.f(b1,x?a2:b2);}").gives("if(y){if(x)o.f(a1,a2);else o.f(a1,b2);}else{o.f(b1,x?a2:b2);}")//
        .gives("if(y){if(x){o.f(a1,a2);}else{o.f(a1,b2);}}else{o.f(b1,x?a2:b2);}")
        .gives("if(y){if(x){o.f(a1,a2);}else{o.f(a1,b2);}}else{if(x)o.f(b1,a2);else o.f(b1,b2);}");
  }

  @Test public void d() {
    zoomingInto("o.f(y ? a1 : b1, pp, x ? a2 : b2);")
        .gives("if (y)" //
            + "  o.f(a1, pp, x ? a2 : b2);" //
            + "else" //
            + "  o.f(b1, pp, x ? a2 : b2);")
        .gives("if(y){o.f(a1,pp,x?a2:b2);}else{o.f(b1,pp,x?a2:b2);}").gives("if(y){if(x)o.f(a1,pp,a2);else o.f(a1,pp,b2);}else{o.f(b1,pp,x?a2:b2);}");
  }
}
