package il.org.spartan.refactoring.utils;

import static org.junit.Assert.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.refactoring.engine.*;

/** @author Dor Ma'ayan
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc", "boxing" }) 
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
    assertEquals(9, (int) recurse.preVisit(accum));
  }

  @Test public void issue101_5() {
    final Expression simple_exp = into.i("3+4*4+6*7+8");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(9, (int) recurse.postVisit(accum));
  }

  @Test public void issue101_6() {
    final Expression simple_exp = into.i("3+4+5+6");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(5, (int) recurse.postVisit(accum));
  }

  @Test public void issue101_7() {
    final Expression simple_exp = into.e("a==4 ? 34 : 56");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(6, (int) recurse.postVisit(accum));
  }

  @Test public void issue101_8() {
    final Expression simple_exp = into.e("a==4 ? 34 : 56+34");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(8, (int) recurse.preVisit(accum));
  }

  @Test public void issue101_9() {
    final Expression simple_exp = into.e("56");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99", simple_exp.toString());
  }

  @Test @Ignore("Seems like working value/refrence problem") public void issue101_10() {
    final Expression simple_exp = into.e("56+87");
    final Recurser<Integer> recurse = new Recurser<>(simple_exp, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99+99", simple_exp.toString());
  }
}
