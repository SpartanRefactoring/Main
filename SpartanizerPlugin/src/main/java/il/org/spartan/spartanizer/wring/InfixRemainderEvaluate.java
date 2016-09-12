package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Evaluate the remainder of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int % int --> int <br/>
 * long % long --> long <br/>
 * int % long --> long <br/>
 * long % int --> long <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class InfixRemainderEvaluate extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompatible(first(xs)))
      return null;
    int remainder = EvaluateAux.extractInt(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompatible(¢))
        return null;
      if (index != 0)
        remainder %= EvaluateAux.extractInt(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(remainder));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompatible(first(xs)))
      return null;
    long remainder = EvaluateAux.extractLong(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompatible(¢))
        return null;
      if (index != 0)
        remainder %= EvaluateAux.extractLong(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Long.toString(remainder) + "L");
  }

  @Override public String description() {
    return "Evaluate remainder of numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate remainder of numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != REMAINDER || EvaluateAux.getEvaluatedType(x) == null)
      return null;
    switch (EvaluateAux.getEvaluatedType(x).asPrimitiveCertain()) {
      case INT:
        $ = replacementInt(extract.allOperands(x), x);
        break;
      case LONG:
        $ = replacementLong(extract.allOperands(x), x);
        break;
      default:
        return null;
    }
    return $ != null && az.numberLiteral($).getToken().length() < sourceLength ? $ : null;
  }
}
