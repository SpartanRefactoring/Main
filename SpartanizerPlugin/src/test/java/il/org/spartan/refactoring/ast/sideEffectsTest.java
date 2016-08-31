package il.org.spartan.refactoring.ast;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.java.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class sideEffectsTest {
  @Test public void deterministicArray1() {
    azzert.that(sideEffects.deterministic(e("new a[3]")), is(false));
  }

  @Test public void deterministicArray2() {
    azzert.that(sideEffects.deterministic(e("new int[] {12,13}")), is(false));
  }

  @Test public void deterministicArray3() {
    azzert.that(sideEffects.deterministic(e("new int[] {12,13, i++}")), is(false));
  }

  @Test public void deterministicArray4() {
    azzert.that(sideEffects.deterministic(e("new int[f()]")), is(false));
  }
}
