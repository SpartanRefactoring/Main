package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** Prioritize the use of "contains" string operation over "indexOf"
 * @author Ori Marcovitch
 * @since 2016 */
public final class InfixIndexOfToStringContains extends Tipper<InfixExpression>//
    implements TipperCategory.Idiomatic {
  /** Adds to the "tippers" list the basic transformation regarding to
   * "contains" method of String. This list is static, and therefore should be
   * initialized only once. */
  private static final List<UserDefinedTipper<InfixExpression>> tippers = as.list(
      patternTipper("$X1.indexOf($X2) >= 0", "$X1.contains($X2)", "replace indexOf >= 0 with contains"),
      patternTipper("$X1.indexOf($X2) < 0", "!$X1.contains($X2)", "replace indexOf <0 with !contains"),
      patternTipper("$X1.indexOf($X2) != -1", "$X1.contains($X2)", "replace indexOf != -1 with contains"),
      patternTipper("$X1.indexOf($X2) == -1", "!$X1.contains($X2)", "replace indexOf == -1 with !contains"),
      patternTipper("$X1.indexOf($X2) <= -1", "!$X1.contains($X2)", "replace indexOf == -1 with !contains")//
  );

  /** Indicates if the infix expression contains two strings with string
   * operation between them */
  @Override public boolean canTip(final InfixExpression x) {
    return tippers.stream().anyMatch(λ -> λ.canTip(x) && certain.string(λ.getMatching(x, "$X1"), λ.getMatching(x, "$X2")));
  }

  /** Operates the first tip that can be implemented. */
  @Override public Tip tip(final InfixExpression ¢) {
    return tippers.stream().filter(λ -> λ.canTip(¢)).map(λ -> λ.tip(¢)).findFirst().orElse(null);
  }

  /** @return the first description of tip that can be implemented. */
  @Override public String description(final InfixExpression ¢) {
    return tippers.stream().filter(λ -> λ.canTip(¢)).map(λ -> λ.description(¢)).findFirst().orElse(null);
  }
}
