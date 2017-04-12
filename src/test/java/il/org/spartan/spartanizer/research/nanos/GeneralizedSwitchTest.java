package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.research.nanos.*;

/** Tests {@link GeneralizedSwitch}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class GeneralizedSwitchTest {
  @Test public void iff() {
    trimminKof("if(x == 0) d1(); else if(x == 1) d2(); else d3();")//
        .using(new GeneralizedSwitch<>(), IfStatement.class)//
        .gives("{holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());}")//
        .gives("holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());") //
        .gives("holds(λ->x==λ).on(0,()->d1()).on(1,()->d2()).elze(()->d3());") //
        .stays();
  }

  @Test public void ternary() {
    trimminKof("d = x == 0 ? d1() : x == 1 ? d2() : d3();")//
        .using(new GeneralizedSwitch<>(), ConditionalExpression.class)//
        .gives("d = holds(¢->x==¢).on(0,()->d1()).on(1,()->d2()).elze(()->d3());")//
        .gives("d=holds(λ->x==λ).on(0,()->d1()).on(1,()->d2()).elze(()->d3());") //
        .stays();
  }

  @Test public void ternary2() {
    trimminKof("d = x(y+19) > a(b) ? d1() : z(w+17) > a(b) ? d2() : d3();")//
        .using(new GeneralizedSwitch<>(), ConditionalExpression.class)//
        .gives("d=holds(¢->¢(y+19)>a(b)).on(x,()->d1()).on(z,()->d2()).elze(()->d3());") //
        .gives("d=holds(λ->¢(y+19)>a(b)).on(x,()->d1()).on(z,()->d2()).elze(()->d3());") //
        .stays();
  }
}
