package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
final class RedudnatModifier extends CarefulTipper<Modifier> implements TipperCategory.SyntacticBaggage {
  private static final Predicate<Modifier> isAbstract = Modifier::isAbstract;
  private static final Predicate<Modifier> isFinal = Modifier::isFinal;
  private static final Predicate<Modifier> isPrivate = Modifier::isPrivate;
  private static final Predicate<Modifier> isProtected = Modifier::isProtected;
  private static final Predicate<Modifier> isStatic = Modifier::isStatic;
  private static final Predicate<Modifier> isPublic = Modifier::isPublic;

  private static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (extendedModifiers(¢).isEmpty())
      return $;
    if (iz.enumDeclaration(¢))
      $.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.interface¢(¢) || ¢ instanceof AnnotationTypeDeclaration)
      $.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.isMethodDeclaration(¢) && (iz.private¢(¢) || iz.static¢(¢)))
      $.add(isFinal);
    if (iz.methodDeclaration(¢) && hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    final ASTNode container = hop.containerType(¢);
    if (container == null)
      return $;
    if (iz.abstractTypeDeclaration(container) && iz.final¢(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      $.add(isFinal);
    if (iz.enumDeclaration(container))
      $.add(isProtected);
    if (iz.interface¢(container)) {
      $.addAll(as.list(isPublic, isPrivate, isProtected));
      if (iz.isMethodDeclaration(¢))
        $.add(isAbstract);
      if (iz.fieldDeclaration(¢))
        $.add(isStatic);
    }
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

  private static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ms))
        ¢.remove();
    return $;
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

  @Override public String description(final Modifier ¢) {
    return "Remove redundant [" + ¢ + "] modifier";
  }

  @Override public String description() {
    return "Remove redundant modifier";
  }

  @Override public Tip tip(final Modifier ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite x = r.getListRewrite(parent(¢), az.bodyDeclaration(parent(¢)).getModifiersProperty());
        x.remove(¢, g);
      }
    };
  }

  @Override public boolean prerequisite(final Modifier ¢) {
    final BodyDeclaration d = az.bodyDeclaration(parent(¢));
    final Set<Predicate<Modifier>> ps = redundancies(d);
    return !ps.isEmpty() && !test(¢, ps);
  }
}
