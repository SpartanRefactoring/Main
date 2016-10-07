package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullReturnNull extends NanoPatternTipper<IfStatement> implements TipperCategory.Nanos {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace with #default #deault x";
  }

  @Override public boolean prerequisite(final IfStatement x) {
    final Expression then = expression(step.then(x));
    final InfixExpression e = az.infixExpression(step.expression(x));
    if (!iz.comparison(e))
      return false;
    final InfixExpression condition = az.comparison(e);
    return operator(condition) == EQUALS && iz.nullLiteral(then) && (iz.nullLiteral(step.left(condition)) || iz.nullLiteral(step.right(condition)));
  }

  @Override public Tip tip(final IfStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(step.expression(step.then(s)), into.e("Null.value"), g);
      }
    };
  }
}
