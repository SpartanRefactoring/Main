package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit test for {@link DeclarationInitializerStatementTerminatingScope} this
 * test tests whether the bug mentioned in Issue 711 had been fixed
 * @author Dan Abramovich
 * @since 22-11-2016 */
@SuppressWarnings("static-method")
public class Issue711 {
  @Test public void test0() {
    trimmingOf("int oneLarger(int x) {" + "Function<Integer, Integer> f = i -> i + 1;" + "return f.eval(x);" + "}").stays();
  }

  @Test public void test1() {
    trimmingOf("Consumer<Integer> x = (i->i+1); " + "x.accept(6);").stays();
  }
}
