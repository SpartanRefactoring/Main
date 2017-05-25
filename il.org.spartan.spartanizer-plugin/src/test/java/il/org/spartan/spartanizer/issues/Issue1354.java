package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tests for {@link BlockSingletonEliminate} see thus numbered github issue for
 * more info
 * 
 * @author Niv Shalmon
 * @since 2017-05-25 */
public class Issue1354 extends TipperTest<Block> {
  @Override public Tipper<Block> tipper() {
    return new BlockSingletonEliminate();
  }
  @Override public Class<Block> tipsOn() {
    return Block.class;
  }
  @Ignore @Test public void test01() {
    trimmingOf("if (o == null)"//
        + "      for (int $ = 0; $ < size(); ++$) {"//
        + "        if (data[$] == null)"//
        + "          return $;"//
        + "      }"//
        + "    else"//
        + "      for (int ¢ = 0; ¢ < size(); ++¢) {"//
        + "        if (o.equals(data[¢]))"//
        + "          return ¢;"//
        + "      }")//
            .stays();
  }
}
