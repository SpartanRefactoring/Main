/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings("static-method") //
public final class makeTest {
  @Test public void issue72me4xA() {
    azzert.that(make.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xB() {
    azzert.that(make.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xC() {
    azzert.that(make.minus(into.e("+x")), iz("-x"));
  }

  @Test public void issue72me4xD() {
    azzert.that(make.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xF() {
    azzert.that(make.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xG() {
    azzert.that(make.minus(into.e("+x")), iz("-x"));
  }
}
