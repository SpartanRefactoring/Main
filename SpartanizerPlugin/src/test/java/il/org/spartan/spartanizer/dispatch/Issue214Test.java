package il.org.spartan.spartanizer.dispatch;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.wringing.*;
import il.org.spartan.spartanizer.wrings.*;

/** TDD: Unit tests for {@link Wring#myActualOperandsClass()}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public final class Issue214Test {
  private static <N extends ASTNode> Class<N> mustBeASTNodeClass(final Class<N> ¢) {
    return ¢;
  }

  private final Wring<?> blockSimplify = new BlockSimplify();
  private final Wring<?> wring = new Wring<ASTNode>() {
    @Override public String description() {
      return null;
    }

    @Override protected String description(@SuppressWarnings("unused") final ASTNode __) {
      return null;
    }
  };

  @Test public void A01_hasFunction() {
    wring.myActualOperandsClass();
  }

  @Test public void A02_functionIsNotVoid() {
    wring.myActualOperandsClass();
  }

  @Test public void A03_functionReturnsClass() {
    azzert.that(wring.myActualOperandsClass(), anyOf(nullValue(), instanceOf(Class.class)));
  }

  @Test public void A04_functionReturnsNulByDeault() {
    azzert.that(wring.myActualOperandsClass(), is(nullValue()));
  }

  @Test public void A05_simplifyBlockReturnBlock() {
    azzert.notNull(blockSimplify.myActualOperandsClass());
  }

  @Test public void A06_WringAbstractNotNull() {
    azzert.notNull(new BlockSimplify().myAbstractOperandsClass());
  }

  @Test public void A07_BlockSimplifyReturnsSomeClass() {
    azzert.that(blockSimplify.myAbstractOperandsClass(), instanceOf(Class.class));
  }

  @Test public void A08_WringReturnsSomeASTNode() {
    azzert.that(wring.myAbstractOperandsClass(), is(ASTNode.class));
  }

  @Test public void A09_WringReturnsReasonableValue() {
    azzert.that(wring.myAbstractOperandsClass().getClass(), is(Class.class));
  }

  @Test public void A10_WringReturnsCorrectStaticType() {
    azzert.that(mustBeASTNodeClass(wring.myAbstractOperandsClass()), is(wring.myAbstractOperandsClass()));
  }

  @Test public void A11_WringReturnsCorrectValueBlockSimplify() {
    azzert.that(blockSimplify.myAbstractOperandsClass(), is(Block.class));
  }

  @Test public void A12_WringReturnsCorrectConcreteValueBlockSimplify() {
    azzert.that(blockSimplify.myActualOperandsClass(), is(Block.class));
  }

  @Test public void A13_WringReturnsCorrectConcreteValueAssignmentAssignment() {
    azzert.that(new AssignmentAndAssignment().myActualOperandsClass(), is(Assignment.class));
  }

  @Test public void A14_WringReturnsCorrectConcreteValueIfStatement() {
    azzert.that(new IfAssignToFooElseAssignToFoo().myActualOperandsClass(), is(IfStatement.class));
  }
}
