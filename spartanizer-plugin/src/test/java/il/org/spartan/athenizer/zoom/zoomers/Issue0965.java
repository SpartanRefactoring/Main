package il.org.spartan.athenizer.zoom.zoomers;

import java.util.List;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Test;

import il.org.spartan.athenizer.zoomers.ToStringExpander;
import il.org.spartan.spartanizer.meta.MetaFixture;
import il.org.spartan.spartanizer.testing.BloaterTest;
import il.org.spartan.spartanizer.tipping.Tipper;

/** Test class for issue #965 Unit test for {@link ToStringExpander}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-12-20 */
public class Issue0965 extends BloaterTest<InfixExpression> {
  @Override public Tipper<InfixExpression> bloater() {
    return new ToStringExpander();
  }
  @Override public Class<InfixExpression> tipsOn() {
    return InfixExpression.class;
  }
  @Test public void test0() {
    bloatingOf(new TestClass()) //
        .givesWithBinding(" public String check1(){return is.toString();}", "check1")//
        .staysWithBinding();
  }

  static class TestClass extends MetaFixture {
    final List<Integer> is = an.empty.list();

    public String check1() {
      return is + "";
    }
    public void check2() {
      //
    }
    public void check3() {
      //
    }
  }
}
