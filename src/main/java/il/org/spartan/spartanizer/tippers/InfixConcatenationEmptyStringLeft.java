package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert <code>""+x</code> to <code>x+""</code>
 * @author Dan Greenstein
 * @author Niv Shalmon
 * @since 2016 */
public final class InfixConcatenationEmptyStringLeft extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Collapse {
  private static InfixExpression replace(final InfixExpression x) {
    final List<Expression> es = extract.allOperands(x);
    swap(es, 0, 1);
    return subject.operands(es).to(wizard.PLUS2);
  }
  // TODO: Yossi Gil: this should probably be in lisp, but I can'tipper access
  // its
  // source
  // anymore
  /** swaps two elements in an indexed list in given indexes, if they are legal
   * @param ts the indexed list
   * @param i1 the index of the first element
   * @param i2 the index of the second element
   * @return the list after swapping the elements */
  private static <T> List<T> swap(final List<T> ts, final int i1, final int i2) {
    if (i1 < ts.size() && i2 < ts.size()) {
      final T t = ts.get(i1);
      lisp.replace(ts, ts.get(i2), i1);
      lisp.replace(ts, t, i2);
    }
    return ts;
  }
  @Override public String description(final InfixExpression ¢) {
    return "Append, rather than prepend, \"\", to " + left(¢);
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !iz.emptyStringLiteral(left(¢)) || !iz.infixPlus(¢) ? null : replace(¢);
  }
}
