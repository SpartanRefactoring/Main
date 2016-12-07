package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Dor Ma'ayan Inline return statement
 * @since 03-12-2016 */
public final class WhileNextReturnToWhile extends EagerTipper<WhileStatement> implements TipperCategory.Inlining {
  @Override public String description(@SuppressWarnings("unused") final WhileStatement ¢) {
    return "Iniline the return into the while statement";
  }

  @SuppressWarnings("boxing") @Override public Tip tip(final WhileStatement s) {
    final int num = new Recurser<>(s, 0).postVisit((x) -> x.getRoot().getNodeType() != ASTNode.BREAK_STATEMENT ? x.getCurrent() : x.getCurrent() + 1);
    if (num > 0)
      return null;
    final ReturnStatement nextRet = extract.nextReturn(s);
    final int breaks = new Recurser<>(s, 0).preVisit((x) -> (!iz.breakStatement(az.statement(x.getRoot())) ? x.getCurrent() : 1 + x.getCurrent()));
    if (nextRet == null || breaks != 0 || iz.block(s.getBody()))
      return null;
    final IfStatement inlineIf = subject.pair(nextRet, null).toNot(s.getExpression());
    final WhileStatement retWhile = duplicate.of(s);
    final List<Statement> lst = extract.statements(retWhile.getBody());
    lst.add(inlineIf);
    final Block b = subject.ss(lst).toBlock();
    retWhile.setBody(b);
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(s, retWhile, g);
        r.remove(nextRet, g);
      }
    };
  }
}
