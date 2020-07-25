package il.org.spartan.spartanizer.tippers;

import static fluent.ly.lisp.chop;
import static fluent.ly.lisp.cons;
import static il.org.spartan.spartanizer.ast.navigate.hop.operands;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code X-0} by {@code X} and {@code 0-X} by {@code -X}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Transformation.Prune, Category.Theory.Arithmetics.Numeric {
  private static final long serialVersionUID = -0x1DD7EC059CC8417AL;

  private static List<Expression> minusFirst(final List<Expression> prune) {
    return cons(cons.minus(the.firstOf(prune)), chop(prune));
  }
  private static List<Expression> prune(final Collection<Expression> ¢) {
    final List<Expression> $ = ¢.stream().filter(λ -> !iz.literal0(λ)).collect(toList());
    return $.size() != ¢.size() ? $ : null;
  }
  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = prune(xs);
    if ($ == null)
      return null;
    final Expression first = the.firstOf(xs);
    if ($.isEmpty())
      return make.from(first).literal(0);
    assert !$.isEmpty();
    if ($.size() == 1)
      return !iz.literal0(first) ? first : cons.minus(the.firstOf($));
    assert $.size() >= 2;
    return subject.operands(!iz.literal0(first) ? $ : minusFirst($)).to(il.org.spartan.spartanizer.ast.navigate.op.MINUS2);
  }
  @Override public String description(final InfixExpression ¢) {
    return "Remove subtraction of 0 in " + ¢;
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != MINUS ? null : replacement(operands(¢));
  }
}
