package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code abstract</b> <b>interface</b>a{}</code> to
 * {@code interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierRedundant extends CarefulTipper<Modifier>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x131323DBCDA3200DL;
  public static final Predicate<Modifier> isAbstract = Modifier::isAbstract;
  public static final Predicate<Modifier> isFinal = Modifier::isFinal;
  public static final Predicate<Modifier> isPrivate = Modifier::isPrivate;
  public static final Predicate<Modifier> isProtected = Modifier::isProtected;
  public static final Predicate<Modifier> isPublic = Modifier::isPublic;
  public static final Predicate<Modifier> isStatic = Modifier::isStatic;

  @Override public String description(final Modifier ¢) {
    return "Remove redundant [" + ¢ + "] modifier";
  }
  @Override public String description() {
    return "Remove redundant modifier";
  }
  @Override public Tip tip(final Modifier ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.getListRewrite(parent(¢), az.bodyDeclaration(parent(¢)).getModifiersProperty()).remove(¢, g);
      }
    };
  }
  @Override public boolean prerequisite(final Modifier ¢) {
    return ModifiersRedundancy.test(¢, ModifiersRedundancy.redundancies(az.bodyDeclaration(parent(¢))));
  }
  public static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return matches(¢, redundancies(¢));
  }
  public static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> ret = new LinkedHashSet<>();
    if (extendedModifiers(¢) == null || extendedModifiers(¢).isEmpty())
      return ret;
    if (iz.enumDeclaration(¢))
      ret.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.enumConstantDeclaration(¢)) {
      ret.addAll(ModifiersRedundancy.visibilityModifiers);
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
        ret.addAll(ModifiersRedundancy.visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        ret.add(isFinal);
    }
    if (iz.interface¢(container)) {
      ret.addAll(ModifiersRedundancy.visibilityModifiers);
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
        ret.addAll(ModifiersRedundancy.visibilityModifiers);
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
  public static BodyDeclaration prune(final BodyDeclaration ret, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers(ret).iterator(); ¢.hasNext();)
      if (ModifiersRedundancy.test(¢.next(), ms))
        ¢.remove();
    return ret;
  }
  public static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ms) {
    return matches(extendedModifiers(¢), ms);
  }
  public static Set<Modifier> matches(final List<IExtendedModifier> ms, final Set<Predicate<Modifier>> ps) {
    return ms.stream().filter(λ -> ModifiersRedundancy.test(λ, ps)).map(Modifier.class::cast).collect(toSet());
  }
  public static Set<Modifier> matches(final BodyDeclaration d, final Set<Predicate<Modifier>> ms) {
    return extendedModifiers(d).stream().filter(λ -> ModifiersRedundancy.test(λ, ms)).map(Modifier.class::cast)
        .collect(toCollection(LinkedHashSet::new));
  }
}
