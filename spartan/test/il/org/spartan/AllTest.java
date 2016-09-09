// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import java.util.*;

import org.junit.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") @TestCase public class AllTest {
  @Test public void testNotNul_NotNulllNull() {
    assert !All.notNull(new Object[] { new Object(), null });
  }

  @Test public void testNotNull_EmptySet() {
    assert All.notNull(new HashSet<>());
  }

  @Test public void testNotNull_HashSetTwoNulls() {
    final HashSet<Object> set = new HashSet<>();
    set.add(null);
    set.add(null);
    assert !All.notNull(set);
  }

  @Test public void testNotNull_LengthZero() {
    assert All.notNull(new Object[0]);
  }

  @Test public void testNotNull_NotNullNotNull() {
    assert All.notNull(new Object[] { new Object(), new Object() });
  }

  @Test public void testNotNull_NullArray() {
    assert !All.notNull((Object[]) null);
  }

  @Test public void testNotNull_NullNotNull() {
    assert !All.notNull(new Object[] { null, new Object() });
  }

  @Test public void testNotNull_NullSet() {
    assert !All.notNull((HashSet<Object>) null);
  }

  @Test public void testNotNull_SetNotNull() {
    final HashSet<String> set = new HashSet<>();
    set.add("abc");
    set.add(null);
    assert !All.notNull(set);
  }

  @Test public void testNotNull_SetNullNotNull() {
    final HashSet<String> set = new HashSet<>();
    set.add(null);
    set.add("");
    assert !All.notNull(set);
  }

  @Test public void testNotNull_SetTwoEmptyStrings() {
    final TreeSet<String> set = new TreeSet<>();
    set.add("");
    set.add("");
    assert All.notNull(set);
  }

  @Test public void testNotNull_SetTwoStrings() {
    final TreeSet<String> set = new TreeSet<>();
    set.add("abc");
    set.add("cde");
    assert All.notNull(set);
  }

  @Test public void testNotNull_StringArrayNoNulls() {
    final String[] a = new String[] { "Hello", "World" };
    assert All.notNull((Object[]) a);
  }

  @Test public void testNotNull_StringArrayWithNulls() {
    final String[] a = new String[] { "Hello", null, "World" };
    assert !All.notNull((Object[]) a);
  }

  @Test public void testNotNull_TwoNulls() {
    assert !All.notNull(new Object[] { null, null });
  }
}
