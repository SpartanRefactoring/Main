package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** removes empty return sideEffects, provided that they are last in method.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class ReturnLastInMethod extends EagerTipper<ReturnStatement> implements TipperCategory.SyntacticBaggage {
  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "Remove redundant return statement";
  }

  @Override public Tip tip(final ReturnStatement s) {
    if (s.getExpression() != null)
      return null;
    final Block b = az.block(s.getParent());
    return b == null || !lastIn(s, statements(b)) || !(b.getParent() instanceof MethodDeclaration) ? null
        : new Tip(description(s), s, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(s, g);
          }
        };
  }
}
