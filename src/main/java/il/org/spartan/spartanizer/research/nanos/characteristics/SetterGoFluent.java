package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class SetterGoFluent extends NanoPatternTipper<MethodDeclaration> {
  private static final long serialVersionUID = -7203054410598850023L;
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.patternTipper("this.$N", "", "");

  @Override public boolean canTip(final MethodDeclaration ¢) {
    if (parameters(¢).size() != 1 || body(¢) == null || iz.static¢(¢) || ¢.isConstructor() || !iz.voidType(step.returnType(¢)))
      return false;
    final List<Statement> ss = statements(¢.getBody());
    if (ss.size() != 1 || !iz.expressionStatement(first(ss)))
      return false;
    final Expression e = az.expressionStatement(first(ss)).getExpression();
    if (!iz.assignment(e))
      return false;
    final Assignment $ = az.assignment(e);
    return (iz.name(left($)) || tipper.canTip(left($))) && wizard.same(right($), first(parameters(¢)).getName());
  }

  @Override public Tip pattern(final MethodDeclaration d) {
    return new Tip(description(d), d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        if (!iz.voidType(returnType(d)))
          return;
        final MethodDeclaration n = az.methodDeclaration(ASTNode.copySubtree(d.getAST(), d));
        n.setReturnType2(az.type(ASTNode.copySubtree(n.getAST(), getType(yieldAncestors.untilContainingType().from(d)))));
        final ReturnStatement s = n.getAST().newReturnStatement();
        s.setExpression(n.getAST().newThisExpression());
        wizard.addStatement(n, s, r, g);
        r.replace(d, n, g);
      }
    };
  }

  protected static Type getType(final AbstractTypeDeclaration ¢) {
    return type(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Make setter fluent";
  }
}
