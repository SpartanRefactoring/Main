package il.org.spartan.utils;

import org.eclipse.jdt.annotation.*;

public class Pair<First, Second> {
   @SuppressWarnings("unchecked") //
  public static <First, Second> Pair<First, Second>[] makePairs(final int ¢) {
    return new Pair[¢];
  }
   public static <First, Second> Pair<First, Second>[] makePairs(final int i, final int m) {
    return makePairs(i * m);
  }
   public static <A, B> Pair<A, B> newPair(final A a, final B b) {
    return new Pair<>(a, b);
  }
  private static boolean eq( final Object a,  final Object o) {
    return a == null ? o == null : a.equals(o);
  }

  public final First first;
  public final Second second;

  public Pair(final First first, final Second second) {
    this.first = first;
    this.second = second;
  }
  @Override public boolean equals( final Object ¢) {
    return ¢ == this || ¢ != null && getClass().equals(¢.getClass()) && eq(first, ((Pair<?, ?>) ¢).first) && eq(second, ((Pair<?, ?>) ¢).second);
  }
  @Override public int hashCode() {
    return second.hashCode() ^ first.hashCode() >>> 1;
  }
  @Override  public String toString() {
    return "<" + first + "," + second + ">";
  }
}
