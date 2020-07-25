package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.LocalInitializedArithmeticsInline;
import il.org.spartan.spartanizer.tipping.Tipper;

/** see Github issue thus numbered for more info unit tests for
 * {@link LocalInitializedArithmeticsInline}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-18 */
@Ignore("We do not know how to control the formatting")
public class Issue1257 extends TipperTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> tipper() {
    return new LocalInitializedArithmeticsInline();
  }
  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }
  @Test public void j() {
    trimmingOf("int x = 0; x += ++b;")//
        .gives("int x = 0 + (++b);");
  }
}
