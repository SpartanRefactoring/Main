package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** Tests {@link Tip} 
 * 
 * @author Yossi Gil
 * @since 2017-04-11 */
  @SuppressWarnings("static-method")
public class TipTest {
  @Test public void a() {
    assert Tip.overlap(new Range(51, 64), new Range(51, 64));
  }
  @Test public void b() {
    assert Tip.overlap(new Range(51, 64), new Range(52, 64));
  }
  @Test public void c() {
    assert !Tip.overlap(new Range(0, 1), new Range(1, 2));
  }
  @Test public void d() {
    assert !Tip.overlap(new Range(0, 1), new Range(1, 2));
  }
}
