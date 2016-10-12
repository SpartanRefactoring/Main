package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Setter extends NanoPatternTipper<MethodDeclaration> {
  @Override protected boolean prerequisite(MethodDeclaration ¢) {
    if (step.parameters(¢).size() != 1)
      return false;
    @SuppressWarnings("unchecked") List<Statement> ss = ¢.getBody().statements();
    if (ss.size() != 1 || !iz.expressionStatement(ss.get(0)))
      return false;
    Assignment a = az.assignment(az.expressionStatement(ss.get(0)).getExpression());
    return iz.name(a.getLeftHandSide()) && wizard.same(a.getRightHandSide(), step.parameters(¢).get(0).getName());
  }

  @Override public String description(MethodDeclaration ¢) {
    return ¢.getName() + " is a setter method";
  }

  @Override public Tip tip(MethodDeclaration ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        // TODO: empty
      }
    };
  }
}
