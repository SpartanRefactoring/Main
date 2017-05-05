package il.org.spartan.spartanizer.engine;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** Tests {@link Ranger}
 * @author Yossi Gil
 * @since 2017-04-11 */
@SuppressWarnings("static-method")
public class RangerTest {
  @Test public void a() {
    assert Ranger.overlap(new Range(51, 64), new Range(51, 64));
  }
  @Test public void b() {
    assert Ranger.overlap(new Range(51, 64), new Range(52, 64));
  }
  @Test public void c() {
    assert !Ranger.overlap(new Range(0, 1), new Range(1, 2));
  }
  @Test public void d() {
    assert !Ranger.overlap(new Range(0, 1), new Range(1, 2));
  }
}
