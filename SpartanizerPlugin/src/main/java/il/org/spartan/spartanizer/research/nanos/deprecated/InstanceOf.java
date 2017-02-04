package il.org.spartan.spartanizer.research.nanos.deprecated;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class InstanceOf extends NanoPatternTipper<InstanceofExpression> {
  static final TypeChecker c = new TypeChecker();

  @Override public boolean canTip(final InstanceofExpression ¢) {
    if (!(type(¢) instanceof SimpleType))
      return false;
    final MethodDeclaration $ = yieldAncestors.untilContainingMethod().from(¢);
    final Javadoc j = $.getJavadoc();
    return (j == null || !(j + "").contains(c.tag())) && c.cantTip($) && !(type(¢) + "").contains(".");
  }

  @Override @NotNull public Tip pattern(@NotNull final InstanceofExpression ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(!iz.parenthesizedExpression(¢.getParent()) ? ¢ : ¢.getParent(), wizard.ast(izMethodName(¢) + "(" + step.left(¢) + ")"), g);
        if (!izMethodExist(¢))
          addizMethod(¢, r, g);
      }
    };
  }

  @Nullable static String izMethodName(final InstanceofExpression ¢) {
    return "iz" + type(¢);
  }

  static boolean izMethodExist(final InstanceofExpression ¢) {
    return step.methods(containingType(¢)).stream().filter(λ -> izMethodName(¢).equals(λ.getName() + "") && booleanType(step.returnType(λ)))
        .count() != 0;
  }

  private static boolean booleanType(final Type returnType) {
    return "boolean".equals(returnType + "");
  }

  static void addizMethod(final InstanceofExpression ¢, @NotNull final ASTRewrite r, final TextEditGroup g) {
    wizard.addMethodToType(containingType(¢), newIzMethod(¢), r, g);
  }

  private static MethodDeclaration newIzMethod(final InstanceofExpression ¢) {
    return az.methodDeclaration(wizard.ast("static boolean " + izMethodName(¢) + "(Object ¢){ return ¢ instanceof " + type(¢) + ";}"));
  }

  @Nullable private static AbstractTypeDeclaration containingType(final InstanceofExpression ¢) {
    // TODO: Marco maybe in the future change to iz.java in package which will
    // be created automatically...
    return yieldAncestors.untilContainingType().from(¢);
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final InstanceofExpression __) {
    return "replace instanceof with iz()";
  }
}
