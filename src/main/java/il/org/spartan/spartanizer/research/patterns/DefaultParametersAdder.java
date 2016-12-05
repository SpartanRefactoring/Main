package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;
import java.util.*;
import java.util.stream.*;
import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** A DefaultParametersAdder method is such one that calls another method
 * (usually with same name) and just adds parameters to the method.
 * @author Ori Marcovitch
 * @since 2016 */
public class DefaultParametersAdder extends JavadocMarkerNanoPattern<MethodDeclaration> {
  Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("return $N($A);", "", ""));
      add(TipperFactory.patternTipper("return $N.$N2($A);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (statements(¢) == null || statements(¢).size() != 1)
      return false;
    final List<Statement> ss = statements(¢);
    if (!anyTips(onlyOne(ss)))
      return false;
    final Expression e = expression(az.returnStatement(ss.get(0)));
    return iz.methodInvocation(e) && containsParameters(¢, e) && arguments(az.methodInvocation(e)).size() > parametersNames(¢).size();
  }

  private static boolean containsParameters(final MethodDeclaration ¢, final Expression x) {
    for (final String pn : parametersNames(¢))
      if (!arguments(az.methodInvocation(x)).stream().filter(n -> iz.name(n)).map(n -> az.name(n) + "").collect(Collectors.toList()).contains(pn))
        return false;
    return true;
  }

  boolean anyTips(final Statement s) {
    return tippers.stream().anyMatch(t -> t.canTip(s));
  }
}
