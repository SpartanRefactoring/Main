package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Setter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean morePrerequisites(MethodDeclaration ¢) {
    if (step.parameters(¢).size() != 1 || step.body(¢) == null)
      return false;
    @SuppressWarnings("unchecked") List<Statement> ss = ¢.getBody().statements();
    if (ss.size() != 1 || !iz.expressionStatement(ss.get(0)))
      return false;
    Expression e = az.expressionStatement(ss.get(0)).getExpression();
    if (!iz.assignment(e))
      return false;
    Assignment a = az.assignment(e);
    return iz.name(a.getLeftHandSide()) && wizard.same(a.getRightHandSide(), step.parameters(¢).get(0).getName());
  }

  @Override public String description(MethodDeclaration ¢) {
    return ¢.getName() + " is a setter method";
  }

  @Override protected String javadoc() {
    return "[[Setter]]";
  }
}
