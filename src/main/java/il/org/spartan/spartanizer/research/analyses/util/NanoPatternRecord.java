package il.org.spartan.spartanizer.research.analyses.util;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;

/** Collects statistics for a nano.
 * @author Ori Marcovitch
 * @since 2016 */
public class NanoPatternRecord {
  public final String name;
  public int occurences;
  public int numNPStatements;
  public int numNPExpressions;
  public final String className;

  public NanoPatternRecord(final String name, final Class<? extends ASTNode> cl) {
    this.name = name;
    className = cl.getSimpleName();
  }

  /** @param ¢ matched node */
  public void markNP(final ASTNode ¢) {
    ++occurences;
    if (MethodRecord.excluded(name))
      return;
    numNPStatements += measure.statements(¢);
    numNPExpressions += measure.expressions(¢);
  }
}
