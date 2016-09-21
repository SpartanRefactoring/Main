package il.org.spartan.spartanizer.wrings;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public abstract class BodyDeclarationModifiersPrune<N extends BodyDeclaration> extends ReplaceCurrentNode<N> implements Kind.SyntacticBaggage {
  private static final Predicate<Modifier> isAbstract = Modifier::isAbstract;
  private static final Predicate<Modifier> isFinal = Modifier::isFinal;
  private static final Predicate<Modifier> isPrivate = Modifier::isPrivate;
  private static final Predicate<Modifier> isProtected = Modifier::isProtected;
  private static final Predicate<Modifier> isStatic = Modifier::isStatic;
  private static final Predicate<Modifier> isPublic = Modifier::isPublic;

  static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (extendedModifiers(¢).isEmpty())
      return $;
    if (iz.enumDeclaration(¢)) {
      $.add(isStatic);
      $.add(isAbstract);
      $.add(isFinal);
    }
    if (iz.isInterface(¢) || ¢ instanceof AnnotationTypeDeclaration) {
      $.add(isStatic);
      $.add(isAbstract);
    }
    if (iz.isMethodDeclaration(¢) && (iz.isPrivate(¢) || iz.isStatic(¢)))
      $.add(isFinal);
    final ASTNode container = hop.containerType(¢);
    if (container == null)
      return $;
    if (iz.abstractTypeDeclaration(container) && iz.isFinal(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      $.add(isFinal);
    if (iz.isInterface(container)) {
      $.add(isPublic);
      $.add(isPrivate);
      $.add(isProtected);
      if (iz.isMethodDeclaration(¢))
        $.add(isAbstract);
    }
    if (iz.enumDeclaration(container))
      $.add(isProtected);
    if (iz.anonymousClassDeclaration(container)) {
      $.add(isPrivate);
      if (iz.isMethodDeclaration(¢))
        $.add(isFinal);
      if (iz.enumConstantDeclaration(hop.containerType(container)))
        $.add(isProtected);
    }
    if (iz.methodDeclaration(¢) && hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    return $;
  }

  private static boolean hasSafeVarags(final MethodDeclaration d) {
    for (final Annotation ¢ : extract.annotations(d))
      if (iz.identifier("SafeVarargs", ¢.getTypeName()))
        return true;
    return false;
  }

  private static Set<Modifier> matches(final BodyDeclaration d, final Set<Predicate<Modifier>> ms) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier ¢ : extendedModifiers(d))
      if (test(¢, ms))
        $.add((Modifier) ¢);
    return $;
  }

  private static Set<Modifier> matches(final List<IExtendedModifier> ms, final Set<Predicate<Modifier>> ps) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier ¢ : ms)
      if (test(¢, ps))
        $.add((Modifier) ¢);
    return $;
  }

  private static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ms) {
    return matches(extendedModifiers(¢), ms);
  }

  private static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ms))
        ¢.remove();
    return $;
  }

  private static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return matches(¢, redundancies(¢));
  }

  private static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ms) {
    return m instanceof Modifier && test((Modifier) m, ms);
  }

  private static boolean test(final Modifier m, final Set<Predicate<Modifier>> ms) {
    for (final Predicate<Modifier> ¢ : ms)
      if (¢.test(m))
        return true;
    return false;
  }

  @Override public String description(final BodyDeclaration ¢) {
    return "Remove redundant " + redundants(¢) + " modifier(s) from declaration";
  }

  @Override public boolean prerequisite(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> ps = redundancies(¢);
    return !ps.isEmpty() && !matchess(¢, ps).isEmpty();
  }

  @Override public BodyDeclaration replacement(final BodyDeclaration $) {
    return prune(duplicate.of($), redundancies($));
  }
}
