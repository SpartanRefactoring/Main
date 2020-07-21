package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** see Github issue thus numbered for more info unit tests for
 * {@link LocalInitializedArithmeticsInline}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-18 */
public class Issue0187 extends TipperTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> tipper() {
    return new LocalInitializedArithmeticsInline();
  }
  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }
  @Test public void a() {
    trimmingOf("int i = 1; i+=1;")//
        .gives("int i = 1 + 1;")//
        .stays()//
    ;
  }
  @Test public void b() {
    trimmingOf("int count = packed.charAt(i++), value = packed.charAt(i++);value|=1;")//
        .gives("int count = packed.charAt(i++), value = packed.charAt(i++)|1;")//
        .stays()//
    ;
  }
  @Test public void c() {
    trimmingOf("int x = 1, y = x; x*=y;")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("int x = 1; x&=12;")//
        .gives("int x = 1 & 12;");
  }
  @Test public void e() {
    trimmingOf("int $ = f(x);$^=55;return $;")//
        .gives("int $ = f(x) ^ 55;return $;")//
    ;
  }
  @Test public void f() {
    trimmingOf("int a = 3*2+7;a/=55;")//
        .gives("int a = (3*2+7)/55;")//
    ;
  }
  @Test public void h() {
    trimmingOf("int x = 3; x<<=2;")//
        .gives("int x = 3<<2;");
  }
  @Test public void i() {
    trimmingOf("int x = 0; x += x++;")//
        .stays();
  }
  @Test public void k() {
    trimmingOf("int x = 0, y = 1; x += y;")//
        .stays();
  }
}
