package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code if (a) { f(); return; } } into {@code
 * if (a)
 *   f();
 * } provided that this <code>
 * <b>if</b>
 * </code> statement is the last statement in a method.
 * @author Yossi Gil
 * @since 2016 */
public final class IfLastInMethodThenEndingWithEmptyReturn extends EagerTipper<IfStatement>//
    implements TipperCategory.EarlyReturn {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant return statement in 'then' branch of if statement that terminates this method";
  }

  @Override public Tip tip(@NotNull final IfStatement s, @Nullable final ExclusionManager exclude) {
    final Block b = az.block(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, statements(b)))
      return null;
    final ReturnStatement $ = az.returnStatement(hop.lastStatement(then(s)));
    return $ == null || $.getExpression() != null || exclude != null && exclude.equals(s) ? null : new Tip(description(s), $, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace($, make.emptyStatement($), g);
      }
    };
  }
}
