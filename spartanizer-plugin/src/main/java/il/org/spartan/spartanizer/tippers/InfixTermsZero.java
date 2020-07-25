package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.operator;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.type;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.PLUS;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code 0+X}, {@code X+0}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixTermsZero extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Theory.Arithmetics.Numeric {
  private static final long serialVersionUID = 0x4FC12AA812633EA3L;

  private static ASTNode replacement(final List<Expression> ¢) {
    final List<Expression> $ = ¢.stream().filter(λ -> !iz.literal0(λ)).collect(toList());
    return $.size() == ¢.size() ? null
        : $.isEmpty() ? copy.of(the.firstOf(¢)) : $.size() == 1 ? copy.of(the.firstOf($)) : subject.operands($).to(PLUS);
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