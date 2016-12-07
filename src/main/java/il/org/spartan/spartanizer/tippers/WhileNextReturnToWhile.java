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

  @Override @SuppressWarnings("boxing") public Tip tip(final WhileStatement s) {
    final int num = new Recurser<>(s, 0).postVisit((x) -> x.getRoot().getNodeType() != ASTNode.BREAK_STATEMENT ? x.getCurrent() : x.getCurrent() + 1);
    if (num > 0)
      return null;
    final ReturnStatement $ = extract.nextReturn(s);
    if ($ == null || new Recurser<>(s, 0).preVisit((x) -> (!iz.breakStatement(az.statement(x.getRoot())) ? x.getCurrent() : 1 + x.getCurrent())) != 0 || iz.block(s.getBody()))
      return null;
    final IfStatement inlineIf = subject.pair($, null).toNot(s.getExpression());
    final WhileStatement retWhile = duplicate.of(s);
    final List<Statement> lst = extract.statements(retWhile.getBody());
    lst.add(inlineIf);
    retWhile.setBody(subject.ss(lst).toBlock());
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(s, retWhile, g);
        r.remove($, g);
      }
    };
  }
}
