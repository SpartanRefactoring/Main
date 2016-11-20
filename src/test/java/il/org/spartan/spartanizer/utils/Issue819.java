package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

/** Tests of {@link Str}
 * @author Shimon Azulay
 * @author Idan Atias
 * @since 16-11-11 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue819 {
  @Test public void str_test0() {
    azzert.that(new Str("coverage").inner(), is("coverage"));
  }

  @Test public void str_test1() {
    azzert.that(new Str().inner(), is(null));
  }

  @Test public void str_test2() {
    final Str s = new Str();
    s.set("Hamadia");
    azzert.that(s.inner(), is("Hamadia"));
  }

  @Test public void str_test3() {
    azzert.that(new Str("Doron Miran").inner(), is("Doron Miran"));
  }

  @Test public void str_test4() {
    assert new Str().isEmptyx();
    assert !new Str("bla").isEmptyx();
  }

  @Test public void str_test5() {
    assert new Str("This is not empty").notEmpty();
    assert !new Str().notEmpty();
  }
}
