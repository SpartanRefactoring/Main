package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class intoTest {
  @Test public void dCorrect() {
    azzert.that(into.d("int f() { return a; }"), iz("int f() { return a; }"));
  }

  @Test public void dNotNull() {
    assert into.d("int f() { return a; }") != null;
  }

  @Test(expected = AssertionError.class) public void dOnNull() {
    into.d(null);
  }

  @Test public void findFirstType() {
    assert t("int _;") != null;
  }
}
