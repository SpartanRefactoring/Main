package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.internal.compiler.util.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @since 07-Dec-16
 * @author Doron Meshulam */
public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
<<<<<<< HEAD
=======
  @Override protected boolean prerequisite(final MethodDeclaration __) {
    return false;
  }

>>>>>>> 886dc65fa339a3bf69e266a8890e2a4b09a2f04a
  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration $) {
    if (!$.isConstructor())
      return null;
<<<<<<< HEAD
    List<String> params = parameters(d).stream().map(el -> el.getName().getIdentifier()).collect(Collectors.toList());
    List<Statement> bodyStatements = statements(d);
    List<String> definedLocals = new ArrayList<String>();
    List<SimpleName> oldNames = new ArrayList<SimpleName>();
    List<SimpleName> newNames = new ArrayList<SimpleName>();
    for (Statement s : bodyStatements) {
      if (!iz.expressionStatement(s)) {
        if (iz.variableDeclarationStatement(s))
          definedLocals
              .addAll(fragments(az.variableDeclarationStatement(s)).stream().map(el -> el.getName().getIdentifier()).collect(Collectors.toList()));
        continue;
      }
      Expression e = expression(az.expressionStatement(s));
      if (!(iz.assignment(e)))
        continue;
      Assignment a = az.assignment(e);
      if (!iz.fieldAccess(left(a)) || !iz.thisExpression(expression(az.fieldAccess(left(a)))))
        continue;
      SimpleName fieldName = name(az.fieldAccess(left(a)));
      if (!(iz.simpleName(right(a))))
        continue;
      SimpleName paramName = az.simpleName(right(a));
      if (definedLocals.contains(fieldName.getIdentifier()) || params.contains(paramName.getIdentifier()))
        continue;
      oldNames.add(paramName);
      newNames.add(fieldName);
    }
    return new Tip(description(d), d, this.getClass()) {
      List<SimpleName> on = new ArrayList<SimpleName>(oldNames);
      List<SimpleName> nn = new ArrayList<SimpleName>(newNames);

=======
    parameters($);
    final List<Statement> bodyStatements = statements($);
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
    return new Tip(description($), $, this.getClass()) {
>>>>>>> 886dc65fa339a3bf69e266a8890e2a4b09a2f04a
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (int ¢ = 1; ¢ <= on.size(); ++¢) 
          Tippers.rename(on.get(¢), nn.get(¢), d, r, g);
      }
    };
  }
}