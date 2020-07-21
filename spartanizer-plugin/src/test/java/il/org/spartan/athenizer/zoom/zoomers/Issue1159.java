package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.zoomers.*;

/** test case for {@link SwitchMissingDefaultAdd}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31 */
@SuppressWarnings("static-method")
public class Issue1159 {
  @Test public void t1() {
    bloatingOf("switch(a){ case 1: switch(b){ default: } }").using(new SwitchMissingDefaultAdd(), SwitchStatement.class)
        .gives("switch(a){ case 1: switch(b){ default: } default: }");
  }
  @Test public void t2() {
    bloatingOf("switch(a){ case 1: y=2; break; default: }").stays();
  }
  @Test public void t8() {
    bloatingOf("switch(++x){}")//
        .gives("switch(++x){default:}");
  }
}
