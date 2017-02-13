package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Let x in S <br>
 * {@link Assignment} followed by {@link ExpressionStatement} or
 * {@link ReturnStatement}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
public class Let extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazTwoStatements(¢)//
        && iz.variableDeclarationStatement(firstStatement(¢))//
        && preDelegation(secondStatement(¢));
  }

  private static boolean preDelegation(Statement secondStatement) {
    return iz.expressionStatement(secondStatement)//
        || iz.returnStatement(secondStatement);
  }
}
