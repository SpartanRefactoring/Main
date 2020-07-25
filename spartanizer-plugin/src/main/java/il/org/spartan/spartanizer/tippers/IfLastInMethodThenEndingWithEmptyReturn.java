package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.factory.atomic;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code if (a) { f(); return; } } into {@code
 * if (a)
 *   f();
 * } provided that this <code>
 * <b>if</b>
 * </code> statement is the last statement in a method.
 * @author Yossi Gil
 * @since 2016 */
public final class IfLastInMethodThenEndingWithEmptyReturn extends EagerTipper<IfStatement>//
    implements Category.EarlyReturn {
  private static final long serialVersionUID = 0x3254580189EC0A3BL;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant return statement in 'then' branch of if statement that terminates this method";
  }
  @Override public Tip tip(final IfStatement s) {
    final Block b = az.block(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !is.lastIn(s, statements(b)))
      return null;
    final ReturnStatement $ = az.returnStatement(hop.lastStatement(then(s)));
    return $ == null || $.getExpression() != null ? null : new Tip(description(s), getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, atomic.emptyStatement($), g);
      }
    };
  }
}
