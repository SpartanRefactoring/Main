package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;

@SuppressWarnings({ "static-method", "javadoc" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    assert haz.sideEffects(e("f()"));
  }

  @Test public void freeFunctionCalla() {
    assert haz.sideEffects(e("i =f()"));
  }

  @Test public void seriesA01() {
    assert !haz.sideEffects(e("a"));
  }

  @Test public void seriesA02() {
    assert !haz.sideEffects(e("this.a"));
  }
}
