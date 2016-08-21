package il.org.spartan.refactoring.wring;

//import static il.org.spartan.refactoring.utils.extract.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
//import static il.org.spartan.refactoring.utils.expose.*;
//import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

//import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>X-0</code> by
 * <code>X</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression e) {
    return "Remove substraction of 0 in " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != MINUS ? null : replacementMinus(extract.allOperands(e), e);
  }
  
  
  private static ASTNode replacementMinus(final List<Expression> es, final InfixExpression e) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
      if (!isLiteralZero(¢))
        $.add(¢);
    return $.size() == es.size() ? null : $.size() == 0 ? duplicate(first(es)) : $.size() == 1 ? 
        isLiteralZero(left(e)) ? subject.operand(right(e)).to(PrefixExpression.Operator.MINUS) : duplicate(left(e)) : null;
  }
  /*private static ASTNode replacementMinus(final InfixExpression e) {
    return isLiteralZero(left(e)) ? subject.operand(right(e)).to(PrefixExpression.Operator.MINUS)
        : isLiteralZero(right(e)) ? duplicate(left(e)) : null;
  }*/
}
