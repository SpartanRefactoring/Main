package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Rename parameter names in constructor, to match fields, if they are assigned
 * to them for example: <br/>
 * {@code class A {int x;A(int y,int z) {this.x = z;}}} <br/>
 * to: <br/>
 * {@code class A {int x;A(int y,int x) {this.x = x;}}} <br/>
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-28 */
public class ConstructorRenameParameters extends EagerTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0x6A3AE2731FF74B0BL;

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    final List<String> parameterNames = step.parametersNames(d);
    final List<Statement> statements = extract.statements(d.getBody());
    for (final Statement s : statements) {
      final Assignment a = az.assignment(step.expression(az.expressionStatement(s)));
      if (a == null || a.getOperator() != Operator.ASSIGN)
        continue;
      final SimpleName parameter = az.simpleName(step.from(a));
      if (parameter == null || !parameterNames.contains(parameter + ""))
        continue;
      final FieldAccess fieldAccess = az.fieldAccess(step.to(a));
      if (fieldAccess == null)
        continue;
      final SimpleName field = fieldAccess.getName();
      if (!parameterNames.contains(field + ""))
        return new Tip(description(d), getClass(), d) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final SimpleName to1 = d.getAST().newSimpleName(field + ""), $ = d.getAST().newSimpleName(parameter + "");
            for (final SingleVariableDeclaration q : step.parameters(d))
              misc.rename($, to1, q, r, g);
            misc.rename($, to1, d.getBody(), r, g);
          }
        };
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Rename constructor parameters to match fields names";
  }
}