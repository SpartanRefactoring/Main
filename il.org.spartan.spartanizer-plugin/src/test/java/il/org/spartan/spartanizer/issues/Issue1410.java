package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tests for {@link BlockSingletonEliminate} see thus numbered github issue for
 * more info
 * @author Niv Shalmon
 * @since 2017-05-25 */
public class Issue1410 extends TipperTest<Block> {
  @Override public Tipper<Block> tipper() {
    return new BlockSingletonEliminate();
  }
  @Override public Class<Block> tipsOn() {
    return Block.class;
  }
  @Test public void test01() {
    trimmingOf("int size = list.size();" //
        + "    if (element == null) {"//
        + "      for (int ¢ = 0; ¢ < size; ++¢)"//
        + "    if (list.get(¢) == null)"//
        + "      return ¢;"//
        + "    } else"//
        + "    for (int ¢ = 0; ¢ < size; ++¢)"//
        + "      if (element.equals(list.get(¢)))"//
        + "        return ¢;"//
        + "    return -1;"//
        + "  }")//
            .stays();
  }
  @Test public void test02() {
    trimmingOf("int size = list.size();" //
        + "    if (element == null) {"//
        + "      while (f(¢++))"//
        + "    if (list.get(¢) == null)"//
        + "      return ¢;"//
        + "    } else"//
        + "    for (int ¢ = 0; ¢ < size; ++¢)"//
        + "      if (element.equals(list.get(¢)))"//
        + "        return ¢;"//
        + "    return -1;"//
        + "  }")//
            .stays();
  }
  @Test public void test03() {
    trimmingOf("int size = list.size();" //
        + "    if (element == null) {"//
        + "      for (Object e : list)"//
        + "    if (e == null)"//
        + "      return ¢;"//
        + "    } else"//
        + "    for (Object e : list)"//
        + "      if (element.equals(e))"//
        + "        return ¢;"//
        + "    return -1;"//
        + "  }")//
            .stays();
  }
  @Test public void test04(){
    trimmingOf("int size = list.size();" //
        + "    if (element == null) {"//
        + "      for (Object e : list)"//
        + "        if (e == null)"//
        + "         return ¢;"
        + "        else"
        + "         return a;"//
        + "    } else"//
        + "       for (Object e : list)"//
        + "          if (element.equals(e))"//
        + "            return ¢;"//
        + "    return -1;"//
        + "  }")//
    .gives("int size = list.size();" //
        + "    if (element == null) "//
        + "      for (Object e : list)"//
        + "         if (e == null)"//
        + "           return ¢;"
        + "         else"
        + "           return a;"//
        + "    else"//
        + "     for (Object e : list)"//
        + "        if (element.equals(e))"//
        + "          return ¢;"//
        + "    return -1;"//
        + "  }");
  }
}
