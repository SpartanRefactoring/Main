package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import java.util.Collection;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Match invocation for a method with same name of containing method but
 * different number of parameters (overloading).
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-01 */
public final class MyName extends NanoPatternTipper<MethodInvocation> {
  private static final long serialVersionUID = 0x3866DF4DC20EF00CL;

  @Override public boolean canTip(final MethodInvocation ¢) {
    final MethodDeclaration $ = yieldAncestors.untilContainingMethod().from(¢);
    return $ != null && identifier($).equals(identifier(¢)) && sameSize(parameters($), arguments(¢));
  }
  private static boolean sameSize(final Collection<SingleVariableDeclaration> parameters, final Collection<Expression> arguments) {
    return arguments != null //
        && parameters != null //
        && arguments.size() != parameters.size();
  }
  @Override public Tip pattern(final MethodInvocation ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final MethodInvocation $ = copy.of(¢);
        $.setName($.getAST().newSimpleName("reduce¢"));
        r.replace(¢, $, g);
      }
    };
  }
  @Override public String description() {
    return "Invocation for a method with same name of containing method but different number of parameters";
  }
}
