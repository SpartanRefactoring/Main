package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-28 */
public class RenameConstructorParameters extends EagerTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -7654679500754995979L;

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    List<String> parameterNames = step.parametersNames(d);
    for (ASTNode s : extract.statements(d.getBody()))
      if (iz.expressionStatement(s) && iz.assignment(az.expressionStatement(s).getExpression())) {
        Assignment a = az.assignment(az.expressionStatement(s).getExpression());
        if (a.getOperator() == Operator.ASSIGN && parameterNames.contains((a.getRightHandSide() + "")) && iz.fieldAccess(a.getLeftHandSide())) {
          SimpleName from1 = d.getAST().newSimpleName((a.getRightHandSide() + ""));
          SimpleName to1 = d.getAST().newSimpleName(az.fieldAccess(a.getLeftHandSide()).getName().getIdentifier());
          return alreadyDefined(to1, d) ? null : new Tip(description(d), d, getClass()) {
            @Override public void go(final ASTRewrite r, final TextEditGroup g) {
              for (SingleVariableDeclaration q : step.parameters(d))
                Tricks.rename(from1, to1, q, r, g);
              Tricks.rename(from1, to1, d.getBody(), r, g);
            }
          };
        }
      }
    return null;
  }
  
  private static boolean alreadyDefined(SimpleName to1,MethodDeclaration d){
    for(SingleVariableDeclaration ¢ : step.parameters(d))
      if(¢.getName().getIdentifier().equals(to1.getIdentifier()))
        return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") MethodDeclaration __) {
    return "Rename constructor parameters to match fields names";
  }
}