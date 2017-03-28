package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

/**
 * TODO Yossi Gil: document class 
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-28
 */
public enum ModifiersRedundancy {
  ;

  public static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (extendedModifiers(¢) == null || extendedModifiers(¢).isEmpty())
      return $;
    if (iz.enumDeclaration(¢))
      $.addAll(as.list(ModifiersRedundancy.isStatic, ModifiersRedundancy.isAbstract, ModifiersRedundancy.isFinal));
    if (iz.enumConstantDeclaration(¢)) {
      $.addAll(ModifiersRedundancy.visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        $.addAll(as.list(ModifiersRedundancy.isFinal, ModifiersRedundancy.isStatic, ModifiersRedundancy.isAbstract));
    }
    if (iz.interface¢(¢) || ¢ instanceof AnnotationTypeDeclaration)
      $.addAll(as.list(ModifiersRedundancy.isStatic, ModifiersRedundancy.isAbstract, ModifiersRedundancy.isFinal));
    if (iz.isMethodDeclaration(¢) && (iz.private¢(¢) || iz.static¢(¢)))
      $.add(ModifiersRedundancy.isFinal);
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(ModifiersRedundancy.isFinal);
    final ASTNode container = containing.typeDeclaration(¢);
    if (container == null)
      return $;
    if (iz.annotationTypeDeclaration(container))
      $.add(ModifiersRedundancy.isFinal);
    if (iz.abstractTypeDeclaration(container) && iz.final¢(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      $.add(ModifiersRedundancy.isFinal);
    if (iz.enumDeclaration(container)) {
      $.add(ModifiersRedundancy.isProtected);
      if (iz.constructor(¢))
        $.addAll(ModifiersRedundancy.visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        $.add(ModifiersRedundancy.isFinal);
    }
    if (iz.interface¢(container)) {
      $.addAll(ModifiersRedundancy.visibilityModifiers);
      if (iz.isMethodDeclaration(¢)) {
        $.add(ModifiersRedundancy.isAbstract);
        $.add(ModifiersRedundancy.isFinal);
      }
      if (iz.fieldDeclaration(¢)) {
        $.add(ModifiersRedundancy.isStatic);
        $.add(ModifiersRedundancy.isFinal);
      }
      if (iz.abstractTypeDeclaration(¢))
        $.add(ModifiersRedundancy.isStatic);
    }
    if (iz.anonymousClassDeclaration(container)) {
      if (iz.fieldDeclaration(¢))
        $.addAll(ModifiersRedundancy.visibilityModifiers);
      $.add(ModifiersRedundancy.isPrivate);
      if (iz.isMethodDeclaration(¢))
        $.add(ModifiersRedundancy.isFinal);
      if (iz.enumConstantDeclaration(containing.typeDeclaration(container)))
        $.add(ModifiersRedundancy.isProtected);
    }
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(ModifiersRedundancy.isFinal);
    return $;
  }

  public static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return wizard.matches(¢, redundancies(¢));
  }

  public static final Predicate<Modifier> isAbstract = Modifier::isAbstract;
  public static final Predicate<Modifier> isFinal = Modifier::isFinal;
  public static final Predicate<Modifier> isPrivate = Modifier::isPrivate;
  public static final Predicate<Modifier> isProtected = Modifier::isProtected;
  public static final Predicate<Modifier> isPublic = Modifier::isPublic;
  public static final Predicate<Modifier> isStatic = Modifier::isStatic;
  public static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers($).iterator(); ¢.hasNext();)
      if (ModifiersRedundancy.test(¢.next(), ms))
        ¢.remove();
    return $;
  }

  public static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ms) {
    return m instanceof Modifier && wizard.test((Modifier) m, ms);
  }

  public static final List<Predicate<Modifier>> visibilityModifiers = as.list(isPublic, isPrivate, isProtected);
}
