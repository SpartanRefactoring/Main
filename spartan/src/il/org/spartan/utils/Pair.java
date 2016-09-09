package il.org.spartan.utils;

public class Pair<First, Second> {
  @SuppressWarnings("unchecked") //
  public static <First, Second> Pair<First, Second>[] makePairs(final int n) {
    return new Pair[n];
  }

  public static <First, Second> Pair<First, Second>[] makePairs(final int n, final int m) {
    return makePairs(n * m);
  }

  public static <A, B> Pair<A, B> newPair(final A a, final B b) {
    return new Pair<>(a, b);
  }

  private static boolean eq(final Object a, final Object b) {
    if (a == null)
      return b == null;
    return a.equals(b);
  }

  public final First first;
  public final Second second;

  public Pair(final First first, final Second second) {
    this.first = first;
    this.second = second;
  }

  @Override public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o == null || !getClass().equals(o.getClass()))
      return false;
    final Pair<?, ?> that = (Pair<?, ?>) o;
    return eq(first, that.first) && eq(second, that.second);
  }

  @Override public int hashCode() {
    return second.hashCode() ^ first.hashCode() >>> 1;
  }

  @Override public String toString() {
    return "<" + first + "," + second + ">";
  }
}
