package il.org.spartan.spartanizer.tippers;
import static fluent.ly.is.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixAdditionSort extends InfixExpressionSortingFull//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -0x5C6B08A93E9B3EC8L;

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.ADDITION.sort(¢);
  }

  @Override protected boolean suitable(final InfixExpression ¢) {
    return in(¢.getOperator(), il.org.spartan.spartanizer.ast.navigate.op.PLUS2) && type.isNotString(¢);
  }
}
