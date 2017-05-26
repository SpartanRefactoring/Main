package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Auxilary class for all tippers toList Issue #1014
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-04 */
enum ContinueInConditionalEliminateAux {
  ;
  public static <N extends ASTNode> Tip actualReplacement(final Block b, final Statement s, final Class<Tipper<N>> c) {
    if (b == null || statements(b).size() < 2)
      return null;
    final List<Statement> $ = statements(b);
    final IfStatement continueStatement = az.ifStatement($.get($.size() - 2));
    if (continueStatement == null || !iz.continueStatement(continueStatement.getThenStatement()))
      return null;
    final IfStatement replacementIf = subject.pair(the.lastOf($), null).toNot(continueStatement.getExpression());
    return new Tip("Eliminate conditional continue before last statement in the for loop", c, s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(the.lastOf($), g);
        r.replace(continueStatement, replacementIf, g);
      }
    };
  }
}
