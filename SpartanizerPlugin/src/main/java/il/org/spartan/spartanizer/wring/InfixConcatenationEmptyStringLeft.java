package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Convert <code>""+x</code> to <code>x+""</code>
 * @author Dan Greenstein
 * @author Niv Shalmon
 * @since 2016 */
public class InfixConcatenationEmptyStringLeft extends ReplaceCurrentNode<InfixExpression> implements Kind.Collapse {
  private static InfixExpression replace(final InfixExpression x) {
    final List<Expression> es = extract.allOperands(x);
    swap(es, 0, 1);
    return subject.operands(es).to(wizard.PLUS2);
  }

  // TODO: Yossi, this should probably be in lisp, but I can't access its source
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

  @Override public String description(final InfixExpression x) {
    return "Append, rather than prepend, \"\", to " + left(x);
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    return !iz.emptyStringLiteral(left(x)) || !iz.infixPlus(x) ? null : replace(x);
  }
}
