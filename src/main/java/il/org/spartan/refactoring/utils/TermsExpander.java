package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Plant.*;
import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

/** Expands terms of +/- expressions without reordering.
 * @author Yossi Gil
 * @since 2016-08 */
public class TermsExpander {
  public static Expression simplify(final InfixExpression e) {
    return build(new TermsCollector(e));
  }

  static Expression build(final TermsCollector c) {
    return build(c.all());
  }

  static Expression build(List<SignedExpression> es) {
    assert es.size() >= 2;
    return build(first(es).asExpression(), chop(es));
  }

  private static Expression build(Expression first, List<SignedExpression> rest) {
    assert first != null;
    if (rest == null)
      return first;
    assert first instanceof InfixExpression;
    return build((InfixExpression) first, rest);
  }

  private static Expression build(InfixExpression first, List<SignedExpression> rest) {
    assert first != null;
    assert rest != null;
    Operator o = first.getOperator();
    assert o == PLUS2 || o == MINUS2;
    SignedExpression next = first(rest);
    assert next != null;
    Expression $ = o == PLUS2 ? buildPlus(first, next) : buildMinus(first, next);
    List<SignedExpression> remainder = chop(rest);
    return remainder == null ? $ : build($, remainder);
  }

  private static InfixExpression buildMinus(InfixExpression base, SignedExpression add) {
    return add.negative() ? append(base, add.expression) : subject.pair(base, add.expression).to(PLUS2);
  }

  private static InfixExpression buildPlus(InfixExpression base, SignedExpression add) {
    return add.positive() ? append(base, add.expression) : subject.pair(base, add.expression).to(MINUS2);
  }

  private static InfixExpression append(InfixExpression $, Expression e) {
    extendedOperands($).add(plant(e).into($));
    return $;
  }
}
