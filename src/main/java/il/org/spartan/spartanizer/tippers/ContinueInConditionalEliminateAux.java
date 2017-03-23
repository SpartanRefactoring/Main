package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Auxilary class for all tippers toList Issue #1014
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-04 */
enum ContinueInConditionalEliminateAux {
  ;
  @SuppressWarnings("rawtypes") @Nullable public static Tip actualReplacement(@Nullable final Block b, @NotNull final Statement s,
      final Class<? extends Tipper> c) {
    if (b == null || statements(b).size() < 2)
      return null;
    @NotNull final List<Statement> $ = statements(b);
    @Nullable final IfStatement continueStatement = az.ifStatement($.get($.size() - 2));
    if (continueStatement == null || !iz.continueStatement(continueStatement.getThenStatement()))
      return null;
    final IfStatement replacementIf = subject.pair(last($), null).toNot(continueStatement.getExpression());
    return new Tip("Eliminate conditional continue before last statement in the for loop", s, c) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.remove(last($), g);
        r.replace(continueStatement, replacementIf, g);
      }
    };
  }
}
