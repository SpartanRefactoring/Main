package il.org.spartan.spartanizer.wringing;

import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

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
  final static Comparator<IExtendedModifier> comp = (final IExtendedModifier m1, final IExtendedModifier m2) -> ModifiersOrdering.compare(m1, m2);

  private static boolean isSorted(final List<? extends IExtendedModifier> ms) {
    ModifiersOrdering previous = ModifiersOrdering.$ANNOTATION$;
    for (IExtendedModifier current : ms)
      if (ModifiersOrdering.greaterThanOrEquals(current, previous))
        previous = ModifiersOrdering.find(current);
      else
        return false;
    return true;
  }

  @Override public String description(final N n) {
    return "Sort modifiers of " + extract.category(n) + " " +  extract.name(n) + " (" + extract.modifiers(n) + "->" + sort(extract.modifiers(n)) + ")";
  }

  @Override public N replacement(final N $) {
    return isSorted(step.extendedModifiers($)) ? null : go(duplicate.of($));
  }

  N go(final N $) {
    final List<IExtendedModifier> ms = new ArrayList<>(canonicalModifiers($));
    System.out.println(ms);
    step.extendedModifiers($).clear();
    step.extendedModifiers($).addAll(ms);
    return $;
  }

  private List<? extends IExtendedModifier> canonicalModifiers(final N $) {
    return sort(extendedModifiers($));
  }

  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ms) {
    return ms.stream().sorted(comp).distinct().collect(Collectors.toList());
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