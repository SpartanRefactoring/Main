package il.org.spartan.spartanizer.research.patterns;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullReturnNull extends NanoPatternTipper<IfStatement> {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace with #default #deault x";
  }

  @Override public boolean canTip(final IfStatement x) {
    final Expression then = expression(step.then(x));
    final InfixExpression e = az.infixExpression(step.expression(x));
    if (!iz.comparison(e))
      return false;
    final InfixExpression condition = az.comparison(e);
    return operator(condition) == EQUALS && iz.nullLiteral(then) && (iz.nullLiteral(left(condition)) || iz.nullLiteral(right(condition)));
  }

  @Override public Tip tip(final IfStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(expression(then(s)), into.e("Null.value"), g);
      }
    };
  }
}
