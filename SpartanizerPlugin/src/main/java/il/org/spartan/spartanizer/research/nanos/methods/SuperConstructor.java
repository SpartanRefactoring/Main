package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constructor invoking super constructor
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-10 */
public class SuperConstructor extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x3EFF80AF9AEEB232L;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    return iz.constructor(¢) //
        && iz.superConstructorInvocation(onlyStatement(¢)) //
        && delegating(¢);
  }
  private boolean delegating(final MethodDeclaration ¢) {
    final List<String> ps = parametersNames(¢);
    final Iterable<Expression> as = arguments(az.superConstructorInvocation(onlyStatement(¢)));
    for (final Expression a : as)
      if (!iz.name(¢) && !ps.contains(identifier(az.name(a))))
        return false;
    return true;
  }
  @Override public String tipperName() {
    return "Super";
  }
}
