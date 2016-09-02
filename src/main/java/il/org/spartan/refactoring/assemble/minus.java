package il.org.spartan.refactoring.assemble;

import static il.org.spartan.refactoring.ast.extract.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

public enum minus {
  ;
  public static int level(final InfixExpression x) {
    return lisp.out(x.getOperator(), TIMES, DIVIDE) ? 0 : level(hop.operands(x));
  }

  public static int level(final List<Expression> es) {
    int $ = 0;
    for (final Expression e : es)
      $ += minus.level(e);
    return $;
  }

  public static int level(final Expression ¢) {
    return iz.is(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
        : iz.is(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
            : iz.is(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
                : iz.is(¢, NUMBER_LITERAL) ? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
                    : 0;
  }

  public static Expression peel(final Expression $) {
    return iz.is($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
        : iz.is($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
            : iz.is($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
                : iz.is($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
                    : $;
  }

  public static Expression peel(final NumberLiteral $) {
    return !$.getToken().startsWith("-") && !$.getToken().startsWith("+") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }

  public static Expression peel(final PrefixExpression $) {
    return lisp.out($.getOperator(), wizard.MINUS1, wizard.PLUS1) ? $ : peel($.getOperand());
  }

  public static Expression peel(final InfixExpression x) {
    return lisp.out(x.getOperator(), TIMES, DIVIDE) ? x : subject.operands(peel(hop.operands(x))).to(x.getOperator());
  }

  private static List<Expression> peel(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression e : es)
      $.add(peel(e));
    return $;
  }

  private static int level(final PrefixExpression ¢) {
    return az.bit(¢.getOperator() == wizard.MINUS1) + level(¢.getOperand());
  }
}
