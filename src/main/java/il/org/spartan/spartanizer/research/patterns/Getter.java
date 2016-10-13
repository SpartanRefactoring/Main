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
public class Getter extends NanoPatternTipper<MethodDeclaration> {
  @Override protected boolean prerequisite(MethodDeclaration ¢) {
    @SuppressWarnings("unchecked") List<Statement> ss = ¢.getBody().statements();
    return ss.size() == 1 && iz.returnStatement(ss.get(0)) && step.parameters(¢).isEmpty() && iz.name(az.returnStatement(ss.get(0)).getExpression());
  }

  @Override public String description(MethodDeclaration ¢) {
    return ¢.getName() + " is a getter method";
  }

  @Override public Tip tip(MethodDeclaration ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        // TODO: empty
      }
    };
  }
}
