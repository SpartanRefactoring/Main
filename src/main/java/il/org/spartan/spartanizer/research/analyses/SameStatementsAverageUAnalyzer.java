package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Nov 17, 2016 */
public class SameStatementsAverageUAnalyzer extends AvgMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return metrics.subtreeUnderstandability(¢);
  }
}
