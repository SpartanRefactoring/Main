package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

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

/** convert {@code if(a)f();else{g();return;} } into
 * {@code if(a)f();else{g();;}} provided that this {@code if} statement is the
 * last statement in a method.
 * @author Yossi Gil
 * @author Daniel Mittelman {@code <mittelmania [at] gmail.com>}
 * @since 2015-09-09 */
public final class IfLastInMethodElseEndingWithEmptyReturn extends EagerTipper<IfStatement>//
    implements Category.EarlyReturn {
  private static final long serialVersionUID = -0x46C7F6C79A377A64L;

  @Override @SuppressWarnings("unused") public String description(final IfStatement __) {
    return "Remove redundant return statement in 'else' branch of if statement that terminates this method";
  }
  @Override public Tip tip(final IfStatement s) {
    final Block b = az.block(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !is.lastIn(s, statements(b)))
      return null;
    final ReturnStatement $ = az.returnStatement(hop.lastStatement(elze(s)));
    return $ == null || $.getExpression() != null ? null : new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, atomic.emptyStatement(s), g);
      }
    };
  }
}
