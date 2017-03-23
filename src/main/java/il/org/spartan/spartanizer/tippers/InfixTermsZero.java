package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code 0+X}, {@code X+0}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixTermsZero extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  private static final long serialVersionUID = 5746921500852829859L;

  private static ASTNode replacement(final List<Expression> ¢) {
    final List<Expression> $ = ¢.stream().filter(λ -> !iz.literal0(λ)).collect(toList());
    return $.size() == ¢.size() ? null : $.isEmpty() ? copy.of(first(¢)) : $.size() == 1 ? copy.of(first($)) : subject.operands($).to(PLUS);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Remove all additions and substructions of 0 to and from " + ¢;
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return (operator(¢) != PLUS && operator(¢) != MINUS || !(¢ == initializer(az.variableDeclrationFragment(parent(¢)))
        & iz.intType(type(az.variableDeclarationStatement(parent(az.variableDeclrationFragment(parent(¢))))))))
        && (operator(¢) != PLUS && operator(¢) != MINUS || !type.isNotString(¢)) ? null : replacement(extract.allOperands(¢));
  }
}