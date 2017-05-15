package il.org.spartan.spartanizer.dispatch;

import static fluent.ly.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** TDD: Unit tests for {@link Tipper#myActualOperandsClass()}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "javadoc", "static-method" })
public final class Issue0214 {
  private static <N extends ASTNode> Class<N> mustBeASTNodeClass(final Class<N> ¢) {
    return ¢;
  }

  private final Tipper<?> blockSimplify = new BlockSimplify();
  private final Tipper<?> tipper = new EagerTipper<ASTNode>() {
    static final long serialVersionUID = 0x6E096C5D5235EC39L;

    @Override public String description() {
      return null;
    }
    @Override public String description(@SuppressWarnings("unused") final ASTNode __) {
      return null;
    }
    @Override public Tip tip(@SuppressWarnings("unused") final ASTNode ¢) {
      return null;
    }
  };

  @Test public void a01_hasFunction() {
    tipper.myActualOperandsClass();
  }
  @Test public void a02_functionIsNotVoid() {
    tipper.myActualOperandsClass();
  }
  @Test public void a03_functionReturnsClass() {
    azzert.that(tipper.myActualOperandsClass(), anyOf(nullValue(), instanceOf(Class.class)));
  }
  @Test public void a04_functionReturnsNulByDeault() {
    azzert.that(tipper.myActualOperandsClass(), is(nullValue()));
  }
  @Test public void a05_simplifyBlockReturnBlock() {
    assert blockSimplify.myActualOperandsClass() != null;
  }
  @Test public void a06_TipperAbstractNonNull() {
    assert new BlockSimplify().getAbstractOperandClass() != null;
  }
  @Test public void a07_BlockSimplifyReturnsSomeClass() {
    azzert.that(blockSimplify.getAbstractOperandClass(), instanceOf(Class.class));
  }
  @Test public void a08_TipperReturnsSomeASTNode() {
    azzert.that(tipper.getAbstractOperandClass(), is(ASTNode.class));
  }
  @Test public void a09_TipperReturnsReasonableValue() {
    azzert.that(tipper.getAbstractOperandClass().getClass(), is(Class.class));
  }
  @Test public void a10_TipperReturnsCorrectStaticType() {
    azzert.that(mustBeASTNodeClass(tipper.getAbstractOperandClass()), is(tipper.getAbstractOperandClass()));
  }
  @Test public void a11_TipperReturnsCorrectValueBlockSimplify() {
    azzert.that(blockSimplify.getAbstractOperandClass(), is(Block.class));
  }
  @Test public void a12_TipperReturnsCorrectConcreteValueBlockSimplify() {
    azzert.that(blockSimplify.myActualOperandsClass(), is(Block.class));
  }
}
