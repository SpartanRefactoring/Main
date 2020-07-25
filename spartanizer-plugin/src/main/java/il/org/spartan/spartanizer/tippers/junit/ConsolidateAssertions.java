package il.org.spartan.spartanizer.tippers.junit;

import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
