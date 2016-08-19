/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.lazy;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lazy.Environment.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.lazy.Environment.*;

@SuppressWarnings({ "boxing", "javadoc", "null" }) //
public class Hamlet {
  private Property<Boolean> hamlet = undefined();
  {
    hamlet = function(() -> {
      hamlet.bind2(() -> !hamlet.get());
      return false;
    });
  }
  @Test public void seriesA01() {
    @Nullable Boolean first = hamlet.¢();
    azzert.notNull(first);
    azzert.notNull(hamlet.cache);
    @Nullable Boolean second = hamlet.get();
    @Nullable Boolean third = hamlet.get();
    @Nullable Boolean fourth = hamlet.get();
    azzert.that("" + first, is("false"));
    azzert.that("" + second, is("true"));
    azzert.that("" + third, is("false"));
    azzert.that("" + fourth, is("true"));
  }
}