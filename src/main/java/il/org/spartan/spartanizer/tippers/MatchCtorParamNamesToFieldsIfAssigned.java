package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;
import static il.org.spartan.lisp.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Match c'tor parameters to fields, for example: Convert: class A { int x;
 * public A(int y) {this.x = y; } } to: class A { int x; public A(int x) {this.x
 * = x; } }
 * @since 07-Dec-16
 * @author Doron Meshulam */
// TODO Doron - I gave you some tips on fixing this class in one of the issues.
// Please follow up.
@SuppressWarnings("unused")
public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = 4669973864090408581L;

  @Override protected boolean prerequisite(final MethodDeclaration __) {
    return false;
  }

  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    final List<String> params = parameters(d).stream().map(λ -> λ.getName().getIdentifier()).collect(toList());
    final List<Statement> bodyStatements = statements(d);
    final Collection<String> definedLocals = new ArrayList<>();
    final List<SimpleName> $ = new ArrayList<>(), newNames = new ArrayList<>();
    for (final Statement s : bodyStatements) {
      if (!iz.expressionStatement(s)) {
        if (iz.variableDeclarationStatement(s))
          definedLocals.addAll(fragments(az.variableDeclarationStatement(s)).stream().map(λ -> λ.getName().getIdentifier()).collect(toList()));
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
      if (definedLocals.contains(fieldName.getIdentifier()) || params.contains(fieldName.getIdentifier()))
        continue;
      $.add(paramName);
      newNames.add(fieldName);
    }
    return $.isEmpty() ? null : new Tip(description(d), d, getClass()) {
      final List<SimpleName> on = new ArrayList<>($);
      final List<SimpleName> nn = new ArrayList<>(newNames);

      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(first(on), first(nn), d, r, g);
      }
    };
  }
}