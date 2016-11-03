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

  @Override public Tip tip(final MethodDeclaration d, final ExclusionManager m) {
    final Tip tip = super.tip(d, m);
    return new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        tip.go(r, g);
        // TODO: Marco if (!iz.Void(step.returnType(d)))
        // return;
        // final ReturnStatement s = d.getAST().newReturnStatement();
        // s.setExpression(d.getAST().newThisExpression());
        // wizard.addStatement(d, s, r, g);
        // d.setReturnType2(getType(searchAncestors.forContainingType().from(d)));
      }
    };
  }

  /** @param ¢
   * @return */
  protected static Type getType(final TypeDeclaration ¢) {
    // TODO Marco somehow get type out of TypeDeclaration
    return ¢.getSuperclassType();
  }
}
