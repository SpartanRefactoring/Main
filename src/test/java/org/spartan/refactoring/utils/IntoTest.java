package org.spartan.refactoring.utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.spartan.hamcrest.MatcherAssert.assertThat;
import static org.spartan.hamcrest.MatcherAssert.iz;

import org.junit.Test;

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
