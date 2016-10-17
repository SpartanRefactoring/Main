package il.org.spartan.reflection;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Permutation.*;

import java.util.*;

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
    final int a[] = Permutation.random(1);
    azzert.that(a[0], is(0));
  }

  @Test public void testIsPermutationLarge() {
    final int N = 1000;
    final int a[] = Permutation.random(N);
    Arrays.sort(a);
    for (int i = 0; i < N; ++i) {
      final int t1 = i;
      azzert.that(Arrays.binarySearch(a, i), is(t1));
    }
  }

  @Test public void testIsRandomPermutation() {
    final int a[] = Permutation.random(10000);
    int count = 0;
    for (int ¢ = 0; ¢ < a.length; ++¢)
      count += As.binary(¢ == a[¢]);
    System.out.println(count);
    assert !(count > a.length / 2);
  }

  @Test public void testPermutation0() {
    final int a[] = Permutation.random(0);
    azzert.that(a.length, is(0));
  }

  @Test public void testPermutationLength1() {
    final int a[] = Permutation.random(1);
    azzert.that(a.length, is(1));
  }

  @Test public void testPermutationLength6() {
    final int a[] = Permutation.random(6);
    azzert.that(a.length, is(6));
  }

  @Test public void testrandom() {
    Permutation.random(5);
  }
}
