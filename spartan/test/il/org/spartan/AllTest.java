// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import il.org.spartan.streotypes.*;
import fluent.ly.*;

@TestCase @SuppressWarnings("static-method") public class AllTest {
  @Test public void testNotNul_NonNulllNull() {
    assert !all.NonNull(new Object[] { new Object(), null });
  }

  @Test public void testNonNull_EmptySet() {
    assert all.NonNull(new HashSet<>());
  }

  @Test public void testNonNull_HashSetTwoNulls() {
     final HashSet<Object> set = new HashSet<>();
    set.add(null);
    set.add(null);
    assert !all.NonNull(set);
  }

  @Test public void testNonNull_LengthZero() {
    assert all.NonNull(new Object[0]);
  }

  @Test public void testNonNull_NonNullNonNull() {
    assert all.NonNull(new Object[] { new Object(), new Object() });
  }

  @Test public void testNonNull_NullArray() {
    assert !all.NonNull((Object[]) null);
  }

  @Test public void testNonNull_NullNonNull() {
    assert !all.NonNull(new Object[] { null, new Object() });
  }

  @Test public void testNonNull_NullSet() {
    assert !all.NonNull((HashSet<Object>) null);
  }

  @Test public void testNonNull_SetNonNull() {
     final HashSet<String> set = new HashSet<>();
    set.add("abc");
    set.add(null);
    assert !all.NonNull(set);
  }

  @Test public void testNonNull_SetNullNonNull() {
     final HashSet<String> set = new HashSet<>();
    set.add(null);
    set.add("");
    assert !all.NonNull(set);
  }

  @Test public void testNonNull_SetTwoEmptyStrings() {
     final TreeSet<String> set = new TreeSet<>();
    set.add("");
    set.add("");
    assert all.NonNull(set);
  }

  @Test public void testNonNull_SetTwoStrings() {
     final TreeSet<String> set = new TreeSet<>();
    set.add("abc");
    set.add("cde");
    assert all.NonNull(set);
  }

  @Test public void testNonNull_StringArrayNoNulls() {
    assert all.NonNull(new String[] { "Hello", "World" });
  }

  @Test public void testNonNull_StringArrayWithNulls() {
    assert !all.NonNull(new String[] { "Hello", null, "World" });
  }

  @Test public void testNonNull_TwoNulls() {
    assert !all.NonNull(new Object[] { null, null });
  }
}
