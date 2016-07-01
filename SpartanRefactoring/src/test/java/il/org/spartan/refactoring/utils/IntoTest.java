package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class IntoTest {
  @Test public void dCorrect() {
    that(Into.d("int f() { return a; }"), iz("int f() { return a; }"));
  }
  @Test public void dNotNull() {
    that(Into.d("int f() { return a; }"), notNullValue());
  }
  @Test(expected = AssertionError.class) public void dOnNull() {
    Into.d(null);
  }
}
