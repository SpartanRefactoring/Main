package il.org.spartan.spartanizer.research.patterns.characteristics;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class SetterGoFluent extends NanoPatternTipper<MethodDeclaration> {
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.patternTipper("this.$N", "", "");

  @Override public boolean canTip(final MethodDeclaration ¢) {
    if (step.parameters(¢).size() != 1 || step.body(¢) == null || iz.static¢(¢) || ¢.isConstructor() || !iz.voidType(step.returnType(¢)))
      return false;
    @SuppressWarnings("unchecked") final List<Statement> ss = ¢.getBody().statements();
    if (ss.size() != 1 || !iz.expressionStatement(ss.get(0)))
      return false;
    final Expression e = az.expressionStatement(ss.get(0)).getExpression();
    if (!iz.assignment(e))
      return false;
    final Assignment $ = az.assignment(e);
    return (iz.name($.getLeftHandSide()) || tipper.canTip($.getLeftHandSide()))
        && wizard.same($.getRightHandSide(), step.parameters(¢).get(0).getName());
  }

  @Override public Tip pattern(final MethodDeclaration d) {
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
