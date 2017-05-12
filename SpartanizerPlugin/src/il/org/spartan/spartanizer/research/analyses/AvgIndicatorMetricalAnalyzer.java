package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** Class for averaging whatever about methods before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class AvgIndicatorMetricalAnalyzer extends IndicatorMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return metrics.subtreeUnderstandability2(¢);
  }

  @Override protected double enumElement(final List<Int> is) {
    return 1.0 * is.stream().reduce((x, y) -> Int.valueOf(x.inner + y.inner)).get().inner / is.size();
  }
}
