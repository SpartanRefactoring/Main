package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tests for {@link LocalInitializedNewAddAll} see thus numbered github issue
 * for more info
 * @author Yossi Gil
 * @author Niv Shalmon
 * @since 2017-05-26 */
public class Issue1314 extends TipperTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> tipper() {
    return new LocalInitializedNewAddAll();
  }
  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }
  @Test public void test0() {
    trimmingOf("List<T> x = new ArrayList<>(); x.addAll(ys);").gives("List<T> x = new ArrayList<>(ys);");
  }
  @Test public void test01() {
    trimmingOf("List<T> x = new ArrayList<>(); x.addAll(ys); y(x); return x;") //
        .gives("List<T> x = new ArrayList<>(ys); y(x); return x;")//
    ;
  }
  @Test public void test02() {
    trimmingOf("List<T> x = new LinkedList<>(); f(x); x.addAll(ys);")//
        .stays();
  }
}
