package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class DelegatorTest {
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadocedDelegator(final String ¢) {
    return spartanized(¢).contains("[[Delegator]]");
  }

  /** @param s */
  private static void notDelegator(final String ¢) {
    assert !javadocedDelegator(¢);
  }

  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, new Delegator());
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  @Test public void basic() {
    delegator("public class A{boolean foo(){return bar();} }");
  }

  @Test public void basic2() {
    delegator("public class A{boolean foo(int a){return bar(a);} }");
  }

  @Test public void basic3() {
    delegator("public class A{boolean foo(int a){return bar(a,a);} }");
  }

  @Test public void basic4() {
    delegator("public class A{boolean foo(int a){return bar(a,f(a));} }");
  }

  @Test public void basic5() {
    notDelegator("public class A{boolean foo(int a){if(a == null) return bar(a);} }");
  }

  @Test public void basic6() {
    notDelegator("public class A{boolean foo(int a){return bar(a,b);} }");
  }

  @Test public void basic7() {
    notDelegator("public class A{boolean foo(int a){return bar(a,f(b));} }");
  }

  @Test public void basic8() {
    notDelegator("public class A{boolean foo(int a){return bar(a,f(f(b)));} }");
  }

  @Test public void basic9() {
    notDelegator("public class A{void foo(int a){return bar(a);} }");
  }

  private void delegator(final String ¢) {
    assert javadocedDelegator(¢);
  }
}
