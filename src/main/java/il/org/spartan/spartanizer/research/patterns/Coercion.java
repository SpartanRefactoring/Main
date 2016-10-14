package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Coercion extends NanoPatternTipper<CastExpression> {
  @Override protected boolean prerequisite(@SuppressWarnings("unused") CastExpression __) {
    return true;
  }

  @Override public Tip tip(CastExpression ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        r.replace(¢, wizard.ast("az(" + ¢.getType() + ", " + ¢.getExpression() + ")"), g);
      }
    };
  }

  @Override public String description(@SuppressWarnings("unused") CastExpression __) {
    return "replace coercion with az()";
  }
}
