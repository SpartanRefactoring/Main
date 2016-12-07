package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** @since 07-Dec-16
 * @author Doron Meshulam */
@SuppressWarnings("unused")
public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
//  @Override protected boolean prerequisite(@SuppressWarnings("unused") final MethodDeclaration __) {
//    return false;
//  }

  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    List<SingleVariableDeclaration> ctorParams = parameters(d);
    List<Statement> bodyStatements = statements(d);
    for (Statement s : bodyStatements) {
      if (!(iz.expressionStatement(s)))
        continue;
      Expression e = expression(az.expressionStatement(s));
      if (!(iz.assignment(e)))
        continue;
      Assignment a = az.assignment(e);
      if (!(iz.fieldAccess(left(a))) || !(iz.thisExpression(expression(az.fieldAccess(left(a))))))
        continue;
      SimpleName fieldName = name(az.fieldAccess(a));
      if (!(iz.simpleName(right(a))))
        continue;
      SimpleName paramName = az.simpleName(right(a));
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
