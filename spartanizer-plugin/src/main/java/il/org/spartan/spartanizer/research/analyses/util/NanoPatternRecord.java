package il.org.spartan.spartanizer.research.analyses.util;

import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.spartanizer.research.MethodRecord;
import il.org.spartan.spartanizer.research.util.measure;

/** Collects statistics for a nano.
 * @author Ori Marcovitch */
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
    numNPStatements += measure.commands(¢);
    numNPExpressions += measure.expressions(¢);
  }
}
