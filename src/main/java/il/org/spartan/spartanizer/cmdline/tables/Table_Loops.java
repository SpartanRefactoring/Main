package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-21 */
public class Table_Loops extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final LoopsStatistics statistics = new LoopsStatistics();
  private static final LoopsStatistics simpleStatistics = new LoopsStatistics();
  private static Table writer; // coverage
  static {
    clazz = Table_Loops.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    writer.close();
  }

  @Override public boolean visit(final EnhancedForStatement ¢) {
    analyze(¢);
    ¢.accept(new CleanerVisitor());
    // spartanalyzer.fixedPoint(Wrap.Method.on(¢ + ""));
    return true;
  }

  private static void analyze(final ASTNode ¢) {
    statistics.log(¢);
    if (iz.simpleLoop(¢))
      simpleStatistics.log(¢);
  }

  @Override protected void done(final String path) {
    summarize(path);
    clearAll();
    System.err.println("Finished");
  }

  private static void clearAll() {
    statistics.clear();
    simpleStatistics.clear();
  }

  private static void initializeWriter() {
    writer = new Table(Table_Loops.class.getSimpleName());
  }

  public static void summarize(final String path) {
    if (writer == null)
      initializeWriter();
    writer//
        .col("Project", path)//
        //// .col("Coverage", coverage())//
        .col("EnhancedForLoops", statistics.enhancedForLoops())//
        .col("ForLoops", statistics.forLoops())//
        .col("WhileLoops", statistics.whileLoops())//
        .col("WhileLoops", statistics.doWhileLoops())//
        .col("Total Loops", statistics.totalLoops())//
        .col("Definites", statistics.definites())//
        // //
        //// .col("Simple Coverage", simpleCoverage())//
        .col("Simple EnhancedForLoops", simpleStatistics.enhancedForLoops())//
        .col("Simple ForLoops", simpleStatistics.forLoops())//
        .col("Simple While Loops", simpleStatistics.whileLoops())//
        .col("WhileLoops", simpleStatistics.doWhileLoops())//
        .col("Simple Total Loops", simpleStatistics.totalLoops())//
        .col("Simple Definites", simpleStatistics.definites())//
        //
        // final HashMap<String, Int> hist = npStatistics.nanoHistogram(type);
        // for (final String ¢ : hist.keySet())
        // writer.col(¢ + " perc.", format.decimal(100 *
        //// safe.div(hist.get(¢).inner, npStatistics.count(type))));
        // for (final String ¢ : hist.keySet())
        // writer.col(¢, hist.get(¢).inner);
        .nl();
  }
}
