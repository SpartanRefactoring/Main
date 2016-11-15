package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class UnderstandabilityAnalyzer extends MetricalAnalyzer {
  @Override protected int metric(ASTNode ¢) {
    return metrics.understandability(¢);
  }
}
