package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import fluent.ly.range;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.engine.type.Primitive.Certain;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Converts {@code ""+"foo"} to {@code "foo"} when x is of __ String
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-08-29 */
public final class InfixPlusEmptyString extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Theory.Strings, Category.Transformation.Prune {
  private static final long serialVersionUID = -0x17CCB52A168511EBL;

  @Override public String description() {
    return "[\"\"+foo]->foo";
  }
  @Override public String description(final InfixExpression ¢) {
    return "Omit concatentation of \"\" to " + (iz.emptyStringLiteral(right(¢)) ? left(¢) : right(¢));
  }
  @Override @SuppressWarnings("boxing") public Expression replacement(final InfixExpression x) {
    if (type.of(x) != Certain.STRING)
      return null;
    final List<Expression> ret = hop.operands(x);
    assert ret.size() > 1;
    final List<Expression> $ = an.empty.list();
    boolean isArithmetic = true;
    for (final Integer i : range.from(0).to(ret.size())) {
      final Expression e = ret.get(i);
      if (!iz.emptyStringLiteral(e)) {
        $.add(e);
        if (type.of(e) == Certain.STRING)
          isArithmetic = false;
      } else {
        if (i < ret.size() - 1 && type.of(ret.get(i + 1)) == Certain.STRING)
          continue;
        if (isArithmetic) {
          $.add(e);
          isArithmetic = false;
        }
      }
    }
    return $.size() == ret.size() ? null : $.size() == 1 ? the.firstOf($) : subject.operands($).to(op.PLUS2);
  }
}
