package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** convert
 *
 * <pre>
 * <b>abstract</b> <b>interface</b> a
 * {}
 * </pre>
 *
 * to
 *
 * <pre>
 * <b>interface</b> a {}
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public class BodyDeclarationRemoveModifiers<N extends BodyDeclaration> extends Wring.ReplaceCurrentNode<N> implements Kind.SyntacticBaggage {
  private static Set<Modifier> matches(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ms) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier m : modifiers(¢))
      if (test(m, ms))
        $.add((Modifier) m);
    return $;
  }

  private static Set<Modifier> matches(final List<IExtendedModifier> ms, final Set<Predicate<Modifier>> ps) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier m : ms)
      if (test(m, ps))
        $.add((Modifier) m);
    return $;
  }

  private static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ms) {
    return matches(modifiers(¢), ms);
  }

  private static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = modifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ms))
        ¢.remove();
    return $;
  }

  private static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (modifiers(¢).isEmpty())
      return $;
    if (iz.enumDeclaration(¢))
      $.add(Modifier::isStatic);
    if (iz.isInterface(¢) || ¢ instanceof AnnotationTypeDeclaration) {
      $.add(Modifier::isStatic);
      $.add(Modifier::isAbstract);
    }
    if (iz.isMethodDeclaration(¢) && (iz.isPrivate(¢) || iz.isStatic(¢)))
      $.add(Modifier::isFinal);
    final ASTNode container = hop.containerType(¢);
    if (container == null)
      return $;
    if (iz.abstractTypeDeclaration(container) && iz.isFinal(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      $.add(Modifier::isFinal);
    if (iz.isInterface(container)) {
      $.add(Modifier::isPublic);
      $.add(Modifier::isPrivate);
      $.add(Modifier::isProtected);
      if (iz.isMethodDeclaration(¢))
        $.add(Modifier::isAbstract);
    }
    if (iz.enumDeclaration(container))
      $.add(Modifier::isProtected);
    if (iz.anonymousClassDeclaration(container)) {
      $.add(Modifier::isPrivate);
      if (iz.isMethodDeclaration(¢))
        $.add(Modifier::isFinal);
      if (iz.enumConstantDeclaration(hop.containerType(container)))
        $.add(Modifier::isProtected);
    }
    return $;
  }

  private static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return matches(¢, redundancies(¢));
  }

  private static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ms) {
    return m instanceof Modifier && test((Modifier) m, ms);
  }

  private static boolean test(final Modifier m, final Set<Predicate<Modifier>> ms) {
    for (final Predicate<Modifier> p : ms)
      if (p.test(m))
        return true;
    return false;
  }

  @Override String description(final BodyDeclaration ¢) {
    return "Remove redundant " + redundants(¢) + " modifier(s) from declaration";
  }

  @Override BodyDeclaration replacement(final BodyDeclaration $) {
    return prune(duplicate.of($), redundancies($));
  }

  @Override boolean scopeIncludes(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> ps = redundancies(¢);
    return !ps.isEmpty() && !matchess(¢, ps).isEmpty();
  }

  // @formatter:off
  public static class OfAnnotation extends BodyDeclarationRemoveModifiers<AnnotationTypeDeclaration> { /* empty */ }

  public static class OfEnum extends BodyDeclarationRemoveModifiers<TypeDeclaration> { /* empty */ }

  public static class OfEnumConstant extends BodyDeclarationRemoveModifiers<EnumConstantDeclaration> { /* empty */ }

  public static class OfField extends BodyDeclarationRemoveModifiers<FieldDeclaration> { /* empty */ }

  public static class OfMethod extends BodyDeclarationRemoveModifiers<MethodDeclaration> { /* empty */ }

  public static class OfType extends BodyDeclarationRemoveModifiers<TypeDeclaration> { /* empty */ }
  // @formatter:on
}
