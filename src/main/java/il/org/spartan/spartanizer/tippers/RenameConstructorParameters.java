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

/** TODO Rename paramter names in constructor, to match fields, if they are
 * assigned to them for example: <br/>
 * {@code class A {int x;A(int y,int z) {this.x = z;}}} <br/>
 * to: <br/>
 * {@code class A {int x;A(int y,int x) {this.x = x;}}} <br/>
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-28 */
public class RenameConstructorParameters extends EagerTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0x6A3AE2731FF74B0BL;

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    final List<String> parameterNames = step.parametersNames(d);
    for (final ASTNode s : extract.statements(d.getBody()))
      if (iz.expressionStatement(s) && iz.assignment(az.expressionStatement(s).getExpression())) {
        final Assignment a = az.assignment(az.expressionStatement(s).getExpression());
        if (a.getOperator() == Operator.ASSIGN && parameterNames.contains(a.getRightHandSide() + "") && iz.fieldAccess(a.getLeftHandSide())) {
          final SimpleName $ = d.getAST().newSimpleName(a.getRightHandSide() + ""),
              to1 = d.getAST().newSimpleName(az.fieldAccess(a.getLeftHandSide()).getName().getIdentifier());
          if (!alreadyDefined(to1, d))
            return new Tip(description(d), getClass(), d) {
              @Override public void go(final ASTRewrite r, final TextEditGroup g) {
                for (final SingleVariableDeclaration q : step.parameters(d))
                  action.rename($, to1, q, r, g);
                action.rename($, to1, d.getBody(), r, g);
              }
            };
          continue;
        }
      }
    return null;
  }

  private static boolean alreadyDefined(final SimpleName to1, final MethodDeclaration d) {
    return step.parameters(d).stream().anyMatch(λ -> λ.getName().getIdentifier().equals(to1.getIdentifier()));
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Rename constructor parameters to match fields names";
  }
}