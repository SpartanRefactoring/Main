package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** Including static setters.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-10-22 */
public class Setter extends JavadocMarkerNanoPattern {
  private static final Collection<UserDefinedTipper<Expression>> tippers = new HashSet<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("this.$N1 = $N2", "", ""));
      add(patternTipper("this.$N1 = $L", "", ""));
      add(patternTipper("$N1 = $N2", "", ""));
      add(patternTipper("$N1 = $L", "", ""));
    }
  };

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    final List<String> $ = parametersNames(¢);
    ___.nothing();
    return notConstructor(¢)//
        && notEmpty(¢)
        && statements(¢).stream().allMatch(λ -> anyTips(tippers, expression(λ)) && isRightSideOK(right(az.assignment(expression(λ))), $));
  }

  private static boolean isRightSideOK(final Expression $, final Collection<String> paramNames) {
    return iz.literal($) || paramNames.contains(identifier(az.name($)));
  }
}
