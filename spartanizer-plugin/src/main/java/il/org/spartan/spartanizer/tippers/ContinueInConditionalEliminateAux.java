package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.Tipper;

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
