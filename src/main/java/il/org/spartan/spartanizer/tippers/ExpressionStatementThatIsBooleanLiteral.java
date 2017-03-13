package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code ?.that(M?, X, is(boolean)); } by {@code assert x == M?; }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016/12/11 */
public final class ExpressionStatementThatIsBooleanLiteral extends ReplaceCurrentNode<ExpressionStatement>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -5353123467714120198L;
  private List<Expression> arguments;
  private MethodInvocation methodInvocation;
  private Expression first;
  private BooleanLiteral booleanLiteral;
  private ExpressionStatement expressionStatement;

  @Override public String description(final ExpressionStatement ¢) {
    return "Rewrite '" + expression(¢) + "' as assert command";
  }

  @Override protected boolean prerequisite(final ExpressionStatement ¢) {
    return set(az.methodInvocation((expressionStatement = ¢).getExpression())) //
        && set(methodInvocation.getName()) //
        && set(arguments(methodInvocation)) //
        && setFirst(first(arguments)) //
        && setSecond(second(arguments));
  }

  @Override public AssertStatement replacement(final ExpressionStatement ¢) {
    assert ¢ == expressionStatement;
    return subject.operand(subject.pair(first, booleanLiteral).to(InfixExpression.Operator.EQUALS)).toAssert();
  }

  boolean set(final List<Expression> arguments) {
    return (this.arguments = arguments).size() == 2;
  }

  boolean set(final MethodInvocation ¢) {
    return (methodInvocation = ¢) != null;
  }

  static boolean set(final SimpleName ¢) {
    return "that".equals(¢ + "");
  }

  boolean setFirst(final Expression ¢) {
    return (first = ¢) != null;
  }

  boolean setSecond(final Expression ¢) {
    final MethodInvocation $ = az.methodInvocation(¢);
    return as.set("is").contains($.getName() + "") && (booleanLiteral = az.booleanLiteral(first(arguments($)))) != null;
  }
}
