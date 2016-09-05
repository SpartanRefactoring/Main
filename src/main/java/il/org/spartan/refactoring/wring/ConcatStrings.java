package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

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
public class ConcatStrings extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override public String description() {
    return "Concat the strings to a one string";
  }

  @SuppressWarnings("unused") @Override String description(final InfixExpression n) {
    return "Concat the string literals to a single string";
  }

  @Override ASTNode replacement(final InfixExpression n) {
    if (n.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> operands = extract.allOperands(n);
    assert operands.size() >= 2;
    boolean isChanged = false;
    int i = 0;
    while (i < operands.size() - 1)
      if (operands.get(i).getNodeType() == ASTNode.STRING_LITERAL && //
          operands.get(i + 1).getNodeType() == ASTNode.STRING_LITERAL) {
        isChanged = true;
        final StringLiteral l = n.getAST().newStringLiteral();
        l.setLiteralValue(((StringLiteral) operands.get(i)).getLiteralValue() //
            + ((StringLiteral) operands.get(i + 1)).getLiteralValue());
        operands.remove(i);
        operands.remove(i);
        operands.add(i, l);
      } else
        i++;
    if (!isChanged)
      return null;
    assert operands.size() >= 1;
    return operands.size() > 1 ? subject.operands(operands).to(wizard.PLUS2) : operands.get(0);
  }
}
