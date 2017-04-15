package il.org.spartan.spartanizer.ast.factory;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.navigate.wizard.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** takes care of of multiplicative terms with minus symbol in them.
 * <p>
 * An empty {@code enum} for fluent programming. The name should say it all: The
 * name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum eliminate {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Remove the last statement residing under a given {@link Statement}, if ¢
   * is empty or has only one statement return empty statement.
   * @param ¢ JD {@code null if not such sideEffects exists.
   * @return Given {@link Statement} without the last inner statement, if ¢ is
   *         empty or has only one statement return empty statement. */
  public static Statement lastStatement(final Statement $) {
    final Block b = az.block($);
    if (b == null)
      return make.emptyStatement($);
    final List<Statement> ss = step.statements(b);
    if (ss.isEmpty())
      return make.emptyStatement($);
    ss.remove(ss.size() - 1);
    return $;
  }

  @SuppressWarnings("boxing") public static int level(final Collection<Expression> xs) {
    return xs.stream().map(eliminate::level).reduce((x, y) -> x + y).get();
  }

  public static int level(final Expression ¢) {
    return iz.nodeTypeEquals(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
        : iz.nodeTypeEquals(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
            : iz.nodeTypeEquals(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
                : iz.nodeTypeEquals(¢, NUMBER_LITERAL) ? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
                    : 0;
  }

  public static int level(final InfixExpression ¢) {
    return out(¢.getOperator(), TIMES, DIVIDE) ? 0 : level(hop.operands(¢));
  }

  public static Expression peel(final Expression $) {
    return iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
        : iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
            : iz.nodeTypeEquals($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
                : iz.nodeTypeEquals($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
                    : $;
  }

  public static Expression peel(final InfixExpression ¢) {
    return out(¢.getOperator(), TIMES, DIVIDE) ? ¢ : subject.operands(peel(hop.operands(¢))).to(¢.getOperator());
  }

  public static Expression peel(final NumberLiteral $) {
    return !$.getToken().startsWith("-") && !$.getToken().startsWith("+") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }

  public static Expression peel(final PrefixExpression $) {
    return out($.getOperator(), op.MINUS1, op.PLUS1) ? $ : peel($.getOperand());
  }

  private static int level(final PrefixExpression ¢) {
    return az.bit(¢.getOperator() == op.MINUS1) + level(¢.getOperand());
  }

  private static List<Expression> peel(final Collection<Expression> ¢) {
    return ¢.stream().map(eliminate::peel).collect(toList());
  }
}
