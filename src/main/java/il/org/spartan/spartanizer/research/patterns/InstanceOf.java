package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class InstanceOf extends NanoPatternTipper<InstanceofExpression> {
  static final TypeChecker c = new TypeChecker();

  @Override public boolean canTip(final InstanceofExpression ¢) {
    if (!(step.type(¢) instanceof SimpleType))
      return false;
    final MethodDeclaration m = searchAncestors.forContainingMethod().from(¢);
    final Javadoc j = m.getJavadoc();
    return (j == null || !(j + "").contains(c.javadoc())) && c.cantTip(m) && !(step.type(¢) + "").contains(".");
  }

  @Override public Tip tip(final InstanceofExpression ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(!iz.parenthesizedExpression(¢.getParent()) ? ¢ : ¢.getParent(), wizard.ast(izMethodName(¢) + "(" + step.left(¢) + ")"), g);
        if (!izMethodExist(¢))
          addizMethod(¢, r, g);
        Logger.logNP(¢, getClass().getSimpleName());
      }
    };
  }

  static String izMethodName(final InstanceofExpression ¢) {
    return "iz" + step.type(¢);
  }

  static boolean izMethodExist(final InstanceofExpression ¢) {
    return step.methods(containingType(¢)).stream().filter(m -> izMethodName(¢).equals(m.getName() + "") && booleanType(step.returnType(m)))
        .count() != 0;
  }

  private static boolean booleanType(final Type returnType) {
    return "boolean".equals(returnType + "");
  }

  static void addizMethod(final InstanceofExpression ¢, final ASTRewrite r, final TextEditGroup g) {
    wizard.addMethodToType(containingType(¢), newIzMethod(¢), r, g);
  }

  private static MethodDeclaration newIzMethod(final InstanceofExpression ¢) {
    return az.methodDeclaration(wizard.ast("static boolean " + izMethodName(¢) + "(Object ¢){ return ¢ instanceof " + step.type(¢) + ";}"));
  }

  private static TypeDeclaration containingType(final InstanceofExpression ¢) {
    // TODO: Marco maybe in the future change to iz.java in package which will
    // be created automatically...
    return searchAncestors.forContainingType().from(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final InstanceofExpression __) {
    return "replace instanceof with iz()";
  }
}
