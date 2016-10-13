package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Getter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean morePrerequisites(MethodDeclaration ¢) {
    @SuppressWarnings("unchecked") List<Statement> ss = ¢.getBody().statements();
    return ss.size() == 1 && iz.returnStatement(ss.get(0)) && step.parameters(¢).isEmpty() && iz.name(az.returnStatement(ss.get(0)).getExpression());
  }

  @Override public String description(MethodDeclaration ¢) {
    return ¢.getName() + " is a getter method";
  }

  @Override protected String javadoc() {
    return "[[Getter]]";
  }
}
