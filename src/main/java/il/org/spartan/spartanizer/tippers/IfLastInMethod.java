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

/** convert <code>
 * <b>if</b> (a) { f(); g(); }
 * </code> into <code>
 * <b>if</b> (!a) return f(); g();
 * </code> provided that this <code>
 * <b>if</b>
 * </code> statement is the last statement in a method.
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class IfLastInMethod extends EagerTipper<IfStatement>//
    implements TipperCategory.EarlyReturn {
  private static final long serialVersionUID = 7913539614277563670L;

  @Override public String description(final IfStatement ¢) {
    return "Invert conditional " + expression(¢) + " for early return";
  }

  @Override public Tip tip(final IfStatement s) {
    if (iz.vacuousThen(s) || !iz.vacuousElse(s) || extract.statements(then(s)).size() < 2)
      return null;
    final Block $ = az.block(parent(s));
    return $ == null || !lastIn(s, statements($)) || !iz.methodDeclaration(parent($)) ? null : new Tip(description(s), s, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.insertAfter(s, extract.statements(then(s)), r, g);
        final IfStatement newIf = copy.of(s);
        newIf.setExpression(copy.of(make.notOf(expression(s))));
        newIf.setThenStatement(s.getAST().newReturnStatement());
        newIf.setElseStatement(null);
        r.replace(s, newIf, g);
      }
    };
  }
}
