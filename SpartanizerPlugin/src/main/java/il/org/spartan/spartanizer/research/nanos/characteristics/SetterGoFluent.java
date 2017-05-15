package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class SetterGoFluent extends NanoPatternTipper<MethodDeclaration> {
  private static final long serialVersionUID = -0x63F663D2B18E1DE7L;
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.patternTipper("this.$N", "", "");

  @Override public boolean canTip(final MethodDeclaration ¢) {
    if (step.parameters(¢).size() != 1 || step.body(¢) == null || iz.static¢(¢) || ¢.isConstructor() || !iz.voidType(step.returnType(¢)))
      return false;
    final List<Statement> ss = statements(¢.getBody());
    if (ss.size() != 1 || !iz.expressionStatement(the.headOf(ss)))
      return false;
    final Expression e = az.expressionStatement(the.headOf(ss)).getExpression();
    if (!iz.assignment(e))
      return false;
    final Assignment $ = az.assignment(e);
    return (iz.name(left($)) || tipper.check(left($))) && wizard.eq(right($), the.headOf(parameters(¢)).getName());
  }
  @Override public Tip pattern(final MethodDeclaration d) {
    return new Tip(description(d), getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        if (!iz.voidType(returnType(d)))
          return;
        final MethodDeclaration n = copy.of(d);
        n.setReturnType2(copy.of(getType(yieldAncestors.untilContainingType().from(d))));
        final ReturnStatement s = n.getAST().newReturnStatement();
        s.setExpression(n.getAST().newThisExpression());
        misc.addStatement(n, s, r, g);
        r.replace(d, n, g);
      }
    };
  }
  protected static Type getType(final AbstractTypeDeclaration ¢) {
    return step.type(¢);
  }
  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Make setter fluent";
  }
}
