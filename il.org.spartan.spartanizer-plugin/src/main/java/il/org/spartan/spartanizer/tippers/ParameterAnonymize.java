package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Rename unused parameter to double underscore "__" VariableChangeName instead
 * of ReplaceCurrentNodeSpanning
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
public final class ParameterAnonymize extends ReplaceCurrentNodeSpanning<SingleVariableDeclaration>
    //
    implements Nominal.Anonymization {
  private static final long serialVersionUID = 0x238DC1F9DD6723DAL;
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
  @Override protected ASTNode[] span() {
    final List<SingleVariableDeclaration> ret = step.parameters(getMethod(current));
    return new ASTNode[] { the.firstOf(ret), the.lastOf(ret) };
  }
  static MethodDeclaration getMethod(final SingleVariableDeclaration ¢) {
    final ASTNode ret = ¢.getParent();
    return !(ret instanceof MethodDeclaration) ? null : (MethodDeclaration) ret;
  }
  private static boolean isUnused(final Expression ¢) {
    return iz.literal("unused", ¢);
  }
  private static ASTNode replace(final SingleVariableDeclaration ¢) {
    final SingleVariableDeclaration ret = ¢.getAST().newSingleVariableDeclaration();
    ret.setName(¢.getAST().newSimpleName(notation.anonymous));
    ret.setFlags(ret.getFlags());
    ret.setInitializer(ret.getInitializer());
    ret.setType(copy.of(¢.getType()));
    ret.setVarargs(¢.isVarargs());
    copy.modifiers(extendedModifiers(¢), extendedModifiers(ret));
    return ret;
  }
  private static boolean suppressing(final ArrayInitializer ¢) {
    return expressions(¢).stream().anyMatch(ParameterAnonymize::isUnused);
  }
  private static boolean suppressing(final Expression ¢) {
    return iz.literal("unused", ¢) || iz.arrayInitializer(¢) && suppressing(az.arrayInitializer(¢));
  }
  private static boolean suppressing(final NormalAnnotation a) {
    return a != null && values(a).stream().anyMatch(λ -> iz.identifier("value", λ.getName()) && isUnused(λ.getValue()));
  }
  private static boolean suppresssing(final SingleMemberAnnotation ¢) {
    return suppressing(¢.getValue());
  }
  @Override public String description(final SingleVariableDeclaration ¢) {
    return "Anonymize parameter " + ¢.getName().getIdentifier();
  }
  @Override @SuppressWarnings("unused") public ASTNode replacement(final SingleVariableDeclaration $) {
    final MethodDeclaration ret = getMethod($);
    if (ret == null || body(ret) == null)
      return null;
    for (final SingleVariableDeclaration ¢ : parameters(ret))
      if (notation.anonymous.equals(¢.getName().getIdentifier()))
        return null;
    return BY_ANNOTATION && !suppressing($) || isUsed(ret, $.getName()) || !JohnDoe.property($.getType(), $.getName()) ? null : replace($);
  }
}
