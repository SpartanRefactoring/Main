package il.org.spartan.refactoring.utils;

import static org.junit.Assert.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.refactoring.engine.*;

/** @author Dor Ma'ayan
 * @since 2016 */
public class RecurserTest {
  @Test public void issue101_1() {
    final Expression simple_exp = into.i("3+4");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(3, (int) recurse.preVisit(accum));
  }

  @Test public void issue101_2() {
    final Expression simple_exp = into.i("3+4");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(3, (int) recurse.postVisit(accum));
  }

  @Test public void issue101_3() {
    final Expression simple_exp = into.i("5*6+43*2");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(7, (int) recurse.preVisit(accum));
  }

  @Test public void issue101_4() {
    final Expression simple_exp = into.i("3+4*4+6*7+8");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(11, (int) recurse.preVisit(accum));
  }

  @Test public void issue101_5() {
    final Expression simple_exp = into.i("3+4*4+6*7+8");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(11, (int) recurse.postVisit(accum));
  }
  
  /* @Test @Ignore("under working") public void issue101_(){ Expression
   * simple_exp = into.i("3+4*4+6*7+8"); Recurser<Integer> recurse = new
   * Recurser<Integer>(simple_exp,0); //Ignore final Consumer<Recurser<Integer>>
   * accum = (x) -> x.=x.getCurrent()+1); //
   * assertEquals(11,(int)recurse.preVisit(accum)); }
   *
   * @Test public void issue101_7(){ Expression simple_exp = into.e("3");
   * Recurser<Integer> recurse = new Recurser<Integer>(simple_exp,0); final
   * Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
   * assertEquals(1,(int)recurse.preVisit(accum)); } */
}
