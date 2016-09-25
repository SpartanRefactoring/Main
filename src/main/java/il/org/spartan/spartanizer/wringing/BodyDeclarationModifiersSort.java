package il.org.spartan.spartanizer.wringing;

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
  static final Comparator<IExtendedModifier> comp = (final IExtendedModifier m1, final IExtendedModifier m2) -> IExtendedModifiersOrdering.compare(m1,
      m2);

  public static boolean contains(final List<IExtendedModifier> ms, final IExtendedModifier m) {
    for (final IExtendedModifier ¢ : ms)
      if (IExtendedModifiersOrdering.compare(m, ¢) == 0)
        return true;
    return false;
  }

  private static boolean isSortedAndDistinct(final List<? extends IExtendedModifier> ms) {
    IExtendedModifiersOrdering previous = IExtendedModifiersOrdering.Override;
    for (final IExtendedModifier current : ms) {
      if (!IExtendedModifiersOrdering.greaterThan(current, previous))
        return false;
      previous = IExtendedModifiersOrdering.find(current);
    }
    return true;
  }

  private static List<? extends IExtendedModifier> removeSame(final List<? extends IExtendedModifier> $) {
    final List<IExtendedModifier> n = new ArrayList<>();
    for (final IExtendedModifier m : $)
      if (!contains(n, m) || IExtendedModifiersOrdering.isUserDefinedAnnotation(m))
        n.add(m);
    return n;
  }

  // TODO: Dan, just look at this! every time in the future we have to sort
  // something, we just make a list
  // of the elements, define comparator and it's ready! Beautiful.
  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    return ¢.stream().sorted(comp).collect(Collectors.toList());
  }

  @Override public String description(final N ¢) {
    return "Sort modifiers of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.modifiers(¢) + "->" + sort(extract.modifiers(¢)) + ")";
  }

  @Override public N replacement(final N $) {
    return go(duplicate.of($));
  }

  @Override protected boolean prerequisite(final N ¢) {
    return !isSortedAndDistinct(extendedModifiers(¢));
  }

  N go(final N $) {
    final List<IExtendedModifier> ms = new ArrayList<>(sortedModifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(ms);
    return $;
  }

  private List<? extends IExtendedModifier> sortedModifiers(final N $) {
    return sort(removeSame(extendedModifiers($)));
  }

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
}
