/**
 *
 */
package il.org.spartan.collections;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.utils.*;

@SuppressWarnings("static-method") //
public final class IntegersTest extends integers {
  private final INVARIANT invariant = new INVARIANT();

  @Test public void __rehash() {
    add(0, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 11, 12, 13, 14, 15, 16, 17);
    remove(2, 3, 5, 7, 11, 13, 17);
    assert contains(0, 1, 4, 6, 8, 9, 10, 12, 14, 14, 15, 16);
    assert disjoint(2, 3, 5, 7, 11, 13, 17);
    rehash();
    assert contains(0, 1, 4, 6, 8, 9, 10, 12, 14, 14, 15, 16);
    assert disjoint(2, 3, 5, 7, 11, 13, 17);
  }

  @Test public void add() {
    add(1);
    assertEquals(1, size());
    assert contains(1);
    add(1);
    assertEquals(1, size());
    assert contains(1);
    add(2);
    assertEquals(2, size());
    assert contains(1);
    assert contains(2);
    add(2);
    assertEquals(2, size());
    assert contains(1);
    assert contains(2);
    add(1);
    assertEquals(2, size());
    assert contains(1);
    assert contains(2);
    add(1);
    add(2);
    add(3);
    assertEquals(3, size());
    assert contains(1);
    assert contains(2);
    assert contains(3);
    add(4);
    add(2);
    add(2);
    add(1);
    add(3);
    add(3);
    assertEquals(4, size());
    assert contains(1);
    assert contains(2);
    assert contains(3);
    assert contains(4);
  }

  @Test public void addArray() {
    add(1, 2, 3);
    assertEquals(3, size());
    assert contains(1);
    assert contains(2);
    assert contains(3);
    add(new int[] { 4, 1, 2, 3 });
    assertEquals(4, size());
    assert contains(1);
    assert contains(2);
    assert contains(3);
    assert contains(4);
    add(6, 7, 8, 9, 10, 5);
    assertEquals(10, size());
    assert contains(6);
    assert contains(7);
    assert contains(8);
    assert contains(9);
    assert contains(10);
    assert contains(5);
  }

  @Test public void constantSizeRemoveInsert() {
    final int N = 20, M = 100;
    add(Permutation.identity(N));
    for (int i = 0; i < M; i++) {
      invariant.check();
      assertEquals(size(), N);
      final int[] v = entries();
      assertEquals(N, v.length);
      Permutation.shuffle(v);
      assert contains(v);
      assert !disjoint(v);
      Permutation.shuffle(v);
      add(v);
      invariant.check();
      Permutation.shuffle(v);
      assert contains(v);
      assert !disjoint(v);
      Arrays.sort(v);
      assertEquals(v[0], i);
      assertEquals(v[N - 1], i + N - 1);
      remove(i);
      add(i + N);
    }
  }

  @Test public void constructorInitialCapacity() {
    assertEquals(16, new Integers(13).capacity());
  }

  @Test public void constructorInitialCapacitySmallValue() {
    final Integers a = new Integers(1);
    assert null != a;
    assertEquals(0, size());
    assertEquals(Integers.MIN_CAPACITY, capacity());
    assert !contains(0xDEAD);
    assert !contains(1);
    assert !contains(-1);
    assert !contains(0);
  }

  @Test public void constructorNegativeInitialCapacity() {
    assertEquals(Integers.MIN_CAPACITY, new Integers(-1).capacity());
  }

  @Test public void defaultConstructor() {
    final Integers a = new Integers();
    assert null != a;
    assertEquals(0, size());
    assertEquals(Integers.MIN_CAPACITY, capacity());
    assert !contains(0xDEAD);
    assert !contains(1);
    assert !contains(-1);
    assert !contains(0);
  }

  @Test public void massOperations() {
    add(0, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 11, 12, 13, 14, 15, 16, 17);
    assertEquals(18, size());
    assertEquals(32, capacity());
    assert contains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 15, 16, 17);
    remove(2, 3, 5, 7, 11, 13, 17);
    assert disjoint(2, 3, 5, 7, 11, 13, 17);
    remove(0, 2, 4, 6, 8, 10, 12, 14, 16, 18);
    assert disjoint(2, 3, 5, 7, 11, 13, 17);
    assert disjoint(0, 2, 4, 6, 8, 10, 12, 14, 16, 18);
    remove(1, 3, 5, 7, 9, 11, 13, 15, 17);
    assertEquals(0, size());
  }

  @Test(expected = NullPointerException.class) public void nullAdd() {
    add(null);
  }

  @Test(expected = NullPointerException.class) public void nullContains() {
    contains(null);
  }

  @Test(expected = NullPointerException.class) public void nullDisjoint() {
    disjoint(null);
  }

  @Test(expected = NullPointerException.class) public void nullRemove() {
    remove(null);
  }

  @Test public void removals() {
    add(1);
    assertEquals(1, size());
    add(1);
    assertEquals(1, size());
    add(2);
    assertEquals(2, size());
    add(2);
    assertEquals(2, size());
    add(1);
    assertEquals(2, size());
    remove(1);
    assertEquals(1, size());
    remove(2);
    assertEquals(0, size());
    remove(2);
    assertEquals(0, size());
    remove(1);
    assertEquals(0, size());
  }

  @Test public void valuesRange() {
    add(1, 2, 3);
    final int[] values = entries();
    Arrays.sort(values);
    assertEquals(0, Arrays.binarySearch(values, 1));
    assertEquals(1, Arrays.binarySearch(values, 2));
    assertEquals(2, Arrays.binarySearch(values, 3));
  }
}