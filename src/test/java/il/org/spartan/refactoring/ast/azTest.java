package il.org.spartan.refactoring.ast;

import static il.org.spartan.azzert.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class azTest {
  @Test public void asComparisonPrefixlExpression() {
    final PrefixExpression p = mock(PrefixExpression.class);
    doReturn(PrefixExpression.Operator.NOT).when(p).getOperator();
    azzert.isNull(az.comparison(p));
  }

  @Test public void asComparisonTypicalExpression() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    azzert.notNull(az.comparison(i));
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
    assertEquals(i, az.comparison(i));
  }

  @Test public void asComparisonTypicalInfixIsNotNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    azzert.notNull(az.comparison(e));
  }
}
