package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-21 */
public class Table_Loops extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  static final InteractiveSpartanizer iSpartanayzer = new InteractiveSpartanizer();
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
    return true;
  }

  @Override public boolean visit(final ForStatement ¢) {
    analyze(¢);
    return true;
  }

  @Override public boolean visit(final WhileStatement ¢) {
    analyze(¢);
    return true;
  }

  @Override public boolean visit(final DoStatement ¢) {
    analyze(¢);
    return true;
  }

  private static void analyze(final ASTNode ¢) {
    ¢.accept(new CleanerVisitor());
    try {
      log(wizard.ast(spartanize(¢)));
    } catch (@SuppressWarnings("unused") AssertionError __) {
      System.out.print("X");
    }
  }

  private static String spartanize(final ASTNode ¢) {
    return Wrap.Method.off(iSpartanayzer.fixedPoint(Wrap.Method.on(¢ + "")));
  }

  private static void log(final ASTNode ¢) {
    statistics.log(¢);
    if (iz.simpleLoop(¢))
      simpleStatistics.log(¢);
  }

  @Override protected void done(final String path) {
    summarize(path);
    clearAll();
    System.err.println("Finished " + path);
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
        .col("DoWhileLoops", statistics.doWhileLoops())//
        .col("Total Loops", statistics.totalLoops())//
        .col("Definites", statistics.definites())//
        // //
        //// .col("Simple Coverage", simpleCoverage())//
        .col("Simple EnhancedForLoops", simpleStatistics.enhancedForLoops())//
        .col("Simple ForLoops", simpleStatistics.forLoops())//
        .col("Simple While Loops", simpleStatistics.whileLoops())//
        .col("Simple DoWhileLoops", simpleStatistics.doWhileLoops())//
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
