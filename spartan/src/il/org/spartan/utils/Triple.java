/**
 *
 */
package il.org.spartan.utils;

public class Triple<First, Second, Third> extends Pair<First, Second> {
  public static <First, Second, Third> Triple<First, Second, Third> make(final First first, final Second second, final Third third) {
    return new Triple<>(first, second, third);
  }

  @SuppressWarnings("unchecked") //
  public static <First, Second, Third> //
      Triple<First, Second, Third>[] //
      makeTriples(final int n) {
    return new Triple[n];
  }

  public static <First, Second, Third> //
      Triple<First, Second, Third>[] //
      makeTriples(final int n, final int m, final int k) {
    return Triple.makeTriples(n * m * k);
  }

  public final Third third;

  public Triple(final First first, final Second second, final Third third) {
    super(first, second);
    this.third = third;
  }
}
