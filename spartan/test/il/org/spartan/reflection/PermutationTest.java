package il.org.spartan.reflection;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Permutation.*;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class PermutationTest {
  @Test public void gold() {
    assert GOLD < 1;
    assert GOLD > 0;
  }

  @Test public void s() {
    System.err.println(scramble(0));
  }

  @Test public void s4() {
    System.err.println(Separate.by(scramble(20), ","));
    System.err.println(Separate.by(scramble(50), ","));
    System.err.println(Separate.by(scramble(75), ","));
    System.err.println(Separate.by(scramble(100), ","));
  }

  @Test public void scrambleSize() {
    azzert.that(scramble(100).length, is(100));
  }

  @Test public void testIsPermutation1() {
    azzert.that(Permutation.random(1)[0], is(0));
  }

  @Test public void testIsPermutationLarge() {
    final int N = 1000;
    @NotNull final int a[] = Permutation.random(N);
    Arrays.sort(a);
    for (int ¢ = 0; ¢ < N; ++¢)
      azzert.that(Arrays.binarySearch(a, ¢), is(¢));
  }

  @Test public void testIsRandomPermutation() {
    @NotNull final int a[] = Permutation.random(10000);
    int count = 0;
    for (int ¢ = 0; ¢ < a.length; ++¢)
      count += as.bit(¢ == a[¢]);
    System.out.println(count);
    assert count <= a.length / 2;
  }

  @Test public void testPermutation0() {
    azzert.that(Permutation.random(0).length, is(0));
  }

  @Test public void testPermutationLength1() {
    azzert.that(Permutation.random(1).length, is(1));
  }

  @Test public void testPermutationLength6() {
    azzert.that(Permutation.random(6).length, is(6));
  }

  @Test public void testrandom() {
    Permutation.random(5);
  }
}
