package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class SetterGoFluent extends NanoPatternTipper<MethodDeclaration> {
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.tipper("this.$N", "", "");

  @Override public boolean canTip(final MethodDeclaration ¢) {
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

  @Override public Tip tip(final MethodDeclaration d) {
    Logger.logNP(d, getClass().getSimpleName());
    return new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        if (!iz.voidType(step.returnType(d)))
          return;
        final MethodDeclaration n = az.methodDeclaration(ASTNode.copySubtree(d.getAST(), d));
        n.setReturnType2(az.type(ASTNode.copySubtree(n.getAST(), getType(searchAncestors.forContainingType().from(d)))));
        final ReturnStatement s = n.getAST().newReturnStatement();
        s.setExpression(n.getAST().newThisExpression());
        wizard.addStatement(n, s, r, g);
        r.replace(d, n, g);
      }
    };
  }

  /** @param ¢
   * @return */
  protected static Type getType(final AbstractTypeDeclaration ¢) {
    return step.type(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Make setter fluent";
  }
}
