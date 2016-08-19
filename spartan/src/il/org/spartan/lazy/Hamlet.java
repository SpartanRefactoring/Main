/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.lazy;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lazy.Environment.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "boxing", "javadoc", "null" }) //
public class Hamlet implements Environment {
  private Property<Boolean> hamlet = undefined();
  {
    hamlet = function(() -> {
      hamlet.bind2(() -> !hamlet.get());
      return false;
    });
  }

  @Test public void seriesA01() {
    @Nullable final Boolean first = hamlet.¢();
    azzert.notNull(first);
    azzert.notNull(hamlet.cache);
    @Nullable final Boolean second = hamlet.get();
    @Nullable final Boolean third = hamlet.get();
    @Nullable final Boolean fourth = hamlet.get();
    azzert.that("" + first, is("false"));
    azzert.that("" + second, is("true"));
    azzert.that("" + third, is("false"));
    azzert.that("" + fourth, is("true"));
  }
}