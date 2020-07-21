/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Oct 19, 2016 */
package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.engine.parse.*;

import org.junit.*;

import il.org.spartan.spartanizer.java.*;

@SuppressWarnings({ "static-method", "javadoc" })
public final class sideEffectsTest {
  @Test public void deterministicArray1() {
    assert !sideEffects.deterministic(e("new a[3]"));
  }
  @Test public void deterministicArray2() {
    assert !sideEffects.deterministic(e("new int[] {12,13}"));
  }
  @Test public void deterministicArray3() {
    assert !sideEffects.deterministic(e("new int[] {12,13, i++}"));
  }
  @Test public void deterministicArray4() {
    assert !sideEffects.deterministic(e("new int[f()]"));
  }
  @Test public void freeFunctionCall() {
    assert !sideEffects.free(e("f()"));
  }
  @Test public void freeFunctionCalla() {
    assert !sideEffects.free(e("i =f()"));
  }
  @Test public void seriesA01() {
    assert sideEffects.free(e("a"));
  }
  @Test public void seriesA02() {
    assert sideEffects.free(e("this.a"));
  }
}
