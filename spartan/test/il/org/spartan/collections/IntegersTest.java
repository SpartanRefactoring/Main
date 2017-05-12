package il.org.spartan.collections;

import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") //
public final class IntegersTest extends Integers {
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
    azzert.that(size(), is(1));
    assert contains(1);
    add(1);
    azzert.that(size(), is(1));
    assert contains(1);
    add(2);
    azzert.that(size(), is(2));
    assert contains(1);
    assert contains(2);
    add(2);
    azzert.that(size(), is(2));
    assert contains(1);
    assert contains(2);
    add(1);
    azzert.that(size(), is(2));
    assert contains(1);
    assert contains(2);
    add(1);
    add(2);
    add(3);
    azzert.that(size(), is(3));
    assert contains(1);
    assert contains(2);
    assert contains(3);
    add(4);
    add(2);
    add(2);
    add(1);
    add(3);
    add(3);
    azzert.that(size(), is(4));
    assert contains(1);
    assert contains(2);
    assert contains(3);
    assert contains(4);
  }

  @Test public void addArray() {
    add(1, 2, 3);
    azzert.that(size(), is(3));
    assert contains(1);
    assert contains(2);
    assert contains(3);
    add(4, 1, 2, 3);
    azzert.that(size(), is(4));
    assert contains(1);
    assert contains(2);
    assert contains(3);
    assert contains(4);
    add(6, 7, 8, 9, 10, 5);
    azzert.that(size(), is(10));
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
    for (int i = 0; i < M; ++i) {
      invariant.check();
      azzert.that(N, is(size()));
       final int[] v = entries();
      azzert.that(v.length, is(N));
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
      azzert.that(i, is(v[0]));
      azzert.that(i + N - 1, is(v[N - 1]));
      remove(i);
      add(i + N);
    }
  }

  @Test public void constructorInitialCapacity() {
    azzert.that(new Integers(13).capacity(), is(16));
  }

  @Test public void constructorInitialCapacitySmallValue() {
    assert new Integers(1) != null;
    azzert.that(size(), is(0));
    azzert.that(capacity(), is(Integers.MIN_CAPACITY));
    assert !contains(0xDEAD);
    assert !contains(1);
    assert !contains(-1);
    assert !contains(0);
  }

  @Test public void constructorNegativeInitialCapacity() {
    azzert.that(new Integers(-1).capacity(), is(Integers.MIN_CAPACITY));
  }

  @Test public void defaultConstructor() {
    assert new Integers() != null;
    azzert.that(size(), is(0));
    azzert.that(capacity(), is(Integers.MIN_CAPACITY));
    assert !contains(0xDEAD);
    assert !contains(1);
    assert !contains(-1);
    assert !contains(0);
  }

  @Test public void massOperations() {
    add(0, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 11, 12, 13, 14, 15, 16, 17);
    azzert.that(size(), is(18));
    azzert.that(capacity(), is(32));
    assert contains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 15, 16, 17);
    remove(2, 3, 5, 7, 11, 13, 17);
    assert disjoint(2, 3, 5, 7, 11, 13, 17);
    remove(0, 2, 4, 6, 8, 10, 12, 14, 16, 18);
    assert disjoint(2, 3, 5, 7, 11, 13, 17);
    assert disjoint(0, 2, 4, 6, 8, 10, 12, 14, 16, 18);
    remove(1, 3, 5, 7, 9, 11, 13, 15, 17);
    azzert.that(size(), is(0));
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
    azzert.that(size(), is(1));
    add(1);
    azzert.that(size(), is(1));
    add(2);
    azzert.that(size(), is(2));
    add(2);
    azzert.that(size(), is(2));
    add(1);
    azzert.that(size(), is(2));
    remove(1);
    azzert.that(size(), is(1));
    remove(2);
    azzert.that(size(), is(0));
    remove(2);
    azzert.that(size(), is(0));
    remove(1);
    azzert.that(size(), is(0));
  }

  @Test public void valuesRange() {
    add(1, 2, 3);
     final int[] values = entries();
    Arrays.sort(values);
    azzert.that(Arrays.binarySearch(values, 1), is(0));
    azzert.that(Arrays.binarySearch(values, 2), is(1));
    azzert.that(Arrays.binarySearch(values, 3), is(2));
  }
}