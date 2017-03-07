package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.research.util.*;

/** Failing test, originally from {@link normalizeTest} .
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings("static-method")
public class Issue0436 {
  @Test public void testRenamingWithQualified() {
    azzert.that(normalize.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear.foo());"), is("if(a == A) return b(a, B());"));
  }
}
