package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link EliminateEmptyTryBlock} of previously failed
 * tests. Related to {@link Issue234}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0909 {
  @Test public void b$03() {
    trimmingOf("int a; try { } catch(Exception e) { return -1; }").gives("int a;");
  }
}
