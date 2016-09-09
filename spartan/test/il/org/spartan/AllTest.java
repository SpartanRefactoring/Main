// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") @TestCase public class AllTest {
  @Test public void testNotNul_NotNulllNull() {
    assertFalse(All.notNull(new Object[] { new Object(), null }));
  }

  @Test public void testNotNull_EmptySet() {
    assertTrue(All.notNull(new HashSet<>()));
  }

  @Test public void testNotNull_HashSetTwoNulls() {
    final HashSet<Object> set = new HashSet<>();
    set.add(null);
    set.add(null);
    assertFalse(All.notNull(set));
  }

  @Test public void testNotNull_LengthZero() {
    assertTrue(All.notNull(new Object[0]));
  }

  @Test public void testNotNull_NotNullNotNull() {
    assertTrue(All.notNull(new Object[] { new Object(), new Object() }));
  }

  @Test public void testNotNull_NullArray() {
    assertFalse(All.notNull((Object[]) null));
  }

  @Test public void testNotNull_NullNotNull() {
    assertFalse(All.notNull(new Object[] { null, new Object() }));
  }

  @Test public void testNotNull_NullSet() {
    assertFalse(All.notNull((HashSet<Object>) null));
  }

  @Test public void testNotNull_SetNotNull() {
    final HashSet<String> set = new HashSet<>();
    set.add("abc");
    set.add(null);
    assertFalse(All.notNull(set));
  }

  @Test public void testNotNull_SetNullNotNull() {
    final HashSet<String> set = new HashSet<>();
    set.add(null);
    set.add("");
    assertFalse(All.notNull(set));
  }

  @Test public void testNotNull_SetTwoEmptyStrings() {
    final TreeSet<String> set = new TreeSet<>();
    set.add("");
    set.add("");
    assertTrue(All.notNull(set));
  }

  @Test public void testNotNull_SetTwoStrings() {
    final TreeSet<String> set = new TreeSet<>();
    set.add("abc");
    set.add("cde");
    assertTrue(All.notNull(set));
  }

  @Test public void testNotNull_StringArrayNoNulls() {
    final String[] a = new String[] { "Hello", "World" };
    assertTrue(All.notNull((Object[]) a));
  }

  @Test public void testNotNull_StringArrayWithNulls() {
    final String[] a = new String[] { "Hello", null, "World" };
    assertFalse(All.notNull((Object[]) a));
  }

  @Test public void testNotNull_TwoNulls() {
    assertFalse(All.notNull(new Object[] { null, null }));
  }
}
