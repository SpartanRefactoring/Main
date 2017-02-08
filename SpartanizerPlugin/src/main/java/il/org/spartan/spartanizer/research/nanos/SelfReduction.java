package il.org.spartan.spartanizer.research.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Match invocation for a method with same name of containing method but
 * different number of parameters (overloading).
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-01 */
public final class SelfReduction extends NanoPatternTipper<MethodInvocation> {
  @Override public boolean canTip(final MethodInvocation ¢) {
    final MethodDeclaration $ = yieldAncestors.untilContainingMethod().from(¢);
    return $ == null ? false : identifier($).equals(identifier(¢)) && sameSize(parameters($), arguments(¢));
  }

  private static boolean sameSize(final List<SingleVariableDeclaration> parameters, final List<Expression> arguments) {
    return arguments != null //
        && parameters != null //
        && arguments.size() != parameters.size();
  }

  @Override public Tip pattern(final MethodInvocation ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final MethodInvocation $ = copy.of(¢);
        $.setName($.getAST().newSimpleName("reduce¢"));
        r.replace(¢, $, g);
      }
    };
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Invocation for a method with same name of containing method but different number of parameters";
  }
}
