package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;

/** see issue #814 for more details
 * @author yaelAmitay
 * @author koral chapnik
 * @since 16-11-10 */
public class Issue0814 {
  @Test @SuppressWarnings("static-method") public void nullTest() {
    final Assignment a = parse.a("a = 3");
    final Statement s = parse.s("f();");
    a.getParent().delete();
    assert new AssignmentAndReturn().go(null, a, s, null) == null;
  }
  @Test @SuppressWarnings("static-method") public void simpleTest() {
    final MethodDeclaration m = parse.m("public int p(){ int a;a = 3; return a; }");
    final ReturnStatement s = the.firstOf(new MethodExplorer(m).returnStatements());
    // noinspection SameReturnValue
    m.accept(new ASTVisitor(true) {
      @Override public boolean visit(final Assignment a) {
        final ASTRewrite r = ASTRewrite.create(m.getAST());
        assert new AssignmentAndReturn().go(r, a, s, new TextEditGroup("")) != null;
        return true;
      }
    });
  }
}
