// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import java.util.*;

import org.junit.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") @TestCase public class AllTest {
  @Test public void testNotNul__NotNulllNull() {
    assert !All.notNull(new Object[] { new Object(), null });
  }

  @Test public void testNotNull__EmptySet() {
    assert All.notNull(new HashSet<>());
  }

  @Test public void testNotNull__HashSetTwoNulls() {
    final HashSet<Object> set = new HashSet<>();
    set.add(null);
    set.add(null);
    assert !All.notNull(set);
  }

  @Test public void testNotNull__LengthZero() {
    assert All.notNull(new Object[0]);
  }

  @Test public void testNotNull__NotNullNotNull() {
    assert All.notNull(new Object[] { new Object(), new Object() });
  }

  @Test public void testNotNull__NullArray() {
    assert !All.notNull((Object[]) null);
  }

  @Test public void testNotNull__NullNotNull() {
    assert !All.notNull(new Object[] { null, new Object() });
  }

  @Test public void testNotNull__NullSet() {
    assert !All.notNull((HashSet<Object>) null);
  }

  @Test public void testNotNull__SetNotNull() {
    final HashSet<String> set = new HashSet<>();
    set.add("abc");
    set.add(null);
    assert !All.notNull(set);
  }

  @Test public void testNotNull__SetNullNotNull() {
    final HashSet<String> set = new HashSet<>();
    set.add(null);
    set.add("");
    assert !All.notNull(set);
  }

  @Test public void testNotNull__SetTwoEmptyStrings() {
    final TreeSet<String> set = new TreeSet<>();
    set.add("");
    set.add("");
    assert All.notNull(set);
  }

  @Test public void testNotNull__SetTwoStrings() {
    final TreeSet<String> set = new TreeSet<>();
    set.add("abc");
    set.add("cde");
    assert All.notNull(set);
  }

  @Test public void testNotNull__StringArrayNoNulls() {
    final String[] a = new String[] { "Hello", "World" };
    assert All.notNull((Object[]) a);
  }

  @Test public void testNotNull__StringArrayWithNulls() {
    final String[] a = new String[] { "Hello", null, "World" };
    assert !All.notNull((Object[]) a);
  }

  @Test public void testNotNull__TwoNulls() {
    assert !All.notNull(new Object[] { null, null });
  }
}
