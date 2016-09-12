package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016 */
public abstract class AbstractBodyDeclarationSortModifiers<N extends BodyDeclaration> //
    extends Wring.ReplaceCurrentNode<N> implements Kind.Canonicalization {
  static Comparator<IExtendedModifier> comp = (final IExtendedModifier m1, final IExtendedModifier m2) -> {
    return m1.isAnnotation() && m2.isAnnotation() ? 0
        : m1.isAnnotation() && m2.isModifier() ? -1 : m2.isAnnotation() && m1.isModifier() ? 1 : Modifiers.gt(m1 + "", m2 + "");
  };

  private static boolean Sorted(final List<IExtendedModifier> ms) {
    final List<IExtendedModifier> ¢ = new ArrayList<>(ms);
    Collections.sort(¢, comp);
    return ms.equals(¢);
  }

  @Override String description(@SuppressWarnings("unused") final N __) {
    return "Sort Modifiers as defined at Modifier.class";
  }

  N go(final N $) {
    final List<IExtendedModifier> ms = new ArrayList<>(step.modifiers($));
    Collections.sort(ms, comp);
    step.modifiers($).clear();
    step.modifiers($).addAll(ms);
    return $;
  }

  @Override N replacement(final N $) {
    return Sorted(step.modifiers($)) ? null : go(duplicate.of($));
  }

  public static final class ofAnnotation extends AbstractBodyDeclarationSortModifiers<AnnotationTypeDeclaration> { //
  }

  public static final class ofAnnotationTypeMember extends AbstractBodyDeclarationSortModifiers<AnnotationTypeMemberDeclaration> { //
  }

  public static final class ofEnum extends AbstractBodyDeclarationSortModifiers<EnumDeclaration> { //
  }

  public static final class ofEnumConstant extends AbstractBodyDeclarationSortModifiers<EnumConstantDeclaration> { //
  }

  public static final class ofField extends AbstractBodyDeclarationSortModifiers<FieldDeclaration> { //
  }

  public static final class ofInitializer extends AbstractBodyDeclarationSortModifiers<Initializer> { //
  }

  public static final class ofMethod extends AbstractBodyDeclarationSortModifiers<MethodDeclaration> { //
  }

  public static final class ofType extends AbstractBodyDeclarationSortModifiers<TypeDeclaration> { //
  }
}