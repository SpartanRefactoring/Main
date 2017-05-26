package il.org.spartan.spartanizer.java;

import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** Maintain a canonical order of modifiers.
 * @author Yossi Gil
 * @since 2016 */
public enum IExtendedModifiersRank {
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
  $USER_DEFINED_ANNOTATION$, //
  ;
  public static int compare(final IExtendedModifiersRank m1, final IExtendedModifiersRank m2) {
    return m1.ordinal() - m2.ordinal();
  }
  public static int compare(final String modifier1, final String modifier2) {
    return compare(find(modifier1), find(modifier2));
  }
  public static IExtendedModifiersRank find(final IExtendedModifier ¢) {
    return find(¢ + "");
  }
  public static boolean isUserDefinedAnnotation(final IExtendedModifier ¢) {
    return rank(¢) == $USER_DEFINED_ANNOTATION$.ordinal();
  }
  public static int ordinal(final IExtendedModifier ¢) {
    return ordinal(¢ + "");
  }
  public static int rank(final IExtendedModifier ¢) {
    return find(¢ + "").ordinal();
  }
  public static int rankAnnotation(final IExtendedModifier ¢) {
    return find("@" + az.annotation(¢).getTypeName()).ordinal();
  }
  public static int userDefinedAnnotationsOrdinal() {
    return IExtendedModifiersRank.$USER_DEFINED_ANNOTATION$.ordinal();
  }
  static IExtendedModifiersRank find(final String modifier) {
    return Stream.of(IExtendedModifiersRank.values()).filter(λ -> modifier.equals((λ + "").toLowerCase()) || modifier.equals("@" + λ)).findFirst()
        .orElse($USER_DEFINED_ANNOTATION$);
  }
  static int ordinal(final String modifier) {
    return find(modifier).ordinal();
  }
}