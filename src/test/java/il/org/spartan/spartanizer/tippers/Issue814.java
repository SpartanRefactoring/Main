package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

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
  @SuppressWarnings("static-method") @Test public void allParamsNull() {
    MethodDeclaration m = into.m("public int p(){ int a;a = 3; return a; }");
    MethodExplorer e = new MethodExplorer(m);
    ReturnStatement s = e.returnStatements().get(0);
    m.accept(new ASTVisitor() {
      @Override public boolean visit(final Assignment a) {
        ASTRewrite r = ASTRewrite.create(m.getAST());
        ASTRewrite new_r = (new AssignmentAndReturn()).go(r, a, s, (new TextEditGroup("")));
        assertFalse(new_r == null);
        return true;
      }
    });
  }
}
