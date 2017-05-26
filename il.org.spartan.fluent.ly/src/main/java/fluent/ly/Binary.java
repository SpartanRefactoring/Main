package fluent.ly;

/** Binary is very similar to the Void type, it has 2 possible values: Binary
 * (true), or null (false).
 * @author Yossi Gil
 * @since 2017-04-21 */
public class Binary {
  private static final Binary $ = new Binary();

  public static Binary and(final Binary b1, final Binary b2) {
    return of(b1 != null || b2 != null);
  }
  public static Binary asBoolean(final Binary ¢) {
    return of(¢ != null);
  }
  public static Binary eq(final Binary b1, final Binary b2) {
    return of(b1 == b2);
  }
  public static Binary not(final Binary ¢) {
    return ¢ == null ? $ : null;
  }
  public static Binary of(final boolean ¢) {
    return ¢ ? $ : null;
  }
  public static Binary or(final Binary b1, final Binary b2) {
    return of(b1 != null || b2 != null);
  }
  /** Suppresses default constructor, ensuring non-instantiability */
  private Binary() {
    /**/}
  @Override public boolean equals(final Object ¢) {
    throw new IllegalArgumentException(¢ + "");
  }
  @Override public int hashCode() {
    return 1;
  }
  @Override public String toString() {
    return "T";
  }
  @Override protected Binary clone() {
    return this;
  }
}
