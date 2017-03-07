package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (a) { f(); return; } } into {@code
 * if (a)
 *   f();
 * } provided that this <code>
 * <b>if</b>
 * </code> statement is the last statement in a method.
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
public final class IfLastInMethodThenEndingWithEmptyReturn extends EagerTipper<IfStatement>//
    implements TipperCategory.EarlyReturn {
  private static final long serialVersionUID = 3626620363572251195L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant return statement in 'then' branch of if statement that terminates this method";
  }

  @Override public Tip tip(final IfStatement s, final ExclusionManager exclude) {
    final Block b = az.block(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, statements(b)))
      return null;
    final ReturnStatement $ = az.returnStatement(hop.lastStatement(then(s)));
    return $ == null || $.getExpression() != null || Objects.equals(exclude, s) ? null : new Tip(description(s), $, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, make.emptyStatement($), g);
      }
    };
  }
}
