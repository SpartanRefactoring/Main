package il.org.spartan.spartanizer.dispatch;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link TipperApplicator}
 * @author Yossi GIl
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc" })
public final class TipperApplicatorTest {
  private static final Class<BlockSimplify> BLOCK_SIMPLIFY = BlockSimplify.class;
  private final TipperApplicator it = new TipperApplicator(new BlockSimplify());

  @Test public void clazzIsCorrect() {
    azzert.that(it.clazz, is(Block.class));
  }

  @Test public void clazzIsNotNull() {
    assert it.clazz != null;
  }

  @Test public void exists() {
    assert it != null;
  }

  @Test public void nameIsCorrect() {
    azzert.that(it.tipper, instanceOf(BLOCK_SIMPLIFY));
  }

  @Test public void tipper() {
    assert it.tipper != null;
  }

  @Test public void tipperIsCorrect() {
    azzert.that(it.tipper, instanceOf(BLOCK_SIMPLIFY));
  }
}
