package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Rename unused variable to double underscore "__" VariableChangeName instead
 * of ReplaceCurrentNodeExclude
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
public final class SingelVariableDeclarationUnderscoreDoubled extends ReplaceCurrentNodeExclude<SingleVariableDeclaration>
    //
    implements TipperCategory.Annonimization {
  static final boolean BY_ANNOTATION = false;

  public static boolean isUsed(final MethodDeclaration d, final SimpleName n) {
    return !collect.usesOf(n).in(body(d)).isEmpty();
  }

  public static boolean suppressing(final SingleVariableDeclaration ¢) {
    for (final Annotation $ : annotations(¢)) {
      if (!"SuppressWarnings".equals($.getTypeName() + ""))
        continue;
      if (iz.singleMemberAnnotation($))
        return suppresssing(az.singleMemberAnnotation($));
      if (suppressing(az.normalAnnotation($)))
        return true;
    }
    return false;
  }

  static MethodDeclaration getMethod(@NotNull final SingleVariableDeclaration ¢) {
    final ASTNode $ = ¢.getParent();
    return !($ instanceof MethodDeclaration) ? null : (MethodDeclaration) $;
  }

  private static boolean isUnused(final Expression ¢) {
    return iz.literal("unused", ¢);
  }

  @NotNull
  private static ASTNode replace(@NotNull final SingleVariableDeclaration ¢) {
    final SingleVariableDeclaration $ = ¢.getAST().newSingleVariableDeclaration();
    $.setName(¢.getAST().newSimpleName(unusedVariableName()));
    $.setFlags($.getFlags());
    $.setInitializer($.getInitializer());
    $.setType(copy.of(¢.getType()));
    $.setVarargs(¢.isVarargs());
    copy.modifiers(step.extendedModifiers(¢), extendedModifiers($));
    return $;
  }

  private static boolean suppressing(final ArrayInitializer ¢) {
    return expressions(¢).stream().anyMatch(SingelVariableDeclarationUnderscoreDoubled::isUnused);
  }

  private static boolean suppressing(final Expression ¢) {
    return iz.literal("unused", ¢) || iz.arrayInitializer(¢) && suppressing(az.arrayInitializer(¢));
  }

  private static boolean suppressing(@Nullable final NormalAnnotation a) {
    return a != null && values(a).stream().anyMatch(λ -> iz.identifier("value", λ.getName()) && isUnused(λ.getValue()));
  }

  private static boolean suppresssing(@NotNull final SingleMemberAnnotation ¢) {
    return suppressing(¢.getValue());
  }

  private static String unusedVariableName() {
    return "__";
  }

  @NotNull
  @Override public String description(@NotNull final SingleVariableDeclaration ¢) {
    return "Rename unused variable " + ¢.getName().getIdentifier() + " to " + unusedVariableName();
  }

  @Nullable
  @Override public ASTNode replacement(@NotNull final SingleVariableDeclaration ¢) {
    return replacement(¢, null);
  }

  @Override @SuppressWarnings("unused") public ASTNode replacement(@NotNull final SingleVariableDeclaration $, @Nullable final ExclusionManager m) {
    final MethodDeclaration method = getMethod($);
    if (method == null || body(method) == null)
      return null;
    for (final SingleVariableDeclaration ¢ : parameters(method))
      if (unusedVariableName().equals(¢.getName().getIdentifier()))
        return null;
    if (BY_ANNOTATION && !suppressing($) || isUsed(method, $.getName()) || !JohnDoe.property($.getType(), $.getName()))
      return null;
    if (m != null)
      parameters(method).stream().filter(λ -> !$.equals(λ)).forEach(m::exclude);
    return replace($);
  }
}
