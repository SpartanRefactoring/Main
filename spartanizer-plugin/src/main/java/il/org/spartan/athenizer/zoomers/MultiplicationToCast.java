package il.org.spartan.athenizer.zoomers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.PrimitiveType;

import il.org.spartan.athenizer.zoom.zoomers.Issue1007;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Covers Issues #1006 & #1007 toList Convert : {@code
 * x * 1. / x * 1.0
 * x * 1L
 * } To: {@code
 * (double) x
 * (long) x
 * } Tested in {@link Issue1007}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-11 */
public class MultiplicationToCast extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x57EAC7C1A6BFDD34L;

  @Override public ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != Operator.TIMES)
      return null;
    int i = 0;
    boolean found = false;
    final CastExpression $ = x.getAST().newCastExpression();
    for (final Expression e : extract.allOperands(x)) {
      if (iz.hexaDecimal(e))
        continue;
      if (iz.literal(e, 1.)) {
        $.setType(x.getAST().newPrimitiveType(PrimitiveType.DOUBLE));
        found = true;
      }
      if (iz.literal(e, 1L)) {
        $.setType(x.getAST().newPrimitiveType(PrimitiveType.LONG));
        found = true;
      }
      if (found) {
        if (x.hasExtendedOperands()) {
          final List<Expression> xs = extract.allOperands(x);
          xs.remove(i);
          $.setExpression(subject.operands(xs).to(x.getOperator()));
        } else {
          if (i == 0) {
            $.setExpression(copy.of(x.getRightOperand()));
            return $;
          }
          $.setExpression(copy.of(x.getLeftOperand()));
        }
        return $;
      }
      ++i;
    }
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return null;
  }
}
