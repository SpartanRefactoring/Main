package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Fluenter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  static UserDefinedTipper<ReturnStatement> tipper = TipperFactory.tipper("return this;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return returnTypeSameAsClass(¢) && lastStatementReturnsThis(¢);
  }
  private static boolean returnTypeSameAsClass(final MethodDeclaration ¢) {
    return (searchAncestors.forContainingType().from(¢).getName() + "").equals(step.returnType(¢) + "");
  }
  /** @param ¢
   * @return */
  private static boolean lastStatementReturnsThis(final MethodDeclaration ¢) {
    return step.body(¢) != null && !step.body(¢).statements().isEmpty()
        && iz.returnStatement(step.statements(step.body(¢)).get(step.body(¢).statements().size() - 1))
        && tipper.canTip(az.returnStatement(az.statement(step.statements(step.body(¢)).get(step.body(¢).statements().size() - 1))));
  }
}