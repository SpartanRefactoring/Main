package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.lastIn;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} to convert <code><b>if</b> (a) { f(); g(); }</code> into
 * <code><b>if</b> (!a) return f(); g();</code> provided that this
 * <code><b>if</b></code> statement is the last statement in a method.
 *
 * @author Yossi Gil
 * @since 2015-09-09
 */
public class IfLastInMethod extends Wring<IfStatement> {
  @Override String description(final IfStatement s) {
    return "Invert conditional " + s.getExpression() + " for early return";
  }
  @Override Rewrite make(final IfStatement s) {
    if (Is.vacuousThen(s) || !Is.vacuousElse(s) || Extract.statements(then(s)).size() < 2)
      return null;
    final Block b = asBlock(s.getParent());
    return b == null || !lastIn(s, b.statements()) || !(b.getParent() instanceof MethodDeclaration) ? null : new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.insertAfter(s, Extract.statements(then(s)), r, g);
        final IfStatement newIf = duplicate(s);
        newIf.setExpression(duplicate(logicalNot(s.getExpression())));
        newIf.setThenStatement(s.getAST().newReturnStatement());
        newIf.setElseStatement(null);
        r.replace(s, newIf, g);
      }
    };
  }
}
