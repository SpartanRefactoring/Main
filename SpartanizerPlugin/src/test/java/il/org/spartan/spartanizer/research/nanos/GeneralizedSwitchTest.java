package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class GeneralizedSwitchTest {
  @Ignore // TODO: Yossi Gil
  @Test public void iff() {
    trimmingOf("if(x == 0) d1(); else if(x == 1) d2(); else d3();")//
        .using(IfStatement.class, new GeneralizedSwitch<>())//
        .gives("{holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());}")//
        .gives("holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());")//
        .stays();
  }

  @Ignore // TODO: Yossi Gil
  @Test public void ternary() {
    trimmingOf("d = x == 0 ? d1() : x == 1 ? d2() : d3();")//
        .using(ConditionalExpression.class, new GeneralizedSwitch<>())//
        .gives("d = holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());")//
        .stays();
  }

  @Ignore // TODO: Yossi Gil
  @Test public void ternary2() {
    trimmingOf("d = x(y+19) > a(b) ? d1() : z(w+17) > a(b) ? d2() : d3();")//
        .using(ConditionalExpression.class, new GeneralizedSwitch<>())//
        .gives("{holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());}")//
        .stays();
  }
}
