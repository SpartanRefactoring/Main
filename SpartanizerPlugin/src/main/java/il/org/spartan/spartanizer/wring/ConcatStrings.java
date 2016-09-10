package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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

  @SuppressWarnings("unused") @Override String description(final InfixExpression x) {
    return "Concat the string literals to a single string";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> operands = extract.allOperands(x);
    assert operands.size() >= 2;
    boolean isChanged = false;
    // TODO: NIV Convert into a for(;;) loop
    int i = 0;
    while (i < operands.size() - 1)
      if (operands.get(i).getNodeType() != ASTNode.STRING__LITERAL || operands.get(i + 1).getNodeType() != ASTNode.STRING__LITERAL)
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
    return operands.size() <= 1 ? operands.get(0) : subject.operands(operands).to(wizard.PLUS2);
  }
}
