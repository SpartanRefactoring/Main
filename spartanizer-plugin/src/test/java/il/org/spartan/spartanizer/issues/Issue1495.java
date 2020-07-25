package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.LocalUninitializedAssignment;
import il.org.spartan.spartanizer.tipping.Tipper;

/** TODO Yuval Simon: document class
 * @author Yuval Simon
 * @since 2017-06-19 */
public class Issue1495 extends TipperTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> tipper() {
    return new LocalUninitializedAssignment();
  }
  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }
  @Test public void t1() {
    trimmingOf("boolean b2, b3 = ownerType == null;"//
        + "b2 = !b3 ? ownerType != null : !ownerType.equals(ownerType + a);")//
            .gives("boolean b3 = ownerType == null;"//
                + "boolean b2 = !b3 ? ownerType != null : !ownerType.equals(ownerType + a);")//
            .stays();
  }
}
