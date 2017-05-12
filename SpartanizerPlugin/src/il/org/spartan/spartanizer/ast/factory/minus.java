package il.org.spartan.spartanizer.ast.factory;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** takes care of of multiplicative terms with minus symbol in them.
 * <p>
 * An empty {@code enum} for fluent programming. The name should say it all: The
 * name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum minus {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Remove the last statement residing under a given {@link Statement}, if ¢
   * is empty or has only one statement return empty statement.
   * @param ¢ JD {@code null if not such sideEffects exists.
   * @return Given {@link Statement} without the last inner statement, if ¢ is
   *         empty or has only one statement return empty statement. */
  public static Statement lastStatement(final Statement $) {
    if (!iz.block($))
      return make.emptyStatement($);
    final List<Statement> ss = step.statements(az.block($));
    if (!ss.isEmpty())
      ss.remove(ss.size() - 1);
    return $;
  }

  public static int level(final Expression ¢) {
    return iz.nodeTypeEquals(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
        : iz.nodeTypeEquals(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
            : iz.nodeTypeEquals(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
                : iz.nodeTypeEquals(¢, NUMBER_LITERAL) ? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
                    : 0;
  }

  public static int level(final InfixExpression ¢) {
    return is.out(operator(¢), TIMES, DIVIDE) ? 0 : level(hop.operands(¢));
  }

  @SuppressWarnings("boxing") public static int level(final Collection<Expression> xs) {
    return xs.stream().map(minus::level).reduce((x, y) -> x + y).get();
  }

  private static int level(final PrefixExpression ¢) {
    return az.bit(operator(¢) == op.MINUS1) + level(operand(¢));
  }

  public static Expression peel(final Expression $) {
    return iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
        : iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
            : iz.nodeTypeEquals($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
                : iz.nodeTypeEquals($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
                    : $;
  }

  public static Expression peel(final InfixExpression ¢) {
    return is.out(operator(¢), TIMES, DIVIDE) ? ¢ : subject.operands(peel(hop.operands(¢))).to(operator(¢));
  }

  private static List<Expression> peel(final Collection<Expression> ¢) {
    return ¢.stream().map(minus::peel).collect(toList());
  }

  public static Expression peel(final NumberLiteral $) {
    return !token($).startsWith("-") && !token($).startsWith("+") ? $ : $.getAST().newNumberLiteral(token($).substring(1));
  }

  public static Expression peel(final PrefixExpression $) {
    return is.out(operator($), op.MINUS1, op.PLUS1) ? $ : peel(operand($));
  }
}
