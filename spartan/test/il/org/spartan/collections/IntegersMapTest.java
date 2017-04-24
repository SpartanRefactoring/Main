/**
 *
 */
package il.org.spartan.collections;

import static org.junit.Assert.*;

import org.junit.*;

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
    assertEquals(100, m.get(10));
  }

  @Test public void get1000() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.put(¢, 2 * ¢ + 1);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      assertEquals(2 * ¢ + 1, m.get(¢));
  }

  @Test public void get2() {
    m.put(10, 100).put(20, 30);
    assertEquals(100, m.get(10));
    assertEquals(30, m.get(20));
  }

  @Test public void get3() {
    m.put(10, 100).put(20, 30).put(50, 60);
    assertEquals(100, m.get(10));
    assertEquals(30, m.get(20));
    assertEquals(60, m.get(50));
  }

  @Test public void get4() {
    m.put(10, 100).put(20, 30).put(30, 40).put(50, 60);
    assertEquals(100, m.get(10));
    assertEquals(30, m.get(20));
    assertEquals(60, m.get(50));
    assertEquals(40, m.get(30));
  }

  @Test public void increment1000() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.increment(¢);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      assertEquals(1, m.get(¢));
  }

  @Test public void incrementMany() {
    for (int i = 0; i < 100; ++i)
      for (int j = 0; j <= i; ++j)
        m.increment(i);
    for (int ¢ = 0; ¢ < 100; ++¢)
      assertEquals(¢ + 1, m.get(¢));
  }

  @Test public void init1000() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.init(¢);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      assertEquals(0, m.get(¢));
  }

  @Test public void initDoesClear() {
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    for (int ¢ = 0; ¢ < 1000; ++¢)
      m.init(¢);
    for (int ¢ = 0; ¢ < 1000; ++¢)
      assertEquals(0, m.get(¢));
  }

  @Test @SuppressWarnings("static-method") public void insert1() {
    new IntegersMap().put(10, 100);
  }

  @Test public void keys() {
    for (int ¢ = 0; ¢ < 10000; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 0; ¢ < 10000; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys10() {
    for (int ¢ = 0; ¢ < 10; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.keys();
    for (int ¢ = 0; ¢ < 10; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys100() {
    for (int ¢ = 0; ¢ < 100; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 0; ¢ < 100; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys15() {
    for (int ¢ = 0; ¢ < 15; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.keys();
    for (int ¢ = 0; ¢ < 15; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys15rehash() {
    for (int ¢ = 0; ¢ < 15; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    m.rehash();
    final int[] keys = m.keys();
    for (int ¢ = 0; ¢ < 15; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys17() {
    for (int ¢ = 0; ¢ < 17; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.keys();
    for (int ¢ = 0; ¢ < 17; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys18() {
    for (int ¢ = 0; ¢ < 18; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 0; ¢ < 18; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys18inverse() {
    for (int ¢ = 0; ¢ < 18; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 17; ¢ >= 0; --¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys20inverse() {
    for (int ¢ = 0; ¢ < 20; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 19; ¢ >= 0; --¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys22() {
    for (int ¢ = 0; ¢ < 22; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 0; ¢ < 22; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys30() {
    for (int ¢ = 0; ¢ < 30; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 0; ¢ < 30; ++¢)
      assertEquals(¢, keys[¢]);
  }

  @Test public void keys30inverse() {
    for (int ¢ = 0; ¢ < 30; ++¢)
      m.put(¢, IntegersMap.hash(¢));
    final int[] keys = m.sortedKeys();
    for (int ¢ = 29; ¢ >= 0; --¢)
      assertEquals(¢, keys[¢]);
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
    assertEquals(h, m.get(h));
    assert m.location(h) >= 0;
  }

  @Test public void location1_1() {
    final int h = IntegersMap.hash(1);
    m.put(h, h);
    assertEquals(h, m.get(h));
    assert m.location(h) >= 0;
  }

  @Test public void location11() {
    location(11);
  }

  @Test public void location2() {
    final int h0 = IntegersMap.hash(0);
    m.put(h0, 100);
    assertEquals(100, m.get(h0));
    final int h1 = IntegersMap.hash(1);
    m.put(h1, 1);
    assert h1 != h0;
    assert m.location(h0) >= 0;
    assert m.location(h1) >= 0;
    assert m.location(h1) != m.location(h0);
    assertEquals(1, m.get(h1));
    assertEquals(100, m.get(h0));
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