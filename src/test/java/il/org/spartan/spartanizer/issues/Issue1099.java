package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests of
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1099 {
  @Test public void a() {
    trimmingOf("/**/" + //
        "    @A public void a() {" + //
        "      final B b = a + \"\", c = C.d(b), e = F.f(g, c);" + //
        "      h.i( j, c, k(l(e)));" + //
        "      final B m = C.n(e);" + //
        "      h.i( j, m, k(l(b)));" + //
        "      h.i(j, o.p(b), k(l(o.p(m))));" + //
        "    }"//
    ).stays();
  }

  @Test public void aPuVoaFiBbacCdbeDfgchijckleFiBmCnehijmklbhijopbklopm() {
    trimmingOf(
        "@A public void a(){final B b=a+\"\",c=C.d(b),e=D.f(g,c);h.i(j,c,k(l(e)));final B m=C.n(e);h.i(j,m,k(l(b)));h.i(j,o.p(b),k(l(o.p(m))));}") //
            .using(VariableDeclarationFragment.class, new FragmentInitializerStatementTerminatingScope()) //
            .gives(
                "@A public void a(){final B c=C.d(a+\"\"),e=D.f(g,c);h.i(j,c,k(l(e)));final B m=C.n(e);h.i(j,m,k(l(a+\"\")));h.i(j,o.p(a+\"\"),k(l(o.p(m))));}") //
            .using(VariableDeclarationFragment.class, new FragmentInitializerStatementTerminatingScope()) //
            .gives(
                "@A public void a(){final B e=D.f(g,C.d(a+\"\"));h.i(j,C.d(a+\"\"),k(l(e)));final B m=C.n(e);h.i(j,m,k(l(a+\"\")));h.i(j,o.p(a+\"\"),k(l(o.p(m))));}") //
            .using(VariableDeclarationFragment.class, new FragmentInitializerStatementTerminatingScope()) //
            .gives(
                "@A public void a(){h.i(j,C.d(a+\"\"),k(l(D.f(g,C.d(a+\"\")))));final B m=C.n(D.f(g,C.d(a+\"\")));h.i(j,m,k(l(a+\"\")));h.i(j,o.p(a+\"\"),k(l(o.p(m))));}") //
            .using(VariableDeclarationFragment.class, new FragmentInitializerStatementTerminatingScope()) //
            .gives(
                "@A public void a(){h.i(j,C.d(a+\"\"),k(l(D.f(g,C.d(a+\"\")))));h.i(j,C.n(D.f(g,C.d(a+\"\"))),k(l(a+\"\")));h.i(j,o.p(a+\"\"),k(l(o.p(C.n(D.f(g,C.d(a+\"\")))))));}") //
            .stays() //
    ;
  }

  @Test public void b() {
    trimmingOf("/**/" + //
        "  " + //
        "    final D c = b.d(), e = f.g(e(b)), h = f.g(h(b));" + //
        "    return e == null || h == null ? null : i.j(i.k(e, h).l(c)).m();" + //
        "  "//
    )//
        .gives("/**/" + //
            "  " + //
            "    final D e = f.g(e(b)), h = f.g(h(b));" + //
            "    return e == null || h == null ? null : i.j(i.k(e, h).l(b.d())).m();" + //
            "  "//
        )//
        .stays();
  }

  @Test public void c() {
    trimmingOf("/**/" + //
        "  " + //
        "    final D c = d(), e = f(c,c), h = g(e, c);" + //
        "    return c + e + h; " + //
        "  "//
    )//
        .gives("/**/" + //
            "  " + //
            "    final D c = d(), e = f(c,c);" + //
            "    return c + e + g(e,c); " + //
            "  "//
        )//
        .stays();
  }
}
