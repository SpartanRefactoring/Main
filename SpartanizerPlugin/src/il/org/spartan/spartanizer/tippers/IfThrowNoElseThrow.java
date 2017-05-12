package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * if (x)
 *   throw foo();
 * throw bar();
 * } into {@code
 * throw a ? foo() : bar();
 * }
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class IfThrowNoElseThrow extends GoToNextStatement<IfStatement>//
    implements TipperCategory.Ternarization {
  private static final long serialVersionUID = 0x7B2F65029551C18BL;

  static Expression getThrowExpression(final Statement ¢) {
    final ThrowStatement $ = extract.throwStatement(¢);
    return $ == null ? null : extract.core($.getExpression());
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate into a single 'throw'";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s))
      return null;
    final Expression $ = getThrowExpression(then(s));
    if ($ == null)
      return null;
    final Expression e2 = getThrowExpression(nextStatement);
    return e2 == null ? null : misc.replaceTwoStatements(r, s, subject.operand(subject.pair($, e2).toCondition(s.getExpression())).toThrow(), g);
  }
}
