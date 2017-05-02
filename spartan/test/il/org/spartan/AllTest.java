// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.streotypes.*;
import fluent.ly.*;

@TestCase @SuppressWarnings("static-method") public class AllTest {
  @Test public void testNotNul_NotNulllNull() {
    assert !all.notNull(new Object[] { new Object(), null });
  }

  @Test public void testNotNull_EmptySet() {
    assert all.notNull(new HashSet<>());
  }

  @Test public void testNotNull_HashSetTwoNulls() {
    @NotNull final HashSet<Object> set = new HashSet<>();
    set.add(null);
    set.add(null);
    assert !all.notNull(set);
  }

  @Test public void testNotNull_LengthZero() {
    assert all.notNull(new Object[0]);
  }

  @Test public void testNotNull_NotNullNotNull() {
    assert all.notNull(new Object[] { new Object(), new Object() });
  }

  @Test public void testNotNull_NullArray() {
    assert !all.notNull((Object[]) null);
  }

  @Test public void testNotNull_NullNotNull() {
    assert !all.notNull(new Object[] { null, new Object() });
  }

  @Test public void testNotNull_NullSet() {
    assert !all.notNull((HashSet<Object>) null);
  }

  @Test public void testNotNull_SetNotNull() {
    @NotNull final HashSet<String> set = new HashSet<>();
    set.add("abc");
    set.add(null);
    assert !all.notNull(set);
  }

  @Test public void testNotNull_SetNullNotNull() {
    @NotNull final HashSet<String> set = new HashSet<>();
    set.add(null);
    set.add("");
    assert !all.notNull(set);
  }

  @Test public void testNotNull_SetTwoEmptyStrings() {
    @NotNull final TreeSet<String> set = new TreeSet<>();
    set.add("");
    set.add("");
    assert all.notNull(set);
  }

  @Test public void testNotNull_SetTwoStrings() {
    @NotNull final TreeSet<String> set = new TreeSet<>();
    set.add("abc");
    set.add("cde");
    assert all.notNull(set);
  }

  @Test public void testNotNull_StringArrayNoNulls() {
    assert all.notNull(new String[] { "Hello", "World" });
  }

  @Test public void testNotNull_StringArrayWithNulls() {
    assert !all.notNull(new String[] { "Hello", null, "World" });
  }

  @Test public void testNotNull_TwoNulls() {
    assert !all.notNull(new Object[] { null, null });
  }
}
