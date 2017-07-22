package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.analyses.analyzers.*;
import il.org.spartan.spartanizer.research.util.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class MagicNumbersAnalysis extends IntegerMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return measure.commands(¢);
  }
}
