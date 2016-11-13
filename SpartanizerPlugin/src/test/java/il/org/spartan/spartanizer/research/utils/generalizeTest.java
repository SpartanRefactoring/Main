package il.org.spartan.spartanizer.research.utils;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.research.util.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method") public class generalizeTest {
  @Test public void a() {
    assertEquals("$N0 + $N1", generalize.code("x + y"));
  }
}
