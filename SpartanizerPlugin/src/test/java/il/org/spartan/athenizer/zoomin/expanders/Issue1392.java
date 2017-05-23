package il.org.spartan.athenizer.zoomin.expanders;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO dormaayn: document class
 * @author dormaayn
 * @since 2017-05-23 */
public class Issue1392 extends BloaterTest<MethodDeclaration> {
  @Override public Tipper<MethodDeclaration> bloater() {
    return new AddModifiersToMethodDeclaration();
  }
  @Override public Class<MethodDeclaration> tipsOn() {
    return MethodDeclaration.class;
  }
  @Test public void test0() {
    bloatingOf(" public final class Check{ public int check(){}}").gives("public final class C{public final void check(){}}");
  }
}
