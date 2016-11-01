package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Coercion extends NanoPatternTipper<CastExpression> {
  static final Converter c = new Converter();

  @Override public boolean canTip(final CastExpression ¢) {
    if (!(step.type(¢) instanceof SimpleType))
      return false;
    final MethodDeclaration m = searchAncestors.forContainingMethod().from(¢);
    final Javadoc j = m.getJavadoc();
    return (j == null || !(j + "").contains(c.javadoc())) && c.cantTip(m);
  }

  @Override public Tip tip(final CastExpression ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(!iz.parenthesizedExpression(¢.getParent()) ? ¢ : ¢.getParent(), wizard.ast("az" + step.type(¢) + "(" + step.expression(¢) + ")"),
            g);
        if (!azMethodExist(¢))
          addAzMethod(¢, r, g);
        Logger.logNP(¢, getClass().getSimpleName());
      }
    };
  }

  static boolean azMethodExist(final CastExpression ¢) {
    return Arrays.stream(step.methods(containingType(¢)))
        .filter(m -> ("az" + step.type(¢)).equals(m.getName() + "") && typesEqual(step.returnType(m), step.type(¢))).count() != 0;
  }

  private static boolean typesEqual(final Type returnType, final Type t) {
    return (returnType + "").equals(t + "");
  }

  static void addAzMethod(final CastExpression ¢, final ASTRewrite r, final TextEditGroup g) {
    wizard.addMethodToType(containingType(¢), createAzMethod(¢), r, g);
  }

  private static MethodDeclaration createAzMethod(final CastExpression ¢) {
    return az.methodDeclaration(wizard.ast("static " + step.type(¢) + " az" + step.type(¢) + "(Object ¢){ return (" + step.type(¢) + ")¢;}"));
  }

  private static TypeDeclaration containingType(final CastExpression ¢) {
    // TODO: Marco maybe in the future change to az.java in package which will
    // be created automatically...
    String s = AnalyzerOptions.get(Coercion.class.getSimpleName(), "apiLevel");
    return s == null || !"type".equals(s) ? null : searchAncestors.forContainingType().from(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final CastExpression __) {
    return "replace coercion with az()";
  }
}
