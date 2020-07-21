/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.factory;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings("static-method") //
public final class makeTest {
  @Test public void issue72me4xA() {
    azzert.that(cons.minus(parse.e("-x")), iz("x"));
  }
  @Test public void issue72me4xB() {
    azzert.that(cons.minus(parse.e("x")), iz("-x"));
  }
  @Test public void issue72me4xC() {
    azzert.that(cons.minus(parse.e("+x")), iz("-x"));
  }
  @Test public void issue72me4xD() {
    azzert.that(cons.minus(parse.e("-x")), iz("x"));
  }
  @Test public void issue72me4xF() {
    azzert.that(cons.minus(parse.e("x")), iz("-x"));
  }
  @Test public void issue72me4xG() {
    azzert.that(cons.minus(parse.e("+x")), iz("-x"));
  }
}
