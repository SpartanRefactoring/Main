package il.org.spartan.spartanizer.tippers.junit;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code
 * assert a;
 * assert b;
 * } into {@code
 * assert a && b;
 * }
 * @author Dor Ma'ayan
 * @since 2018-12-023 */
public final class ConsolidateAssertions extends EagerTipper<AssertStatement> //
    implements Category.Idiomatic {
  private static final long serialVersionUID = 6446305952040518541L;
  @Override public String description(final AssertStatement n) {
    return "Consolidate assertions";
  }
  @Override public Tip tip(final AssertStatement ¢) {
    if (iz.assertStatement(extract.nextStatement(¢)))
      return new Tip(description(¢), getClass(), ¢) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          final AssertStatement a = az.assertStatement(extract.nextStatement(¢));
          final InfixExpression e = ¢.getAST().newInfixExpression();
          e.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
          e.setLeftOperand(copy.of(¢.getExpression()));
          e.setRightOperand(copy.of(a.getExpression()));
          r.replace(¢.getExpression(), e, g);
          r.remove(a, g);
        }
      };
    return null;
  }
}
