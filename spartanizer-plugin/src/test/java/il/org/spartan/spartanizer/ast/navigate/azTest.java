package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.is;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.safety.az;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Oct 7, 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class azTest {
  @Test public void asComparisonPrefixlExpression() {
    final PrefixExpression p = mock(PrefixExpression.class);
    doReturn(PrefixExpression.Operator.NOT).when(p).getOperator();
    azzert.isNull(az.comparison(p));
  }
  @Test public void asComparisonTypicalExpression() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    assert az.comparison(i) != null;
  }
  @Test public void asComparisonTypicalExpressionFalse() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(CONDITIONAL_OR).when(i).getOperator();
    azzert.isNull(az.comparison(i));
  }
  @Test public void asComparisonTypicalInfixFalse() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(CONDITIONAL_AND).when(i).getOperator();
    azzert.isNull(az.comparison(i));
  }
  @Test public void asComparisonTypicalInfixIsCorrect() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    azzert.that(az.comparison(i), is(i));
  }
  @Test public void asComparisonTypicalInfixIsNonNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    assert az.comparison(e) != null;
  }
}
