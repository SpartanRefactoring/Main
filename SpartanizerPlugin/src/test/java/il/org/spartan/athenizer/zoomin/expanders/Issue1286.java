package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import java.util.*;
import org.junit.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.meta.*;

/** Test case for {@link StatementExtractParameters}
 * @author Yuval Simon
 * @since 2017-04-29 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1286 {
  @Test public void basic() {
    bloatingOf(new Gives()).givesWithBinding("int f1() {int t1 = x1(x);y = x2(t1);return y;}", "f1");
  }

  @Test public void basic2() {
    bloatingOf(new Gives()).givesWithBinding("void f2() {Integer t1 = Integer.valueOf(1);l.add(t1);}", "f2");
  }

  // Issue #1155 and #1104
  @Test public void bugLambda() {
    bloatingOf(new Stays()).staysWithBinding();
  }

  // Issue #1104
  @Test public void bugAmbiguity() {
    bloatingOf(new Gives())
        .givesWithBinding("void f3() {InfixExpression t1 = az.infixExpression(a);b = copy.of(t1);Assignment.Operator e;e = c.getOperator();}", "f3");
  }

  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings("boxing")
  public static class Gives extends MetaFixture {
    int x = 1;
    int y = 2;
    List<Integer> l = new ArrayList<>();
    ASTNode a, b;
    Assignment c;

    int f1() {
      y = x2(x1(x));
      return y;
    }

    void f2() {
      l.add(Integer.valueOf(1));
    }

    // #1104
    void f3() {
      b = copy.of(az.infixExpression(a));
      c.getOperator();
    }

    private int x1(final int k) {
      return x + k;
    }

    private Integer x2(final Integer k) {
      return x + k;
    }
  }

  /** [[SuppressWarningsSpartan]] */
  public static class Stays extends MetaFixture {
    List<Integer> l = new ArrayList<>();

    // #1155
    void f1() {
      l.forEach(x -> l.add(x));
    }

    // #1104
    void f2() {
      //
    }
  }
}
