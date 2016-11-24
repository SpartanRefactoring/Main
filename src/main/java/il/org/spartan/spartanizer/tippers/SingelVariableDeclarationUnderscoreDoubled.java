package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Rename unused variable to double underscore "__" VariableChangeName instead
 * of ReplaceCurrentNodeExclude
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
public final class SingelVariableDeclarationUnderscoreDoubled extends ReplaceCurrentNodeExclude<SingleVariableDeclaration>
    implements TipperCategory.Annonimization {
  static final boolean BY_ANNOTATION = false;

  public static boolean isUsed(final MethodDeclaration d, final SimpleName n) {
    return !Collect.usesOf(n).in(d.getBody()).isEmpty();
  }

  public static boolean suppressing(final SingleVariableDeclaration d) {
    for (final Annotation ¢ : annotations(d)) {
      if (!"SuppressWarnings".equals(¢.getTypeName() + ""))
        continue;
      if (iz.singleMemberAnnotation(¢))
        return suppresssing(az.singleMemberAnnotation(¢));
      if (suppressing(az.normalAnnotation(¢)))
        return true;
    }
    return false;
  }

  static MethodDeclaration getMethod(final SingleVariableDeclaration ¢) {
    final ASTNode $ = ¢.getParent();
    return $ == null || !($ instanceof MethodDeclaration) ? null : (MethodDeclaration) $;
  }

  private static boolean isUnused(final Expression ¢) {
    return iz.literal("unused", ¢);
  }

  private static ASTNode replace(final SingleVariableDeclaration ¢) {
    final SingleVariableDeclaration $ = ¢.getAST().newSingleVariableDeclaration();
    $.setName(¢.getAST().newSimpleName(unusedVariableName()));
    $.setFlags($.getFlags());
    $.setInitializer($.getInitializer());
    $.setType(duplicate.of(¢.getType()));
    $.setVarargs(¢.isVarargs());
    duplicate.modifiers(step.extendedModifiers(¢), extendedModifiers($));
    return $;
  }

  private static boolean suppressing(final ArrayInitializer i) {
    for (final Expression ¢ : expressions(i))
      if (isUnused(¢))
        return true;
    return false;
  }

  private static boolean suppressing(final Expression ¢) {
    return iz.literal("unused", ¢) || iz.arrayInitializer(¢) && suppressing(az.arrayInitializer(¢));
  }

  private static boolean suppressing(final NormalAnnotation a) {
    if (a == null)
      return false;
    for (final MemberValuePair ¢ : values(a)) {
      if (!iz.identifier("value", ¢.getName()))
        continue;
      if (isUnused(¢.getValue()))
        return true;
    }
    return false;
  }

  private static boolean suppresssing(final SingleMemberAnnotation ¢) {
    return suppressing(¢.getValue());
  }

  private static String unusedVariableName() {
    return "__";
  }

  @Override public String description(final SingleVariableDeclaration ¢) {
    return "Rename unused variable " + ¢.getName().getIdentifier() + " to " + unusedVariableName();
  }

  @Override public ASTNode replacement(final SingleVariableDeclaration ¢) {
    return replacement(¢, null);
  }

  @Override @SuppressWarnings("unused") public ASTNode replacement(final SingleVariableDeclaration d, final ExclusionManager m) {
    final MethodDeclaration method = getMethod(d);
    if (method == null || method.getBody() == null)
      return null;
    for (final SingleVariableDeclaration ¢ : parameters(method))
      if (unusedVariableName().equals(¢.getName().getIdentifier()))
        return null;
    if (BY_ANNOTATION && !suppressing(d) || isUsed(method, d.getName()) || !isJohnDoe(d.getType(), d.getName()))
      return null;
    if (m != null)
      for (final SingleVariableDeclaration ¢ : parameters(method))
        if (!d.equals(¢))
          m.exclude(¢);
    return replace(d);
  }
}
