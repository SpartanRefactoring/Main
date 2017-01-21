package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.stream.*;

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
// TODO: doron add docs description such as in {@link SwitchEmpty} pls
// TODO: Dorn, what's going on with the above TOOD --yg
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
    final List<String> params = parameters(d).stream().map(el -> el.getName().getIdentifier()).collect(Collectors.toList());
    final List<Statement> bodyStatements = statements(d);
    final List<String> definedLocals = new ArrayList<>();
    final List<SimpleName> $ = new ArrayList<>(), newNames = new ArrayList<>();
    for (final Statement s : bodyStatements) {
      if (!iz.expressionStatement(s)) {
        if (iz.variableDeclarationStatement(s))
          definedLocals
              .addAll(fragments(az.variableDeclarationStatement(s)).stream().map(el -> el.getName().getIdentifier()).collect(Collectors.toList()));
        continue;
      }
      final Expression e = expression(az.expressionStatement(s));
      if (!iz.assignment(e))
        continue;
      final Assignment a = az.assignment(e);
      if (!iz.fieldAccess(left(a)) || !iz.thisExpression(expression(az.fieldAccess(left(a)))))
        continue;
      final SimpleName fieldName = name(az.fieldAccess(left(a)));
      if (!iz.simpleName(right(a)))
        continue;
      final SimpleName paramName = az.simpleName(right(a));
      if (definedLocals.contains(fieldName.getIdentifier()) || params.contains(paramName.getIdentifier()))
        continue;
      $.add(paramName);
      newNames.add(fieldName);
    }
    // TODO: Doron Meshulam - change only one variable at a time --yg
    return new Tip(description(d), d, getClass()) {
      final List<SimpleName> on = new ArrayList<>($);
      final List<SimpleName> nn = new ArrayList<>(newNames);

      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (int ¢ = 1; ¢ <= on.size(); ++¢)
          Tippers.rename(on.get(¢), nn.get(¢), d, r, g);
      }
    };
  }
}