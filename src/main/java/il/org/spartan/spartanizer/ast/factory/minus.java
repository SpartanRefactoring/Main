package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** takes care of of multiplicative terms with minus symbol in them.
 * <p>
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum minus {
  ;
  /** Remove the last statement residing under a given {@link Statement}, if ¢
   * is empty or has only one statement return empty statement.
   * @param ¢ JD <code><b>null</b></code> if not such sideEffects exists.
   * @return Given {@link Statement} without the last inner statement, if ¢ is
   *         empty or has only one statement return empty statement. */
  public static Statement lastStatement(final Statement ¢) {
    if (!iz.block(¢))
      return make.emptyStatement(¢);
    final List<Statement> ss = step.statements(az.block(¢));
    if (!ss.isEmpty())
      ss.remove(ss.size() - 1);
    return ¢;
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

  public static int level(final List<Expression> xs) {
    int $ = 0;
    for (final Expression ¢ : xs)
      $ += minus.level(¢);
    return $;
  }

  private static int level(final PrefixExpression ¢) {
    return az.bit(¢.getOperator() == wizard.MINUS1) + level(¢.getOperand());
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

  private static List<Expression> peel(final List<Expression> ¢) {
    return ¢.stream().map(minus::peel).collect(Collectors.toList());
  }

  public static Expression peel(final NumberLiteral $) {
    return !$.getToken().startsWith("-") && !$.getToken().startsWith("+") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }

  public static Expression peel(final PrefixExpression $) {
    return out($.getOperator(), wizard.MINUS1, wizard.PLUS1) ? $ : peel($.getOperand());
  }
}
