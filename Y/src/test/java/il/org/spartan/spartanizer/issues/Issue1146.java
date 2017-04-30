package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** see Github issue thus numbered for more info unit tests for
 * {@link LocalInitializedIncrementDecrementInline}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
@SuppressWarnings("static-method")
public class Issue1146 extends TipperTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> tipper() {
    return new LocalInitializedIncrementDecrementInline();
  }

  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }

  @Test public void a() {
    trimmingOf("int i = 1; ++i;")//
        .gives("int i = 1 + 1;")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("int count = packed.charAt(i++), value = packed.charAt(i++);--value;")//
        .gives("int count = packed.charAt(i++), value = packed.charAt(i++)-1;")//
        .stays()//
    ;
  }

  @Test public void c1() {
    trimmingOf("int x = 1, y = x; ++x;")//
        .stays();
  }

  /** an example of the difference between TipperTest's trimmingOf and
   * TestUtilsSpartanizer trimmingOf. {@link LocalInitializedUnusedRemove} is
   * used here */
  @Test public void c2() {
    TestsUtilsSpartanizer.trimmingOf("int x = 1, y = x; ++x;")//
        .gives("int x = 1; ++x;")//
        .gives("int x = 1+1;");
  }

  @Test public void d() {
    trimmingOf("int x = 1; ++x;")//
        .givesEither("int x = 2;", "int x = 1+1;", "", null);
  }

  // issue #1125
  @Test public void e() {
    TestsUtilsSpartanizer.trimmingOf("int $ = f(x);++$;return $;")//
        .using(tipper(), new PrefixIncrementDecrementReturn())//
        .gives("int $ = f(x) + 1;return $;")//
    ;
  }
}
