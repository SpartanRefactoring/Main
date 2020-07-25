/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.lazy;

import static fluent.ly.azzert.is;
import static il.org.spartan.lazy.Environment.function;
import static il.org.spartan.lazy.Environment.value;

import org.junit.Test;

import fluent.ly.azzert;

@SuppressWarnings("boxing") public class Hamlet implements Environment {
  private final Property<Boolean> $ = function(init());
  private final Function0<Boolean> permanent = () -> !$.get();

  @Test public void seriesA01() {
    final Boolean first = $.¢();
    assert first != null;
    assert $.cache != null;
    final Boolean second = $.get(), third = $.get(), fourth = $.get();
    azzert.that(first + "", is("false"));
    azzert.that(second + "", is("true"));
    azzert.that(third + "", is("false"));
    azzert.that(fourth + "", is("true"));
  }

  private Boolean first() {
    $.ϑ(permanent, $);
    return Boolean.FALSE;
  }

  private Function0<Boolean> init() {
    return () -> first();
  }

  public static class Hamlet2 {
    private final Function0<Boolean> permanent1 = null;
    private final Property<Boolean> $ = value(false).push(permanent1);
    @SuppressWarnings("unused") private final Function0<Boolean> permanent = () -> !$.get();
  }
}