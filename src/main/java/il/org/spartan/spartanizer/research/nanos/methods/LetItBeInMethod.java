package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tippers.*;

/** Let x in S <br>
 * {@link Assignment} followed by {@link ExpressionStatement} or
 * {@link ReturnStatement}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
public class LetItBeInMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 8849848153637800009L;
  private static final FragmentInitializerStatementTerminatingScope rival = new FragmentInitializerStatementTerminatingScope();

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazTwoStatements(¢) //
        && iz.variableDeclarationStatement(firstStatement(¢)) //
        && preDelegation(secondStatement(¢)) //
        && rival.cantTip(first(fragments(az.variableDeclarationStatement(firstStatement(¢))))) //
        && usesAssignment(¢);
  }

  private boolean usesAssignment(final MethodDeclaration ¢) {
    return !collect.usesOf(name(first(fragments(az.variableDeclarationStatement(firstStatement(¢)))))).in(secondStatement(¢)).isEmpty();
  }

  private static boolean preDelegation(final Statement secondStatement) {
    return iz.expressionStatement(secondStatement) //
        || iz.returnStatement(secondStatement);
  }
}
