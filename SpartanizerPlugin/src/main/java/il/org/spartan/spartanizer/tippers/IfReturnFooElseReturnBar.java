package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * if (x)
 *   return b;
 * else
 *   return c;
 * } into {@code
 * return x? b : c
 * }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfReturnFooElseReturnBar extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Ternarization {
  private static final long serialVersionUID = -0x6DF5ABB8AEE76689L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace if with a return of a conditional statement";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && extract.returnExpression(then(¢)) != null && extract.returnExpression(elze(¢)) != null;
  }
  @Override public Statement replacement(final IfStatement s) {
    final Expression $ = extract.returnExpression(then(s)), elze = extract.returnExpression(elze(s));
    return $ == null || elze == null ? null : subject.operand(subject.pair($, elze).toCondition(s.getExpression())).toReturn();
  }
}
