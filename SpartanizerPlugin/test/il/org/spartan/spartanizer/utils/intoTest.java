package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings({ "javadoc", "static-method" })
public final class intoTest {
  @Test public void dCorrect() {
    azzert.that(into.d("int f() { return a; }"), iz("int f() { return a; }"));
  }
  @Test public void dNonNull() {
    assert into.d("int f() { return a; }") != null;
  }
  @Test(expected = AssertionError.class) public void dOnNull() {
    into.d(null);
  }
  @Test public void findFirstType() {
    assert t("int __;") != null;
  }
}
