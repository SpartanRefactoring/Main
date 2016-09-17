package il.org.spartan.spartanizer.dispatch;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.wrings.*;

/** Unit tests for {@link WringApplicator}
 * @author Yossi GIl
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc" }) //
public class WringApplicatorTest {
  private static final Class<BlockSimplify> BLOCK_SIMPLIFY = BlockSimplify.class;
  private final WringApplicator it = new WringApplicator(new BlockSimplify());

  @Test public void clazzIsCorrect() {
    azzert.that(it.clazz, is(Block.class));
  }

  @Test public void clazzIsNotNull() {
    azzert.notNull(it.clazz);
  }

  @Test public void exists() {
    azzert.notNull(it);
  }

  @Test public void nameIsCorrect() {
    azzert.that(it.wring, instanceOf(BLOCK_SIMPLIFY));
  }

  @Test public void wring() {
    azzert.notNull(it.wring);
  }

  @Test public void wringIsCorrect() {
    azzert.that(it.wring, instanceOf(BLOCK_SIMPLIFY));
  }
}
