package il.org.spartan.spartanizer.research.nanos.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constructor invoking super constructor
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-10 */
public class SuperConstructor extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 4539488441119715890L;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    return iz.constructor(¢) //
        && iz.superConstructorInvocation(onlyStatement(¢)) //
        && delegating(¢);
  }

  private boolean delegating(MethodDeclaration ¢) {
    List<String> ps = parametersNames(¢);
    Iterable<Expression> as = arguments(az.superConstructorInvocation(onlyStatement(¢)));
    for (Expression a : as)
      if (!iz.name(¢) && !ps.contains(identifier(az.name(a))))
        return false;
    return true;
  }
}
