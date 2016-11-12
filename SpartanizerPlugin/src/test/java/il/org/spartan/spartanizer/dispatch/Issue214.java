package il.org.spartan.spartanizer.dispatch;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** TDD: Unit tests for {@link Tipper#myActualOperandsClass()}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "javadoc", "static-method" }) public final class Issue214 {
  private static <N extends ASTNode> Class<N> mustBeASTNodeClass(final Class<N> ¢) {
    return ¢;
  }

  private final Tipper<?> blockSimplify = new BlockSimplify();
  private final Tipper<?> tipper = new EagerTipper<ASTNode>() {
    @Override public String description() {
      return null;
    }
    @Override public String description(@SuppressWarnings("unused") final ASTNode __) {
      return null;
    }
  };

  @Test public void A01_description() {
    azzert.isNull(tipper.description());
  }
  @Test public void A01_descriptionWithParameter() {
    azzert.isNull(tipper.description(null));
  }
  @Test public void A01_hasFunction() {
    tipper.myActualOperandsClass();
  }
  @Test public void A02_functionIsNotVoid() {
    tipper.myActualOperandsClass();
  }
  @Test public void A03_functionReturnsClass() {
    azzert.that(tipper.myActualOperandsClass(), anyOf(nullValue(), instanceOf(Class.class)));
  }
  @Test public void A04_functionReturnsNulByDeault() {
    azzert.that(tipper.myActualOperandsClass(), is(nullValue()));
  }
  @Test public void A05_simplifyBlockReturnBlock() {
    azzert.notNull(blockSimplify.myActualOperandsClass());
  }
  @Test public void A06_TipperAbstractNotNull() {
    azzert.notNull(new BlockSimplify().myAbstractOperandsClass());
  }
  @Test public void A07_BlockSimplifyReturnsSomeClass() {
    azzert.that(blockSimplify.myAbstractOperandsClass(), instanceOf(Class.class));
  }
  @Test public void A08_TipperReturnsSomeASTNode() {
    azzert.that(tipper.myAbstractOperandsClass(), is(ASTNode.class));
  }
  @Test public void A09_TipperReturnsReasonableValue() {
    azzert.that(tipper.myAbstractOperandsClass().getClass(), is(Class.class));
  }
  @Test public void A10_TipperReturnsCorrectStaticType() {
    azzert.that(mustBeASTNodeClass(tipper.myAbstractOperandsClass()), is(tipper.myAbstractOperandsClass()));
  }
  @Test public void A11_TipperReturnsCorrectValueBlockSimplify() {
    azzert.that(blockSimplify.myAbstractOperandsClass(), is(Block.class));
  }
  @Test public void A12_TipperReturnsCorrectConcreteValueBlockSimplify() {
    azzert.that(blockSimplify.myActualOperandsClass(), is(Block.class));
  }
  @Test public void A13_TipperReturnsCorrectConcreteValueAssignmentAssignment() {
    azzert.that(new AssignmentAndAssignment().myActualOperandsClass(), is(Assignment.class));
  }
  @Test public void A14_TipperReturnsCorrectConcreteValueIfStatement() {
    azzert.that(new IfAssignToFooElseAssignToFoo().myActualOperandsClass(), is(IfStatement.class));
  }
}
