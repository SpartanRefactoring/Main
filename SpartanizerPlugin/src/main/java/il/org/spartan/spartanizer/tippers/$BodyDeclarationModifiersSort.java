package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.java.IExtendedModifiersRank.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @author Dor Ma'ayan
 * @since 2016 */
public abstract class $BodyDeclarationModifiersSort<N extends BodyDeclaration> //
    extends ReplaceCurrentNode<N> implements TipperCategory.Sorting {
  static final Comparator<IExtendedModifier> comp = (m1, m2) -> rank(m1) - rank(m2);

  private static boolean isSortedAndDistinct(final List<? extends IExtendedModifier> ms) {
    int previousRank = -1;
    for (final IExtendedModifier current : ms) {
      final int currentRank = rank(current);
      if (currentRank <= previousRank)
        return false;
      previousRank = currentRank;
    }
    return true;
  }
  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    return pruneDuplicates(¢.stream().sorted(comp).collect(Collectors.toList()));
  }
  private static List<? extends IExtendedModifier> pruneDuplicates(final List<? extends IExtendedModifier> ms) {
    for (int ¢ = 0; ¢ < ms.size(); ++¢)
      while (¢ < ms.size() - 1 && comp.compare(ms.get(¢), ms.get(¢ + 1)) == 0)
        ms.remove(¢ + 1);
    return ms;
  }
  @Override public String description(final N ¢) {
    return "Sort modifiers of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.modifiers(¢) + "->" + sort(extract.modifiers(¢)) + ")";
  }
  @Override public N replacement(final N $) {
    return go(duplicate.of($));
  }
  @Override protected boolean prerequisite(final N ¢) {
    return !extendedModifiers(¢).isEmpty() && !isSortedAndDistinct(extract.modifiers(¢));
  }
  N go(final N $) {
    final List<IExtendedModifier> as = new ArrayList<>(extract.annotations($));
    final List<IExtendedModifier> ms = new ArrayList<>(sortedModifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(as);
    extendedModifiers($).addAll(ms);
    return $;
  }
  private List<? extends IExtendedModifier> sortedModifiers(final N $) {
    return sort(extract.modifiers($));
  }

  public static final class ofAnnotation extends $BodyDeclarationModifiersSort<AnnotationTypeDeclaration> { //
  }

  public static final class ofAnnotationTypeMember extends $BodyDeclarationModifiersSort<AnnotationTypeMemberDeclaration> { //
  }

  public static final class ofEnum extends $BodyDeclarationModifiersSort<EnumDeclaration> { //
  }

  public static final class ofEnumConstant extends $BodyDeclarationModifiersSort<EnumConstantDeclaration> { //
  }

  public static final class ofField extends $BodyDeclarationModifiersSort<FieldDeclaration> { //
  }

  public static final class ofInitializer extends $BodyDeclarationModifiersSort<Initializer> { //
  }

  public static final class ofMethod extends $BodyDeclarationModifiersSort<MethodDeclaration> { //
  }

  public static final class ofType extends $BodyDeclarationModifiersSort<TypeDeclaration> { //
  }
}
