package il.org.spartan.refactoring.java;

/** Maintain a canonical order of modifiers.
 * @author Yossi Gil
 * @since 2016 */
public enum Modifiers {
  PUBLIC, //
  PROTECTED, //
  PRIVATE, //
  ABSTRACT, //
  DEFAULT, //
  STATIC, //
  FINAL, //
  TRANSIENT, //
  VOLATILE, //
  SYNCHRONIZED, //
  NATIVE, //
  STRICTFP, //
  ;
  public static Modifiers find(final String modifier) {
    for (final Modifiers $ : Modifiers.values())
      if (modifier.equals(("" + $).toLowerCase()))
        return $;
    return null;
  }

  private static int gt(final Modifiers m1, final Modifiers m2) {
    return m1.ordinal() > m2.ordinal() ? 1 : m1.ordinal() < m2.ordinal() ? -1 : 0;
  }

  public static int gt(final String modifier1, final String modifier2) {
    return gt(find(modifier1), find(modifier2));
  }
}