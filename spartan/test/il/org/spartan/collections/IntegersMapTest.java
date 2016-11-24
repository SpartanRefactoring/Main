package il.org.spartan.collections;

import static il.org.spartan.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

public final class IntegersMapTest {
  final IntegersMap m = new IntegersMap();

  @Test public void containsMany() {
    for (int ¢ = 0; ¢ < 10000; ++¢)
      m.put(IntegersMap.hash(¢), ¢);
    for (int ¢ = 0; ¢ < 10000; ++¢)
      assert m.contains(IntegersMap.hash(¢));
  }

  @Test public void doesNotContain() {
    for (int ¢ = 0; ¢ < 10000; ++¢)
      m.put(¢, ¢);
    for (int ¢ = 10000; ¢ < 100000; ++¢)
      assert !m.contains(¢);
  }

  @Test public void find() {
    assert m.find(10) != -1;
    m.put(10, 100);
    assert m.find(10) == -1;
  }

  @Test public void get1() {
    m.put(10, 100);
    azzert.that(m.get(10), is(100));
  }

  @Test public void get1000() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.put(¢, 2 * ¢ + 1);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      azzert.that(m.get(¢), is(2 * ¢ + 1));
  }

  @Test public void get2() {
    m.put(10, 100).put(20, 30);
    azzert.that(m.get(10), is(100));
    azzert.that(m.get(20), is(30));
  }

  @Test public void get3() {
    m.put(10, 100).put(20, 30).put(50, 60);
    azzert.that(m.get(10), is(100));
    azzert.that(m.get(20), is(30));
    azzert.that(m.get(50), is(60));
  }

  @Test public void get4() {
    m.put(10, 100).put(20, 30).put(30, 40).put(50, 60);
    azzert.that(m.get(10), is(100));
    azzert.that(m.get(20), is(30));
    azzert.that(m.get(50), is(60));
    azzert.that(m.get(30), is(40));
  }

  @Test public void increment1000() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.increment(¢);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      azzert.that(m.get(¢), is(1));
  }

  @Test public void incrementMany() {
    for (int i = 0; i < 100; ++i)
      for (int j = 0; j <= i; ++j)
        m.increment(i);
    for (int ¢ = 0; ¢ < 100; ++¢)
      azzert.that(m.get(¢), is(¢ + 1));
  }

  @Test public void init1000() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.init(¢);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      azzert.that(m.get(¢), is(0));
  }

  @Test public void initDoesClear() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.init(¢);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      azzert.that(m.get(¢), is(0));
  }

  @SuppressWarnings("static-method") //
  @Test public void insert1() {
    new IntegersMap().put(10, 100);
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys() {
    for (int ¢ = 0; ¢ < 10000; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int i = 0; i < 10000; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys10() {
    for (int ¢ = 0; ¢ < 10; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.keys();
    for (int i = 0; i < 10; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys100() {
    for (int ¢ = 0; ¢ < 100; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int i = 0; i < 100; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys15() {
    for (int ¢ = 0; ¢ < 15; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.keys();
    for (int i = 0; i < 15; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys15rehash() {
    for (int ¢ = 0; ¢ < 15; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    m.rehash();
    @NotNull final int[] keys = m.keys();
    for (int i = 0; i < 15; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys17() {
    for (int ¢ = 0; ¢ < 17; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.keys();
    for (int i = 0; i < 17; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys18() {
    for (int ¢ = 0; ¢ < 18; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int i = 0; i < 18; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys18inverse() {
    for (int ¢ = 0; ¢ < 18; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int i = 17; i >= 0; --i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys20inverse() {
    for (int ¢ = 0; ¢ < 20; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int i = 19; i >= 0; --i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys22() {
    for (int ¢ = 0; ¢ < 22; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int ¢ = 0; ¢ < 22; ++¢)
      azzert.that(keys[¢], is(¢));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys30() {
    for (int ¢ = 0; ¢ < 30; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int i = 0; i < 30; ++i)
      azzert.that(keys[i], is(i));
  }

  /** [[SuppressWarningsSpartan]] */
  @Test public void keys30inverse() {
    for (int ¢ = 0; ¢ < 30; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    @NotNull final int[] keys = m.sortedKeys();
    for (int ¢ = 29; ¢ >= 0; --¢)
      azzert.that(keys[¢], is(¢));
  }

  @Test public void loaction1000() {
    location(1000);
  }

  @Test public void loaction150() {
    location(1000);
  }

  @Test public void location() {
    assert m.location(10) == -1;
    m.put(10, 100);
    assert m.location(10) != -1;
  }

  @Test public void location1() {
    final int h = IntegersMap.hash(0);
    m.put(h, h);
    azzert.that(m.get(h), is(h));
    assert m.location(h) >= 0;
  }

  @Test public void location1_1() {
    final int h = IntegersMap.hash(1);
    m.put(h, h);
    azzert.that(m.get(h), is(h));
    assert m.location(h) >= 0;
  }

  @Test public void location11() {
    location(11);
  }

  @Test public void location2() {
    final int h0 = IntegersMap.hash(0);
    m.put(h0, 100);
    azzert.that(m.get(h0), is(100));
    final int h1 = IntegersMap.hash(1);
    m.put(h1, 1);
    assert h1 != h0;
    assert m.location(h0) >= 0;
    assert m.location(h1) >= 0;
    assert m.location(h1) != m.location(h0);
    azzert.that(m.get(h1), is(1));
    azzert.that(m.get(h0), is(100));
  }

  @Test public void location30() {
    location(30);
  }

  @Test public void location5() {
    location(5);
  }

  @Test public void location7() {
    location(7);
  }

  @Test public void locationLadder() {
    location(0);
    location(1);
    location(2);
    location(3);
    location(4);
    location(5);
  }

  @Test public void loczation4() {
    m.put(10, 100).put(20, 30).put(30, 40).put(50, 60);
    assert m.location(10) >= 0;
    assert m.location(20) >= 0;
    assert m.location(30) >= 0;
    assert m.location(50) >= 0;
  }

  private void location(final int i) {
    for (int ¢ = 0; ¢ < i; ++¢)
      m.put(IntegersMap.hash(¢), 2 * ¢ + 1);
    for (int ¢ = 0; ¢ < i; ++¢)
      assert m.location(IntegersMap.hash(¢)) >= 0;
  }
}