package il.org.spartan.spartanizer.research.patterns.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** A DefaultParametersAdder method is such one that calls another method
 * (usually with same name) and just adds parameters to the method.
 * @author Ori Marcovitch
 * @since 2016 */
public class DefaultParametersAdder extends JavadocMarkerNanoPattern {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return $N($A);", "", ""));
      add(patternTipper("return $N.$N2($A);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && (defaulter(¢, onlyStatement(¢)) || defaulter(¢, onlySynchronizedStatementStatement(¢)));
  }

  private static boolean defaulter(final MethodDeclaration d, final Statement s) {
    if (!anyTips(tippers, s))
      return false;
    final Expression $ = expression(s);
    return iz.methodInvocation($) && containsParameters(d, $) && arguments(az.methodInvocation($)).size() > parametersNames(d).size();
  }

  private static boolean containsParameters(final MethodDeclaration ¢, final Expression x) {
    return arguments(az.methodInvocation(x)).stream().filter(n -> iz.name(n)).map(n -> az.name(n) + "").collect(Collectors.toList())
        .containsAll(parametersNames(¢));
  }
}
