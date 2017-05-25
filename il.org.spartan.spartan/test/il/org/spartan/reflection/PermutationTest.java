package il.org.spartan.reflection;

import static il.org.spartan.utils.Permutation.*;
import static org.junit.Assert.*;

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
    assertEquals(100, scramble(100).length);
  }

  @Test public void testIsPermutation1() {
    assertEquals(0, Permutation.random(1)[0]);
  }

  @Test public void testIsPermutationLarge() {
    final int N = 1000, a[] = Permutation.random(N);
    Arrays.sort(a);
    for (int ¢ = 0; ¢ < N; ++¢)
      assertEquals(¢, Arrays.binarySearch(a, ¢));
  }

  @Test public void testIsRandomPermutation() {
    final int a[] = Permutation.random(10000);
    int count = 0;
    for (int ¢ = 0; ¢ < a.length; ++¢)
      count += as.bit(¢ == a[¢]);
    System.out.println(count);
    assert count <= a.length / 2;
  }

  @Test public void testPermutation0() {
    assertEquals(0, Permutation.random(0).length);
  }

  @Test public void testPermutationLength1() {
    assertEquals(1, Permutation.random(1).length);
  }

  @Test public void testPermutationLength6() {
    assertEquals(6, Permutation.random(6).length);
  }

  @Test public void testrandom() {
    Permutation.random(5);
  }
}
