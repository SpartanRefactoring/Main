package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import il.org.spartan.spartanizer.utils.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class IntegerMetricalAnalyzer extends MetricalAnalyzer<Int> {
  @Override protected abstract int metric(ASTNode n);

  @Override public void logMethod(final MethodDeclaration before, final MethodDeclaration after) {
    ++getSafe(beforeHistogram, Integer(metric(before))).inner;
    ++getSafe(afterHistogram, Integer(metric(after))).inner;
  }

  @Override protected double enumElement(final Int ¢) {
    return ¢.inner;
  }
}
