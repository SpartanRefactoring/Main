package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Concat some strings to one string
 *
 * <pre>
 * "ab" + "c" + "de"
 * </pre>
 *
 * to
 *
 * <pre>
 * "abcde"
 * </pre>
 *
 * @author Dor Ma'ayan
 * @author Nov Shalmon
 * @since 2016-09-04 */
public final class InfixExpressionConcatentateCompileTime extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  @Override public String description() {
    return "Concat the strings to a one string";
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Concat the string literals to a single string";
  }
  @Override public ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> operands = extract.allOperands(x);
    assert operands.size() >= 2;
    boolean isChanged = false;
    for (int i = 0; i < operands.size() - 1;)
      if (operands.get(i).getNodeType() != ASTNode.STRING_LITERAL || operands.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)
        ++i;
      else {
        isChanged = true;
        final StringLiteral l = x.getAST().newStringLiteral();
        l.setLiteralValue(((StringLiteral) operands.get(i)).getLiteralValue() + ((StringLiteral) operands.get(i + 1)).getLiteralValue());
        operands.remove(i);
        operands.remove(i);
        operands.add(i, l);
      }
    if (!isChanged)
      return null;
    assert !operands.isEmpty();
    return operands.size() <= 1 ? first(operands) : subject.operands(operands).to(wizard.PLUS2);
  }
}
