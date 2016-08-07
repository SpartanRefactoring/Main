package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import java.util.*;
import java.util.function.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
 *
 * <pre>
 * <b>abstract</b>abstract <b>interface</b> a
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
public abstract class RedundantModifiers<N extends BodyDeclaration> extends Wring.ReplaceCurrentNode<N> {
  private static Set<Modifier> matches(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ps) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier m : expose.modifiers(¢))
      if (test(m, ps))
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
  private static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ps) {
    return matches(expose.modifiers(¢), ps);
  }
  private static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ps) {
    for (final Iterator<IExtendedModifier> ¢ = expose.modifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ps))
        ¢.remove();
    return $;
  }
  private static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (expose.modifiers(¢).isEmpty())
      return $;
    if (isEnumDeclaration(¢))
      $.add(Modifier::isStatic);
    if (isInterface(¢) || ¢ instanceof AnnotationTypeDeclaration) {
      $.add(Modifier::isStatic);
      $.add(Modifier::isAbstract);
    }
    if (isMethodDeclaration(¢) && (isPrivate(¢) || isStatic(¢)))
      $.add(Modifier::isFinal);
    final ASTNode container = extract.containerType(¢);
    if (container == null)
      return $;
    if (isAbstractTypeDeclaration(container) && isFinal(asAbstractTypeDeclaration(container)) && isMethodDeclaration(¢))
      $.add(Modifier::isFinal);
    if (isInterface(container)) {
      $.add(Modifier::isPublic);
      $.add(Modifier::isPublic);
      $.add(Modifier::isPrivate);
      $.add(Modifier::isProtected);
      if (isMethodDeclaration(¢))
        $.add(Modifier::isAbstract);
    }
    if (isAnonymousClassDeclaration(container)) {
      $.add(Modifier::isPrivate);
      $.add(Modifier::isStatic);
      if (isMethodDeclaration(¢))
        $.add(Modifier::isFinal);
    }
    return $;
  }
  private static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return matches(¢, redundancies(¢));
  }
  private static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ps) {
    return m instanceof Modifier && test((Modifier) m, ps);
  }
  private static boolean test(final Modifier m, final Set<Predicate<Modifier>> ps) {
    for (final Predicate<Modifier> p : ps)
      if (p.test(m))
        return true;
    return false;
  }
  @Override String description(final BodyDeclaration ¢) {
    return "Remove redundant " + redundants(¢) + " modifier(s) from declaration";
  }
  @Override BodyDeclaration replacement(final BodyDeclaration $) {
    return prune(duplicate($), redundancies($));
  }
  @Override boolean scopeIncludes(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> ps = redundancies(¢);
    return !ps.isEmpty() && !matchess(¢, ps).isEmpty();
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REMOVE_SYNTACTIC_BAGGAGE;
  }

  public static class OfAnnotation extends RedundantModifiers<AnnotationTypeDeclaration> { /* empty */
  }

  // @formatter:on
  public static class OfEnum extends RedundantModifiers<TypeDeclaration> { /* empty */
  }

  public static class OfField extends RedundantModifiers<FieldDeclaration> { /* empty */
  }

  //@formatter:off
  public static class OfMethod extends RedundantModifiers<MethodDeclaration> { /* empty */ }
  public static class OfType extends RedundantModifiers<TypeDeclaration> { /* empty */ }
}
