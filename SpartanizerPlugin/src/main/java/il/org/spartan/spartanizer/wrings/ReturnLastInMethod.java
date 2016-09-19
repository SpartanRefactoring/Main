package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** removes empty return statements, provided that they are last in method.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class ReturnLastInMethod extends EagerWring<ReturnStatement> implements Kind.SyntacticBaggage {
  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "Remove redundant return statement";
  }

  @Override public Suggestion suggest(final ReturnStatement s) {
    if (s.getExpression() != null)
      return null;
    final Block b = az.block(s.getParent());
    return b == null || !lastIn(s, statements(b)) || !(b.getParent() instanceof MethodDeclaration) ? null //
        : new Suggestion(description(s), s) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(s, g);
          }
        };
  }
}
