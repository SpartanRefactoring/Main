package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.junit.Test;

import fluent.ly.the;
import il.org.spartan.spartanizer.engine.MethodExplorer;
import il.org.spartan.spartanizer.engine.parse;
import il.org.spartan.spartanizer.tippers.AssignmentAndReturn;

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
