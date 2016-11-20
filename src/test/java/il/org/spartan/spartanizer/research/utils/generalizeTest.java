package il.org.spartan.spartanizer.research.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.util.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class generalizeTest {
  @Test public void a() {
    azzert.that(generalize.code("x + y"), is("$N0 + $N1"));
  }
}
