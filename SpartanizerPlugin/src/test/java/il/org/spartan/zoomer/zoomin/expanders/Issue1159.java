package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** test case for {@link SwitchAddDefault}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31 */
// @Ignore
@SuppressWarnings("static-method")
public class Issue1159 {
  @Test public void t1() {
    bloatingOf("switch(a){ case 1: switch(b){ default: } }").using(SwitchStatement.class, new SwitchAddDefault())
        .gives("switch(a){ case 1: switch(b){ default: } default: }");
  }
}
