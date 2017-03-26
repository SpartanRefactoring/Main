package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-17 */
public final class InfixSubtractionSort extends InfixExpressionSortingRest//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = 0x1CF2CD663E8433EDL;

  @Override protected boolean sort( final List<Expression> ¢) {
    return ExpressionComparator.ADDITION.sort(¢);
  }

  @Override protected boolean suitable( final InfixExpression ¢) {
    return in(¢.getOperator(), MINUS2);
  }
}
