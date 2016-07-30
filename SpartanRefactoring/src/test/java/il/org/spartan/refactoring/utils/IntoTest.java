package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.MatcherAssert.*;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class IntoTest {
  @Test public void dCorrect() {
    assertThat(Into.d("int f() { return a; }"), iz("int f() { return a; }"));
  }
  @Test public void dNotNull() {
    assertThat(Into.d("int f() { return a; }"), notNullValue());
  }
  @Test(expected = AssertionError.class) public void dOnNull() {
    Into.d(null);
  }
}
