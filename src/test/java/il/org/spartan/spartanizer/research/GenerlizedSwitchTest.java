package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class GenerlizedSwitchTest {
  @Test public void basic() {
    trimmingOf("if(x == 0) d1(); else if(x == 1) d2(); else d3();")//
        .withTipper(IfStatement.class, new GeneralizedSwitch())//
        .gives("{holds(λ->x==λ).on(0,()->{d1();}).on(1,()->{d2();}).elze(()->{d3();});}")//
        .gives("holds(λ->x==λ).on(0,()->{d1();}).on(1,()->{d2();}).elze(()->{d3();});")//
        .gives("holds(λ->x==λ).on(0,()->d1()).on(1,()->d2()).elze(()->d3());")//
        .stays();
  }
}
