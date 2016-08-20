package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class IntoTest {
  @Test public void dCorrect() {
    azzert.that(Into.d("int f() { return a; }"), iz("int f() { return a; }"));
  }

  @Test public void dNotNull() {
    azzert.notNull(Into.d("int f() { return a; }"));
  }

  @Test(expected = AssertionError.class) public void dOnNull() {
    Into.d(null);
  }
}
