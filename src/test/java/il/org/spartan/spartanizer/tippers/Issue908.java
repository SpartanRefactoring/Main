package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link InitializationListRemoveComma} of previously failed tests. 
 * Related to Issue074 and {@link Version230}. 
 * @author Yuval Simon
 * @since 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue908 {
  @Test public void issue74d() {
    trimmingOf("int[] a = new int[] {2,3};")//
        .gives("");
  }
}
