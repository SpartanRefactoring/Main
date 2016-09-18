package il.org.spartan.spartanizer.wringing;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016 */
public abstract class AbstractBodyDeclarationSortModifiers<N extends BodyDeclaration> //
    extends ReplaceCurrentNode<N> implements Kind.Collapse {
  static Comparator<Modifier> comp = (final Modifier m1, final Modifier m2) -> {
    return m1.isAnnotation() && m2.isAnnotation() ? 0
        : m1.isAnnotation() && m2.isModifier() ? -1 : m2.isAnnotation() && m1.isModifier() ? 1 : Modifiers.gt(m1 + "", m2 + "");
  };

  private static boolean Sorted(final List<Modifier> ms) {
    final List<Modifier> ¢ = new ArrayList<>(ms);
    Collections.sort(¢, comp);
    return ms.equals(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "Sort Modifiers as defined at Modifier.class";
  }

  @Override public N replacement(final N $) {
    return Sorted(extract.modifiers($)) ? null : go(duplicate.of($));
  }

  N go(final N $) {
    final List<Modifier> ms = new ArrayList<>(extract.modifiers($));
    Collections.sort(ms, comp);
    extract.modifiers($).clear();
    extract.modifiers($).addAll(ms);
    return $;
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