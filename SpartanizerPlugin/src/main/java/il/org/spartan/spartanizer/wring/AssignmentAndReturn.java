package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** convert
 *
 * <pre>
 * a = 3;
 * return a;
 * </pre>
 *
 * to
 *
 * <pre>
 * return a = 3;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-28 */
// TODO: Dan/Alex: add an issue for the following: if a is a local variable, you
// convert to simply a=3. Assignment to local variables should not be preserved.
// To make this happen, we need a wring that removes dead assignments to local
// variables. Attach the "new-wring" label to this one.
public final class AssignmentAndReturn extends Wring.ReplaceToNextStatement<Assignment> implements Kind.Collapse {
  @Override String description(final Assignment ¢) {
    return "Inline assignment to " + left(¢) + " with its subsequent 'return'";
  }

  @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(left(a), core(s.getExpression())))
      return null;
    r.remove(parent, g);
    r.replace(s, subject.operand(a).toReturn(), g);
    return r;
  }
}
