package il.org.spartan.spartanizer.research.analyses.analyzers;

import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Nov 17, 2016 */
public class SameStatementsAverageUAnalyzer extends AvgMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return Metrics.subtreeUnderstandability(¢);
  }
}
