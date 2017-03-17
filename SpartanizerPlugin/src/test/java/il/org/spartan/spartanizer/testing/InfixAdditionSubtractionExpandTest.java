package il.org.spartan.spartanizer.testing;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

/**
 * Test class for InfixAdditionSubtractionExpand
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-03-16
 */
public class InfixAdditionSubtractionExpandTest {
  private static final String input = "1+a*b+2+b*c+3+d*e+4";
  private static final String input2 = "1+a*b+2+ (b*c+3) +d*e+4";
  private static final InfixExpression INPUT = into.i(input);
  private final String s = "365 * a + a / 4 - a / 100 + a / 400 + (b * 306 + 5) / 10 + c - 1";
  
  @Test
  @SuppressWarnings("static-method") public void test01() {
    assert !new InfixAdditionSubtractionExpand().check(INPUT);
  }

  @Test
  @SuppressWarnings("static-method") public void test01b() {
    assert new InfixAdditionSubtractionExpand().check(into.i(tide.clean(input2)));
  }
  
  @Test
  @SuppressWarnings("static-method") public void test01b1() {
    assert new InfixAdditionSubtractionExpand().check(into.i(input2));
  }  

  @Test public void test01c() {
    assert !new InfixAdditionSubtractionExpand().check(into.i(s));
  }

  @Test
  @SuppressWarnings("static-method") public void test01d() {
    assert new InfixAdditionSubtractionExpand().check(into.i("a - (b+c)"));
  }
  
  @Test
  @SuppressWarnings("static-method") public void test01d2() {
    trimmingOf("a - (b+c)")//
      .gives("a - b -c")
      .stays();
  }
  
}
