// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import java.util.*;

import org.jetbrains.annotations.*;
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
    @NotNull final HashSet<Object> set = new HashSet<>();
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
    @NotNull final HashSet<String> set = new HashSet<>();
    set.add("abc");
    set.add(null);
    assert !All.notNull(set);
  }

  @Test public void testNotNull_SetNullNotNull() {
    @NotNull final HashSet<String> set = new HashSet<>();
    set.add(null);
    set.add("");
    assert !All.notNull(set);
  }

  @Test public void testNotNull_SetTwoEmptyStrings() {
    @NotNull final TreeSet<String> set = new TreeSet<>();
    set.add("");
    set.add("");
    assert All.notNull(set);
  }

  @Test public void testNotNull_SetTwoStrings() {
    @NotNull final TreeSet<String> set = new TreeSet<>();
    set.add("abc");
    set.add("cde");
    assert All.notNull(set);
  }

  @Test public void testNotNull_StringArrayNoNulls() {
    assert All.notNull(new String[] { "Hello", "World" });
  }

  @Test public void testNotNull_StringArrayWithNulls() {
    assert !All.notNull(new String[] { "Hello", null, "World" });
  }

  @Test public void testNotNull_TwoNulls() {
    assert !All.notNull(new Object[] { null, null });
  }
}
