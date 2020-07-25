package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
    implements Category.EarlyReturn {
  private static final long serialVersionUID = 0x6DD28A59F8FEF516L;

  @Override public String description(final IfStatement ¢) {
    return "Invert conditional " + expression(¢) + " for early return";
  }
  @Override public Tip tip(final IfStatement s) {
    if (iz.vacuousThen(s) || !iz.vacuousElse(s) || extract.statements(then(s)).size() < 2)
      return null;
    final Block $ = az.block(parent(s));
    return $ == null || !is.lastIn(s, statements($)) || !iz.methodDeclaration(parent($)) ? null : new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.insertAfter(s, extract.statements(then(s)), r, g);
        final IfStatement newIf = copy.of(s);
        newIf.setExpression(copy.of(cons.not(expression(s))));
        newIf.setThenStatement(s.getAST().newReturnStatement());
        newIf.setElseStatement(null);
        r.replace(s, newIf, g);
      }
    };
  }
}
