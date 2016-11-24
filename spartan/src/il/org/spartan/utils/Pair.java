package il.org.spartan.utils;

import org.jetbrains.annotations.*;

public class Pair<First, Second> {
  @NotNull @SuppressWarnings("unchecked") //
  public static <First, Second> Pair<First, Second>[] makePairs(final int ¢) {
    return new Pair[¢];
  }

  @NotNull public static <First, Second> Pair<First, Second>[] makePairs(final int i, final int m) {
    return makePairs(i * m);
  }

  @NotNull public static <A, B> Pair<A, B> newPair(final A a, final B b) {
    return new Pair<>(a, b);
  }

  private static boolean eq(@Nullable final Object a, @Nullable final Object o) {
    return a == null ? o == null : a.equals(o);
  }

  public final First first;
  public final Second second;

  public Pair(final First first, final Second second) {
    this.first = first;
    this.second = second;
  }

  @Override public boolean equals(@Nullable final Object ¢) {
    return ¢ == this || ¢ != null && getClass().equals(¢.getClass()) && eq(first, ((Pair<?, ?>) ¢).first) && eq(second, ((Pair<?, ?>) ¢).second);
  }

  @Override public int hashCode() {
    return second.hashCode() ^ first.hashCode() >>> 1;
  }

  @Override @NotNull public String toString() {
    return "<" + first + "," + second + ">";
  }
}
