package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

/** see issue #814 for more details
 * @author yaelAmitay
 * @author koral chapnik
 * @since 16-11-10 */
public class Issue814 {
  @Test @SuppressWarnings("static-method") public void nullTest() {
    final Assignment a = into.a("a = 3");
    final Statement s = into.s("f();");
    a.getParent().delete();
    assert new AssignmentAndReturn().go(null, a, s, null) == null;
  }

  @Test @SuppressWarnings("static-method") public void simpleTest() {
    final MethodDeclaration m = into.m("public int p(){ int a;a = 3; return a; }");
    final ReturnStatement s = new MethodExplorer(m).returnStatements().get(0);
    m.accept(new ASTVisitor() {
      @Override public boolean visit(final Assignment a) {
        final ASTRewrite r = ASTRewrite.create(m.getAST());
        assert new AssignmentAndReturn().go(r, a, s, new TextEditGroup("")) != null;
        return true;
      }
    });
  }
}
