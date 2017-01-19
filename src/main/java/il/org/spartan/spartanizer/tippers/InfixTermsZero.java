package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace <code>0+X</code>, <code>X+0</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixTermsZero extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = xs.stream().filter(¢ -> !iz.literal0(¢)).collect(Collectors.toList());
    return $.size() == xs.size() ? null : $.isEmpty() ? copy.of(first(xs)) : $.size() == 1 ? copy.of(first($)) : subject.operands($).to(PLUS);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Remove all additions and substructions of 0 to and from " + ¢;
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != PLUS || !type.isNotString(¢) ? null : replacement(extract.allOperands(¢));
  }
}