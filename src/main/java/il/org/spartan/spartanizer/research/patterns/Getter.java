package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Getter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @SuppressWarnings("serial") Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    {
      add(TipperFactory.tipper("return $N;", "", ""));
      add(TipperFactory.tipper("return this.$N;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (step.body(d) == null || step.statements(step.body(d)) == null || step.statements(step.body(d)).isEmpty())
      return false;
    for (final UserDefinedTipper<Statement> ¢ : tippers)
      if (¢.canTip(step.statements(step.body(d)).get(0)))
        return true;
    return false;
  }

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + " is a getter method";
  }

  @Override protected String javadoc() {
    return "[[Getter]]";
  }
}
