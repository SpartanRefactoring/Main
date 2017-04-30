package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;

/** Manager of modifiers's redundancy
 * @author Yossi Gil
 * @since 2017-03-28 */
public enum ModifiersRedundancy {
  ;
  public static final Predicate<Modifier> isAbstract = Modifier::isAbstract;
  public static final Predicate<Modifier> isFinal = Modifier::isFinal;
  public static final Predicate<Modifier> isPrivate = Modifier::isPrivate;
  public static final Predicate<Modifier> isProtected = Modifier::isProtected;
  public static final Predicate<Modifier> isPublic = Modifier::isPublic;
  public static final Predicate<Modifier> isStatic = Modifier::isStatic;
  public static final List<Predicate<Modifier>> visibilityModifiers = as.list(isPublic, isPrivate, isProtected);

  public static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ms))
        ¢.remove();
    return $;
  }

  public static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (extendedModifiers(¢) == null || extendedModifiers(¢).isEmpty())
      return $;
    if (iz.enumDeclaration(¢))
      $.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.enumConstantDeclaration(¢)) {
      $.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        $.addAll(as.list(isFinal, isStatic, isAbstract));
    }
    if (iz.interface¢(¢) || ¢ instanceof AnnotationTypeDeclaration)
      $.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.isMethodDeclaration(¢) && (iz.private¢(¢) || iz.static¢(¢)))
      $.add(isFinal);
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    final ASTNode container = containing.typeDeclaration(¢);
    if (container == null)
      return $;
    if (iz.annotationTypeDeclaration(container))
      $.add(isFinal);
    if (iz.abstractTypeDeclaration(container) && iz.final¢(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      $.add(isFinal);
    if (iz.enumDeclaration(container)) {
      $.add(isProtected);
      if (iz.constructor(¢))
        $.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        $.add(isFinal);
    }
    if (iz.interface¢(container)) {
      $.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢)) {
        $.add(isAbstract);
        $.add(isFinal);
      }
      if (iz.fieldDeclaration(¢)) {
        $.add(isStatic);
        $.add(isFinal);
      }
      if (iz.abstractTypeDeclaration(¢))
        $.add(isStatic);
    }
    if (iz.anonymousClassDeclaration(container)) {
      if (iz.fieldDeclaration(¢))
        $.addAll(visibilityModifiers);
      $.add(isPrivate);
      if (iz.isMethodDeclaration(¢))
        $.add(isFinal);
      if (iz.enumConstantDeclaration(containing.typeDeclaration(container)))
        $.add(isProtected);
    }
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    return $;
  }

  public static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return ModifierRedundant.matches(¢, redundancies(¢));
  }

  public static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ms) {
    return m instanceof Modifier && test((Modifier) m, ms);
  }

  public static boolean test(final Modifier m, final Set<Predicate<Modifier>> ms) {
    return ms.stream().anyMatch(λ -> λ.test(m));
  }
}
