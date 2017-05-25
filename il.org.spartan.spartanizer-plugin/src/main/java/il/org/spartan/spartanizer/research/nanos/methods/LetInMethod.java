package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tippers.*;

/** Let x in S <br>
 * {@link Assignment} followed by {@link ExpressionStatement} or
 * {@link ReturnStatement}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-13 */
public class LetInMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x7AD0FA01D9BA4849L;
  private static final LocalInitializedStatementTerminatingScope rival = new LocalInitializedStatementTerminatingScope();

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazTwoStatements(¢) //
        && iz.variableDeclarationStatement(firstStatement(¢)) //
        && preDelegation(secondStatement(¢)) //
        && rival.cantTip(the.firstOf(fragments(az.variableDeclarationStatement(firstStatement(¢))))) //
        && usesAssignment(¢);
  }
  private boolean usesAssignment(final MethodDeclaration ¢) {
    return !collect.usesOf(name(the.firstOf(fragments(az.variableDeclarationStatement(firstStatement(¢)))))).in(secondStatement(¢)).isEmpty();
  }
  private static boolean preDelegation(final Statement secondStatement) {
    return iz.expressionStatement(secondStatement) //
        || iz.returnStatement(secondStatement);
  }
  @Override public String tipperName() {
    return "LetIn";
  }
}
