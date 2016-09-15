package il.org.spartan.spartanizer.assemble;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** takes care of of multiplicative terms with minus symbol in them.
 * <p>
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum minus {
  ;
  public static int level(final Expression ¢) {
    return iz.is(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
        : iz.is(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
            : iz.is(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
                : iz.is(¢, NUMBER_LITERAL) ? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
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

  public static Expression peel(final Expression $) {
    return iz.is($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
        : iz.is($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
            : iz.is($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
                : iz.is($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
                    : $;
  }

  public static Expression peel(final InfixExpression ¢) {
    return out(¢.getOperator(), TIMES, DIVIDE) ? ¢ : subject.operands(peel(hop.operands(¢))).to(¢.getOperator());
  }

  public static Expression peel(final NumberLiteral $) {
    return !$.getToken().startsWith("-") && !$.getToken().startsWith("+") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }

  public static Expression peel(final PrefixExpression $) {
    return out($.getOperator(), wizard.MINUS1, wizard.PLUS1) ? $ : peel($.getOperand());
  }

  private static int level(final PrefixExpression ¢) {
    return az.bit(¢.getOperator() == wizard.MINUS1) + level(¢.getOperand());
  }

  private static List<Expression> peel(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      $.add(peel(¢));
    return $;
  }
}
