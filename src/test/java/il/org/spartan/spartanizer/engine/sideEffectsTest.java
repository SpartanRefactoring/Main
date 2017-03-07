/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Oct 19, 2016 */
package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.java.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
