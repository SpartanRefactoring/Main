package il.org.spartan.utils;

import static il.org.spartan.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class PairTest {
  @Test public void testSymmetry() {
    @NotNull final Pair<Integer, Integer> //
    p1 = new NamedPair("a", Integer.valueOf(1001), Integer.valueOf(-5017)), p2 = new Pair<>(Integer.valueOf(1001), Integer.valueOf(-5017));
    azzert.that(p2.equals(p1), is(p1.equals(p2)));
  }

  @SuppressWarnings("all") public static class NamedPair extends Pair<Integer, Integer> {
    public final String name;

    public NamedPair(final String name, final Integer a, final Integer b) {
      super(a, b);
      this.name = name;
    }

    public boolean equals(@Nullable final Pair<Integer, Integer> o) {
      return o == this || o != null && o instanceof NamedPair && equals((NamedPair) o);
    }

    @Override @NotNull public String toString() {
      return name + super.toString();
    }

    private boolean equals(@NotNull final NamedPair that) {
      return first.equals(that.first) && second.equals(that.second) && name.equals(that.name);
    }
  }
}
