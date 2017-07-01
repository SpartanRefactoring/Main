package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

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
    final ReturnStatement ret = az.returnStatement(hop.lastStatement(elze(s)));
    return ret == null || ret.getExpression() != null ? null : new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(ret, make.emptyStatement(s), g);
      }
    };
  }
}
