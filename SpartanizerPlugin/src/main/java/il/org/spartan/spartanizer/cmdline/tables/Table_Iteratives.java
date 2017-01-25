package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

/** Generates a table for analyzing loops distribution and nano pattern applied
 * to loops.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-21 */
public class Table_Iteratives extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  static final InteractiveSpartanizer iSpartanayzer = new InteractiveSpartanizer();
  private static final LoopsStatistics statistics = new LoopsStatistics();
  private static final LoopsStatistics simpleStatistics = new LoopsStatistics();
  private static final NanoPatternsDistributionStatistics npStatistics = new NanoPatternsDistributionStatistics();
  private static Table rawWriter;
  private static Table summaryWriter;
  static {
    clazz = Table_Iteratives.class;
    Logger.subscribe(npStatistics::logNPInfo);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    rawWriter.close();
  }

  @Override public boolean visit(final EnhancedForStatement ¢) {
    return analyze(¢);
  }

  @Override public boolean visit(final ForStatement ¢) {
    return analyze(¢);
  }

  @Override public boolean visit(final WhileStatement ¢) {
    return analyze(¢);
  }

  @Override public boolean visit(final DoStatement ¢) {
    return analyze(¢);
  }

  private static boolean analyze(final ASTNode ¢) {
    ¢.accept(new CleanerVisitor());
    try {
      final ASTNode n = into.s(spartanize(parent(¢)));
      log(extract.singleStatement(findFirst.statement(n)));
      npStatistics.logMethod(intoMethod(spartanalyze(n)));
    } catch (@SuppressWarnings("unused") final AssertionError __) {
      System.out.print("X");
    }
    return false;
  }

  private static MethodDeclaration intoMethod(final String ¢) {
    return findFirst.methodDeclaration(into.cu(¢));
  }

  private static String spartanalyze(final ASTNode ¢) {
    return spartanalyzer.fixedPoint(Wrap.Statement.on(¢ + ""));
  }

  private static String spartanize(final ASTNode ¢) {
    return Wrap.Statement.off(iSpartanayzer.fixedPoint(Wrap.Statement.on(¢ + "")));
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

  private static void initializeWritersIfNeeded() {
    if (rawWriter != null)
      return;
    rawWriter = new Table("Table_Loops_Raw");
    summaryWriter = new Table("Table_Loops_Summary");
  }

  public static void summarize(final String path) {
    initializeWritersIfNeeded();
    rawWriter//
        .col("Project", path)//
        .col("EnhancedForLoops", statistics.enhancedForLoops())//
        .col("ForLoops", statistics.forLoops())//
        .col("WhileLoops", statistics.whileLoops())//
        .col("DoWhileLoops", statistics.doWhileLoops())//
        .col("Total Loops", statistics.totalLoops())//
        .col("Definites", statistics.definites())//
        //
        //// .col("Simple Coverage", simpleCoverage())//
        .col("Simple EnhancedForLoops", simpleStatistics.enhancedForLoops())//
        .col("Simple ForLoops", simpleStatistics.forLoops())//
        .col("Simple While Loops", simpleStatistics.whileLoops())//
        .col("Simple DoWhileLoops", simpleStatistics.doWhileLoops())//
        .col("Simple Total Loops", simpleStatistics.totalLoops())//
        .col("Simple Definites", simpleStatistics.definites())//
    ;
    summaryWriter//
        .col("Project", path)//
        .col("Coverage", format.decimal(100 * npStatistics.coverage(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT))))//
        .col("EnhancedForLoops", statistics.enhancedForLoops())//
        .col("ForLoops", statistics.forLoops())//
        .col("WhileLoops", statistics.whileLoops())//
        .col("DoWhileLoops", statistics.doWhileLoops())//
        .col("Total Loops", statistics.totalLoops())//
        .col("Definites", statistics.definites())//
        //
        //// .col("Simple Coverage", simpleCoverage())//
        .col("Simple EnhancedForLoops", simpleStatistics.enhancedForLoops())//
        .col("Simple ForLoops", simpleStatistics.forLoops())//
        .col("Simple While Loops", simpleStatistics.whileLoops())//
        .col("Simple DoWhileLoops", simpleStatistics.doWhileLoops())//
        .col("Simple Total Loops", simpleStatistics.totalLoops())//
        .col("Simple Definites", simpleStatistics.definites())//
    ;
    //
    final HashMap<String, Int> hist = npStatistics.nanoHistogram(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT));
    hist.keySet().forEach(¢ -> rawWriter.col(¢ + " perc.",
        format.decimal(100 * safe.div(hist.get(¢).inner, npStatistics.count(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT))))));
    hist.keySet().forEach(¢ -> rawWriter.col(¢, hist.get(¢).inner));
    rawWriter.nl();
  }
}
