package il.org.spartan.spartanizer.research.analyses.analyzers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.nodes.metrics.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Nov 17, 2016 */
public class SameStatementsAverageUAnalyzer extends AvgMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return Metrics.subtreeUnderstandability(¢);
  }
}
