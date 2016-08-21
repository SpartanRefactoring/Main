package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>0+X</code>, <code>X+0</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixAdditionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression e) {
    return "Remove all additions and substructions of 0 to and from " + e;
  }
  
  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() == PLUS ? replacementPlus(extract.allOperands(e)) : null;
  }
  
  
  
  private static ASTNode replacementPlus(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
      if (!isLiteralZero(¢))
        $.add(¢);
    return $.size() == es.size() ? null : $.size() == 0 ? duplicate(first(es)) : $.size() == 1 ? duplicate(first($)) : subject.operands($).to(PLUS);
  }
  /*private static ASTNode replacementPlus_old(final List<Expression> es) {
    return isLiteralZero(es.get(0)) ? duplicate(es.get(1)) : isLiteralZero(es.get(1)) ? duplicate(es.get(0)) : null;
  }*/
}