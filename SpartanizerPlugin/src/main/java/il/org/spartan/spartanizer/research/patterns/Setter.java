package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Setter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.tipper("this.$N", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (step.parameters(¢).size() != 1 || step.body(¢) == null)
      return false;
    @SuppressWarnings("unchecked") final List<Statement> ss = ¢.getBody().statements();
    if (ss.size() != 1 || !iz.expressionStatement(ss.get(0)))
      return false;
    final Expression e = az.expressionStatement(ss.get(0)).getExpression();
    if (!iz.assignment(e))
      return false;
    final Assignment a = az.assignment(e);
    return (iz.name(a.getLeftHandSide()) || tipper.canTip(a.getLeftHandSide()))
        && wizard.same(a.getRightHandSide(), step.parameters(¢).get(0).getName());
  }

  @Override public Tip tip(MethodDeclaration d, ExclusionManager m) throws TipperFailure {
    Tip tip = super.tip(d, m);
    return new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        tip.go(r, g);
        if (!iz.Void(step.returnType(d)))
          return;
        ReturnStatement s = d.getAST().newReturnStatement();
        s.setExpression(d.getAST().newThisExpression());
        wizard.addStatement(d, s, r, g);
      }
    };
  }

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + " is a setter method";
  }

  @Override protected String javadoc() {
    return "[[Setter]]";
  }
}
