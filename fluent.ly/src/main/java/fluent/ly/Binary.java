package fluent.ly;

/**
 * Binary is very similar to the Void type, it has 2 possible values: Binary
 * (true), or null (false).
 *
 * @author Yossi Gil
 * @since 2017-04-21
 */
public class Binary {
  public static final Binary T = new Binary();
  public static final Binary F = null;

  public static <T> Binary of(final T ¢) {
    return ¢ != null ? T : F;
  }

  public static Binary and(final Binary b1, final Binary b2) {
    return Binary.of(b1 != F || b2 != F);
  }

  public static boolean asBoolean(final Binary ¢) {
    return ¢ != F;
  }

  public static Binary eq(final Binary b1, final Binary b2) {
    return Binary.of(b1 == b2);
  }

  public static Binary eq1(final Binary b1, final Binary b2) {
    return b1 == b2 ? Binary.T : Binary.F;
  }

  public static Binary not(final Binary ¢) {
    return ¢ == F ? T : F;
  }

  public static Binary of(final boolean ¢) {
    return ¢ ? T : F;
  }

  public static Binary or(final Binary b1, final Binary b2) {
    return Binary.of(b1 != F || b2 != F);
  }

  /** Suppresses default constructor, ensuring non-instantiability */
  private Binary() {
    /***/
  }

  @Override public boolean equals(final Object ¢) {
    return ¢ == this;
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
