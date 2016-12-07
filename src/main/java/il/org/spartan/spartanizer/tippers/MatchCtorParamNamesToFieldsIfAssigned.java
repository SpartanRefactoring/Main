package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @since 07-Dec-16
 * @author Doron Meshulam */
@SuppressWarnings("unused")
public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  @Override protected boolean prerequisite(final MethodDeclaration __) {
    return false;
  }

  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    parameters(d);
    final List<Statement> bodyStatements = statements(d);
    for (final Statement s : bodyStatements) {
      if (!iz.expressionStatement(s))
        continue;
      final Expression e = ((ExpressionStatement) s).getExpression();
      if (!iz.assignment(e))
        continue;
      final Assignment a = az.assignment(e);
      if (!iz.fieldAccess(left(a)) || !(((FieldAccess) left(a)).getExpression() instanceof ThisExpression))
        continue;
      ((FieldAccess) left(a)).getName();
      if (!iz.simpleName(right(a)))
        continue;
      az.simpleName(right(a));
    }
    return new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        // TODO: Change of code here
        System.out.println(r);
        System.out.println(g);
      }
    };
  }
}