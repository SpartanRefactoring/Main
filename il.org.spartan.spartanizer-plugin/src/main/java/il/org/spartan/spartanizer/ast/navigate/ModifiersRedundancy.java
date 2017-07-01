package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
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

  public static BodyDeclaration prune(final BodyDeclaration ret, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers(ret).iterator(); ¢.hasNext();)
      if (test(¢.next(), ms))
        ¢.remove();
    return ret;
  }
  public static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> ret = new LinkedHashSet<>();
    if (extendedModifiers(¢) == null || extendedModifiers(¢).isEmpty())
      return ret;
    if (iz.enumDeclaration(¢))
      ret.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.enumConstantDeclaration(¢)) {
      ret.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        ret.addAll(as.list(isFinal, isStatic, isAbstract));
    }
    if (iz.interface¢(¢) || ¢ instanceof AnnotationTypeDeclaration)
      ret.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.isMethodDeclaration(¢) && (iz.private¢(¢) || iz.static¢(¢)))
      ret.add(isFinal);
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      ret.remove(isFinal);
    final ASTNode container = containing.typeDeclaration(¢);
    if (container == null)
      return ret;
    if (iz.annotationTypeDeclaration(container))
      ret.add(isFinal);
    if (iz.abstractTypeDeclaration(container) && iz.final¢(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      ret.add(isFinal);
    if (iz.enumDeclaration(container)) {
      ret.add(isProtected);
      if (iz.constructor(¢))
        ret.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        ret.add(isFinal);
    }
    if (iz.interface¢(container)) {
      ret.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢)) {
        ret.add(isAbstract);
        ret.add(isFinal);
      }
      if (iz.fieldDeclaration(¢)) {
        ret.add(isStatic);
        ret.add(isFinal);
      }
      if (iz.abstractTypeDeclaration(¢))
        ret.add(isStatic);
    }
    if (iz.anonymousClassDeclaration(container)) {
      if (iz.fieldDeclaration(¢))
        ret.addAll(visibilityModifiers);
      ret.add(isPrivate);
      if (iz.isMethodDeclaration(¢))
        ret.add(isFinal);
      if (iz.enumConstantDeclaration(containing.typeDeclaration(container)))
        ret.add(isProtected);
    }
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      ret.remove(isFinal);
    return ret;
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
