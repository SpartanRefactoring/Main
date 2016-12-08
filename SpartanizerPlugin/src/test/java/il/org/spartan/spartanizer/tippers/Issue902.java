package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
/** This is a unit test for {@link RedundentReturnStatementInVoidTypeMethod} of previously failed tests. 
 * Related to Issue879. 
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
@Ignore
public class Issue902 {
  @Test public void test1() {
    trimmingOf("void f(){int x; int y;return;}")//
    .gives("void f(){int x; int y;}")//
    .gives("void f(){}")//
    .stays();
  }
}
