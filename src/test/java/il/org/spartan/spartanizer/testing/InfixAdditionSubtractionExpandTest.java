package il.org.spartan.spartanizer.testing;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;

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
  @SuppressWarnings("static-method") public void test02() {
    assert new InfixAdditionSubtractionExpand().check(into.i(tide.clean(input2)));
  }
  
  @Test
  @SuppressWarnings("static-method")
  public void test03() {
    assert new InfixAdditionSubtractionExpand().check(into.i(input2));
  }  

  @Test public void test04() {
    assert !new InfixAdditionSubtractionExpand().check(into.i(s));
  }

  @Test
  @SuppressWarnings("static-method")
  public void test05() {
    assert new InfixAdditionSubtractionExpand().check(into.i("a - (b+c)"));
  }
  
  @Test
  @SuppressWarnings("static-method") 
  public void test06() {
    trimmingOf("a - (b+c)")//
      .gives("a - b -c")
      .stays();
  }
  
  @Test
  @SuppressWarnings("static-method") 
  public void test07() {
    trimmingOf("1 - (a+b)")//
      .gives("1 - a - b")
      .stays();
  }
  // TODO: Matteo (for himself) this is not working properly
  @Test
  @SuppressWarnings("static-method") 
  public void test08() {
    trimmingOf("1 - (a+1)")//
      .gives("1 - a - 1")
      .stays();
  }
  
  @Test
  @SuppressWarnings("static-method") 
  public void test09() {
    trimmingOf("1 - (a+c)")//
      .gives("1 - a - c")
      .stays();
  }
}
