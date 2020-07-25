package il.org.spartan.spartanizer.research.analyses.analyzers;

import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.utils.Int;

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
  private static Int getSafe(final Map<Integer, Int> m, final Integer i) {
    m.putIfAbsent(i, new Int());
    return m.get(i);
  }
  @Override protected double enumElement(final Int ¢) {
    return ¢.inner;
  }
}
