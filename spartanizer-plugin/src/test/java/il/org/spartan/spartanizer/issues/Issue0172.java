package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.InfixAdditionSort;
import il.org.spartan.spartanizer.tippers.InfixMultiplicationByOne;
import il.org.spartan.spartanizer.tippers.InfixPlusEmptyString;
import il.org.spartan.spartanizer.tippers.InfixTermsZero;

/** Unit tests for {@link InfixTermsZero}
 * @author Niv Shalmon
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue0172 {
  @Test public void a01() {
    trimmingOf("1+3*x+0")//
        .gives("1+3*x");
  }
  @Test public void a02() {
    trimmingOf("1+3*x+0+\"\"")//
        .gives("1+3*x+\"\"");
  }
  @Test public void a03() {
    trimmingOf("0+x+\"\"")//
        .stays();
  }
  @Test public void a04() {
    trimmingOf("2+1*x+0+\"abc\"+\"\"")//
        .gives("2+1*x+0+\"abc\"") //
        .gives("2+1*x+\"abc\"")//
        .gives("1*x+2+\"abc\"")//
        .gives("x+2+\"abc\"")//
        .stays();
  }
  /** Introduced by Yogi on Tue-Apr-11-13:20:11-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void a0abc() {
    trimmingOf("2 + 1 * a + 0 + \"abc\" + \"\"") //
        .using(new InfixPlusEmptyString(), InfixExpression.class) //
        .gives("2+1*a+0+\"abc\"") //
        .using(new InfixTermsZero(), InfixExpression.class) //
        .gives("2+1*a+\"abc\"") //
        .using(new InfixAdditionSort(), InfixExpression.class) //
        .gives("1*a+2+\"abc\"") //
        .using(new InfixMultiplicationByOne(), InfixExpression.class) //
        .gives("a+2+\"abc\"") //
        .stays() //
    ;
  }
  @Test public void a05() {
    trimmingOf("x+\"\"+\"abc\"+0")//
        .gives("x+\"abc\"+0")//
        .stays();
  }
  @Test public void a06() {
    trimmingOf("0 + \"\"")//
        .stays();
  }
  @Test public void a07() {
    trimmingOf("\"\" + 0")//
        .gives("0+\"\"")//
        .stays();
  }
  @Test public void a08() {
    trimmingOf("\"\" + 0 + 1")//
        .gives("0+ \"\" + 1")//
        .stays();
  }
  @Test public void a09() {
    trimmingOf("x+1+0")//
        .gives("x+1")//
        .stays();
  }
  @Test public void a10() {
    trimmingOf("0+x+1")//
        .stays();
  }
}
