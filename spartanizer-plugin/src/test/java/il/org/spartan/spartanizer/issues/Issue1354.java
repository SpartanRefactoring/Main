package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.Block;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.BlockSingletonEliminate;
import il.org.spartan.spartanizer.tipping.Tipper;

/** Tests for {@link BlockSingletonEliminate} see thus numbered github issue for
 * more info
 * @author Niv Shalmon
 * @since 2017-05-25 */
public class Issue1354 extends TipperTest<Block> {
  @Override public Tipper<Block> tipper() {
    return new BlockSingletonEliminate();
  }
  @Override public Class<Block> tipsOn() {
    return Block.class;
  }
  @Test public void test01() {
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
  @Test public void test02() {
    trimmingOf("if (o == null)"//
        + "      for (Element e : data) {"//
        + "        if (e == null)"//
        + "          return $;"//
        + "      }"//
        + "    else"//
        + "      for (Element e : data) {"//
        + "        if (o.equals(e))"//
        + "          return ¢;"//
        + "      }")//
            .stays();
  }
  @Test public void test03() {
    trimmingOf("if (o == null)"//
        + "      while (f()) {"//
        + "        if (e == null)"//
        + "          return $;"//
        + "      }"//
        + "    else"//
        + "      while(g()) {"//
        + "        if (o.equals(e))"//
        + "          return ¢;"//
        + "      }")//
            .stays();
  }
  @Test public void test04() {
    trimmingOf("if (o == null)"//
        + "      while (f()) {"//
        + "        if (e == null)"//
        + "          return $;"//
        + "      }")//
            .gives("if (o == null)"//
                + "      while (f()) "//
                + "        if (e == null)"//
                + "          return $;")//
            .stays();
  }
  @Test public void test05() {
    trimmingOf("if (o == null)      while (f())        for (Element e : data) {         if (e == null)            return $;       }"
        + "    else      while(g()) {        if (o.equals(e))          return ¢;      }")//
            .stays();
  }
}
