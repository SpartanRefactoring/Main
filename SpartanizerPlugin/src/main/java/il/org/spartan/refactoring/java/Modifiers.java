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
  TRANCIENT, // TODO: Alex: there is a spelling error here, I will let you write
             // a test that
             // fixes it.
  VOLATILE, //
  SYNCHRONIZED, //
  NATIVE, //
  STRICTFP, //
  ;
  public static Modifiers find(final String modifier) {
    for (Modifiers $ : Modifiers.values())
      if (modifier.equals($.toString().toLowerCase()))
        return $;
    return null;
  }

  public static boolean gt(final String modifier1, final String modifier2) {
    return gt(find(modifier1), find(modifier2));
  }

  private static boolean gt(Modifiers m1, Modifiers m2) {
    return m1.ordinal() > m2.ordinal();
  }
}