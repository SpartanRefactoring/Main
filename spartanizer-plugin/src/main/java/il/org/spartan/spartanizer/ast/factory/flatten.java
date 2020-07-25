package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.navigate.extract.core;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum flatten {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Flatten the list of arguments to an {@link InfixExpression}, e.g., convert
   * an expression such as {@code (a + b) + c} whose inner form is roughly
   * "+(+(a,b),c)", into {@code a + b + c}, whose inner form is (roughly)
   * "+(a,b,c)".
   * @param $ JD
   * @return a duplicate of the argument, with the a flattened list of
   *         operands. */
  public static InfixExpression of(final InfixExpression $) {
    assert $ != null;
    final Operator ret = $.getOperator();
    assert ret != null;
    return subject.operands(flatten.into(ret, hop.operands($), an.empty.list())).to(copy.of($).getOperator());
  }
  private static List<Expression> add(final Expression x, final List<Expression> $) {
    $.add(x);
    return $;
  }
  private static List<Expression> into(final Operator o, final Expression x, final List<Expression> $) {
    final Expression ret = core(x);
    final InfixExpression inner = az.infixExpression(ret);
    return inner == null || inner.getOperator() != o ? add(!iz.noParenthesisRequired(ret) ? x : ret, $)
        : flatten.into(o, copy.adjust(o, hop.operands(inner)), $);
  }
  private static List<Expression> into(final Operator o, final Iterable<Expression> xs, final List<Expression> $) {
    xs.forEach(λ -> into(o, λ, $));
    return $;
  }
}
