/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.lazy;

import static fluent.ly.azzert.*;
import static il.org.spartan.lazy.Environment.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import fluent.ly.*;

@SuppressWarnings({ "boxing", "null" }) //
public class Hamlet implements Environment {
  private final Property<Boolean> $ = function(init());
  private final Function0<@Nullable Boolean> permanent = () -> !$.get();

  @Test public void seriesA01() {
    @Nullable final Boolean first = $.¢();
    assert first != null;
    assert $.cache != null;
    @Nullable final Boolean second = $.get(), third = $.get(), fourth = $.get();
    azzert.that(first + "", is("false"));
    azzert.that(second + "", is("true"));
    azzert.that(third + "", is("false"));
    azzert.that(fourth + "", is("true"));
  }

  private Boolean first() {
    $.ϑ(permanent, $);
    return Boolean.FALSE;
  }

  private Function0<@Nullable Boolean> init() {
    return () -> first();
  }

  public static class Hamlet2 {
    @Nullable private final Function0<@Nullable Boolean> permanent1 = null;
    private final Property<Boolean> $ = value(false).push(permanent1);
    @SuppressWarnings("unused") private final Function0<@Nullable Boolean> permanent = () -> !$.get();
  }
}