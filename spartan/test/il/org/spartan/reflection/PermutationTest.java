package il.org.spartan.reflection;

import static il.org.spartan.utils.Permutation.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class PermutationTest {
  @Test public void gold() {
    assertTrue(GOLD < 1);
    assertTrue(GOLD > 0);
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
    final int a[] = Permutation.random(1);
    assertEquals(0, a[0]);
  }

  @Test public void testIsPermutationLarge() {
    final int N = 1000;
    final int a[] = Permutation.random(N);
    Arrays.sort(a);
    for (int i = 0; i < N; ++i)
      assertEquals(i, Arrays.binarySearch(a, i));
  }

  @Test public void testIsRandomPermutation() {
    final int a[] = Permutation.random(10000);
    int count = 0;
    for (int i = 0; i < a.length; i++)
      count += As.binary(i == a[i]);
    System.out.println(count);
    assertFalse(count > a.length / 2);
  }

  @Test public void testPermutation0() {
    final int a[] = Permutation.random(0);
    assertEquals(0, a.length);
  }

  @Test public void testPermutationLength1() {
    final int a[] = Permutation.random(1);
    assertEquals(1, a.length);
  }

  @Test public void testPermutationLength6() {
    final int a[] = Permutation.random(6);
    assertEquals(6, a.length);
  }

  @Test public void testrandom() {
    Permutation.random(5);
  }
}
