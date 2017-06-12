package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code
 * if (x)
 *   return foo();
 * return bar();
 * } into {@code
 * return a ? foo() : bar();
 * } return a; g(); }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfReturnNoElseReturn extends GoToNextStatement<IfStatement>//
    implements Category.Ternarization {
  private static final long serialVersionUID = -0x103A4A950D4E4BC9L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate into a single 'return'";
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s))
      return null;
    final ReturnStatement r1 = extract.returnStatement(then(s));
    if (r1 == null)
      return null;
    final Expression $ = extract.core(r1.getExpression());
    if ($ == null)
      return null;
    final ReturnStatement r2 = extract.returnStatement(nextStatement);
    if (r2 == null)
      return null;
    final Expression e2 = extract.core(r2.getExpression());
    return e2 == null ? null : misc.replaceTwoStatements(r, s, subject.operand(subject.pair($, e2).toCondition(s.getExpression())).toReturn(), g);
  }
}
