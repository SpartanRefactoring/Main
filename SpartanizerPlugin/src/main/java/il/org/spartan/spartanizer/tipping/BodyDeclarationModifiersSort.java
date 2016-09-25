package il.org.spartan.spartanizer.tipping;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016 */
public abstract class BodyDeclarationModifiersSort<N extends BodyDeclaration> //
    extends ReplaceCurrentNode<N> implements Kind.Sorting {
  public static final class ofAnnotation extends BodyDeclarationModifiersSort<AnnotationTypeDeclaration> { //
  }

  public static final class ofAnnotationTypeMember extends BodyDeclarationModifiersSort<AnnotationTypeMemberDeclaration> { //
  }

  public static final class ofEnum extends BodyDeclarationModifiersSort<EnumDeclaration> { //
  }

  public static final class ofEnumConstant extends BodyDeclarationModifiersSort<EnumConstantDeclaration> { //
  }

  public static final class ofField extends BodyDeclarationModifiersSort<FieldDeclaration> { //
  }

  public static final class ofInitializer extends BodyDeclarationModifiersSort<Initializer> { //
  }

  public static final class ofMethod extends BodyDeclarationModifiersSort<MethodDeclaration> { //
  }

  public static final class ofType extends BodyDeclarationModifiersSort<TypeDeclaration> { //
  }

  static final Comparator<IExtendedModifier> comp = (final IExtendedModifier m1, final IExtendedModifier m2) -> IExtendedModifiersOrdering.compare(m1,
      m2);

  private static boolean isSortedAndDistinct(final List<? extends IExtendedModifier> ms) {
    IExtendedModifiersOrdering previous = IExtendedModifiersOrdering.Override;
    for (final IExtendedModifier current : ms) {
      if (!IExtendedModifiersOrdering.greaterThan(current, previous))
        return false;
      previous = IExtendedModifiersOrdering.find(current);
    }
    return true;
  }

  private static boolean pred(final IExtendedModifier m, final boolean[] bitMap) {
    final boolean $ = !bitMap[IExtendedModifiersOrdering.ordinal(m)];
    bitMap[IExtendedModifiersOrdering.ordinal(m)] = true;
    // Can't compare different user defined annotations! So avoid removing them.
    bitMap[IExtendedModifiersOrdering.userDefinedAnnotationsOrdinal()] = false;
    return $;
  }

  private static List<? extends IExtendedModifier> removeSame(final List<? extends IExtendedModifier> $) {
    boolean[] bitMap = IExtendedModifiersOrdering.bitMap();
    List<? extends IExtendedModifier> l =  $.stream().filter(m -> pred(m, bitMap)).collect(Collectors.toList());
    return l;
  }

  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    return ¢.stream().sorted(comp).collect(Collectors.toList());
  }

  @Override public String description(final N ¢) {
    return "Sort modifiers of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.modifiers(¢) + "->" + sort(extract.modifiers(¢)) + ")";
  }

  N go(final N $) {
    final List<IExtendedModifier> ms = new ArrayList<>(sortedModifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(ms);
    return $;
  }

  @Override protected boolean prerequisite(final N ¢) {
    return !isSortedAndDistinct(extendedModifiers(¢));
  }

  @Override public N replacement(final N $) {
    return go(duplicate.of($));
  }

  private List<? extends IExtendedModifier> sortedModifiers(final N $) {
    return sort(removeSame(extendedModifiers($)));
  }
}
