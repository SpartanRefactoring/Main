package il.org.spartan.spartanizer.tippers;

import java.util.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Auxilary class for all tippers </br>
 * Issue #1014
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-04 */
enum EliminateConditionalContinueAux {
  ;
  @SuppressWarnings("rawtypes") public static Tip actualReplacement(final Block b, final Statement s, final Class<? extends Tipper> c) {
    if (b == null || step.statements(b).size() < 2)
      return null;
    final List<Statement> $ = step.statements(b);
    final IfStatement continueStatement = az.ifStatement($.get($.size() - 2));
    if (continueStatement == null || !iz.continueStatement(continueStatement.getThenStatement()))
      return null;
    final IfStatement replacementIf = subject.pair(copy.of($.get($.size() - 1)), null).toNot(continueStatement.getExpression());
    return new Tip("Eliminate conditional continue before last statement in the for loop", s, c) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove($.get($.size() - 1), g);
        r.replace(continueStatement, replacementIf, g);
      }
    };
  }
}
