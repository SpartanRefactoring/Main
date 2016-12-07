package il.org.spartan.spartanizer.tippers;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @since 07-Dec-16
 * @author Doron Meshulam */
// TODO: Rewrite this class, making sure you do not use instanceof nor casting. Instead you should be using classes `iz` and `azz`
public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  @Override protected boolean prerequisite(@SuppressWarnings("unused") MethodDeclaration __) {
    return false; 
  }

  @Override public String description(final MethodDeclaration ¢) {
    return "Match parameter names to fields in constructor '" + ¢ + "'";
  }

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null;
    for (final Statement s : statements(body(d))) {
      if (!(s instanceof ExpressionStatement))
        continue;
      final Expression e = ((ExpressionStatement) s).getExpression();
      if (!(e instanceof Assignment))
        continue;
      final Assignment a = (Assignment) e;
      final Expression leftAss = a.getLeftHandSide();
      final Expression rightAss = a.getRightHandSide();
      if (!(leftAss instanceof FieldAccess) || !(((FieldAccess) leftAss).getExpression() instanceof ThisExpression))
        continue;
      ((FieldAccess) leftAss).getName();
      if (!(rightAss instanceof SimpleName))
        continue;
      a.getRightHandSide();
    }
    new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite __, final TextEditGroup g) {
        // TODO: Change of code here
      }
    }.hashCode();
    return null;
  }
}
