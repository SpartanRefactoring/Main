package il.org.spartan.utils;

import org.jetbrains.annotations.*;

public class Triple<First, Second, Third> extends Pair<First, Second> {
  @NotNull public static <First, Second, Third> Triple<First, Second, Third> make(final First f, final Second s, final Third t) {
    return new Triple<>(f, s, t);
  }

  @NotNull @SuppressWarnings("unchecked") //
  public static <First, Second, Third> //
      Triple<First, Second, Third>[] //
      makeTriples(final int ¢) {
    return new Triple[¢];
  }

  @NotNull public static <First, Second, Third> //
      Triple<First, Second, Third>[] //
      makeTriples(final int i, final int m, final int k) {
    return Triple.makeTriples(i * k * m);
  }

  public final Third third;

  public Triple(final First first, final Second second, final Third third) {
    super(first, second);
    this.third = third;
  }
}
