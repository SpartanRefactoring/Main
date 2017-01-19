package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class GeneralizedSwitchTest {
  @Test public void iff() {
    trimmingOf("if(x == 0) d1(); else if(x == 1) d2(); else d3();")//
        .using(IfStatement.class, new GeneralizedSwitch<>())//
        .gives("{holds(λ->x==λ).on(0,()->d1()).on(1,()->d2()).elze(()->d3());}")//
        .gives("holds(λ->x==λ).on(0,()->d1()).on(1,()->d2()).elze(()->d3());")//
        .stays();
  }

  @Test public void ternary() {
    trimmingOf("d = x == 0 ? d1() : x == 1 ? d2() : d3();")//
        .using(ConditionalExpression.class, new GeneralizedSwitch<>())//
        .gives("d = holds(λ->x==λ).on(0,()->d1()).on(1,()->d2()).elze(()->d3());")//
        .stays();
  }

  @Test public void ternary2() {
    trimmingOf("d = x(y+19) > a(b) ? d1() : z(w+17) > a(b) ? d2() : d3();")//
        .using(ConditionalExpression.class, new GeneralizedSwitch<>())//
        .gives("d = holds(λ-> λ > a(b)).on(()->x(y+19),()->d1()).on(()->z(w+17),()->d2()).elze(()->d3());")//
        .stays();
  }
}
