package il.org.spartan.spartanizer.java;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Maintain a canonical order of modifiers.
 * @author Yossi Gil
 * @since 2016 */
public enum IExtendedModifiersRank {
  Override, //
  Deprecated, //
  SuppressWarnings, //
  SafeVarargs, //
  FunctionalInterface, //
  $USER_DEFINED_ANNOTATION$, //
  PUBLIC, //
  PROTECTED, //
  PRIVATE, //
  ABSTRACT, //
  STATIC, //
  DEFAULT, //
  FINAL, //
  TRANSIENT, //
  VOLATILE, //
  SYNCHRONIZED, //
  NATIVE, //
  STRICTFP, //
  ;
  public static boolean[] bitMap() {
    final boolean[] $ = new boolean[IExtendedModifiersRank.size()];
    Arrays.fill($, false);
    return $;
  }

  public static int compare(final IExtendedModifier modifier1, final IExtendedModifier modifier2) {
    return compare(rank(modifier1), rank(modifier2));
  }

  public static int compare(final IExtendedModifier m, final IExtendedModifiersRank r) {
    return compare(rank(m), r);
  }

  public static int compare(final String modifier1, final String modifier2) {
    return compare(find(modifier1), find(modifier2));
  }

  public static IExtendedModifiersRank rank(final IExtendedModifier ¢) {
    return find(¢ + "");
  }

  public static boolean greaterThan(final IExtendedModifier m1, final IExtendedModifiersRank m2) {
    return compare(m1, m2) > 0;
  }

  public static boolean greaterThanOrEquals(final IExtendedModifier m1, final IExtendedModifiersRank m2) {
    return compare(m1, m2) >= 0;
  }

  public static boolean isUserDefinedAnnotation(final IExtendedModifier m1) {
    return compare(m1, $USER_DEFINED_ANNOTATION$) == 0;
  }

  public static int ordinal(final IExtendedModifier ¢) {
    return ordinal(¢ + "");
  }

  public static int userDefinedAnnotationsOrdinal() {
    return IExtendedModifiersRank.$USER_DEFINED_ANNOTATION$.ordinal();
  }

  static IExtendedModifiersRank find(final String modifier) {
    for (final IExtendedModifiersRank $ : IExtendedModifiersRank.values())
      if (modifier.equals(($ + "").toLowerCase()) || modifier.equals("@" + $))
        return $;
    return $USER_DEFINED_ANNOTATION$;
  }

  static int ordinal(final String modifier) {
    return find(modifier).ordinal();
  }

  public static int compare(final IExtendedModifiersRank m1, final IExtendedModifiersRank m2) {
    return m1.ordinal() - m2.ordinal();
  }

  private static int size() {
    return IExtendedModifiersRank.values().length;
  }
}