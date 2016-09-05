package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016 */
public abstract class BodyDeclarationSortModifiers<N extends BodyDeclaration> //
    extends Wring.ReplaceCurrentNode<N> implements Kind.Canonicalization {
  static Comparator<IExtendedModifier> comp = (final IExtendedModifier m1, final IExtendedModifier m2) -> {
    return m1.isAnnotation() && m2.isAnnotation() ? 0
        : m1.isAnnotation() && m2.isModifier() ? -1 : m2.isAnnotation() && m1.isModifier() ? 1 : Modifiers.gt("" + m1, "" + m2);
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

  public static final class ofAnnotation extends BodyDeclarationSortModifiers<AnnotationTypeDeclaration> {
  }

  public static final class ofAnnotationTypeMember extends BodyDeclarationSortModifiers<AnnotationTypeMemberDeclaration> {
  }

  public static final class ofEnum extends BodyDeclarationSortModifiers<EnumDeclaration> {
  }

  public static final class ofEnumConstant extends BodyDeclarationSortModifiers<EnumConstantDeclaration> {
  }

  public static final class ofField extends BodyDeclarationSortModifiers<FieldDeclaration> {
  }

  public static final class ofInitializer extends BodyDeclarationSortModifiers<Initializer> {
  }

  public static final class ofMethod extends BodyDeclarationSortModifiers<MethodDeclaration> {
  }

  public static final class ofType extends BodyDeclarationSortModifiers<TypeDeclaration> {
  }
}