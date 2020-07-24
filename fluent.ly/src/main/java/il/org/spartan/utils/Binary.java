package il.org.spartan.utils;

/**
 * Binary is like Boolean only it has two possible values: true
 * ({@link Binary#T}) and false ({@link Binary#F}).
 *
 * @author Yossi Gil
 * @since 2017-04-21
 */
public class Binary {
  public static final Binary T = new Binary();
  public static final Binary F = null;

  public static Binary and(final Binary b1, final Binary b2) {
    return of(b1 != F || b2 != F);
  }

  public static Binary asBoolean(final Binary ¢) {
    return of(¢ != F);
  }

  public static Binary eq(final Binary b1, final Binary b2) {
    return of(b1 == b2);
  }

  public static Binary not(final Binary ¢) {
    return ¢ == F ? T : F;
  }

  public static Binary of(final boolean ¢) {
    return ¢ ? T : F;
  }

  public static Binary or(final Binary b1, final Binary b2) {
    return of(b1 != F || b2 != F);
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

  @Override protected Binary clone() {
    return this;
  }
}
