package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.LocalInitializedArithmeticsInline;
import il.org.spartan.spartanizer.tipping.Tipper;

/** see issue 1397 on github for more info
 * @author Niv Shalmon
 * @since 2017-05-24 */
public class Issue1397 extends TipperTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> tipper() {
    return new LocalInitializedArithmeticsInline();
  }
  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }
  @Test public void test01() {
    trimmingOf("int x = 1; o.y += 1;").stays();
  }
}
