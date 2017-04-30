/* TODO orimarco <marcovitch.ori@gmail.com> please add a description
 *
 * @author orimarco <marcovitch.ori@gmail.com>
 *
 * @since Dec 19, 2016 */
package il.org.spartan.spartanizer.ast.navigate;

import org.junit.*;

import nano.ly.*;

@SuppressWarnings("static-method")
public class safeTest {
  @Test public void a() {
    assert safe.div(1, 2) == 0.5;
  }

  @Test public void b() {
    assert safe.div(0, 2) == 0;
  }

  @Test public void c() {
    assert safe.div(17, 0) == 1;
  }
}
