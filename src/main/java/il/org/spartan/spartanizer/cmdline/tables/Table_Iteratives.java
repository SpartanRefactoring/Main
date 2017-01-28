package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.*;

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
  private static final int ENHANCED = ASTNode.ENHANCED_FOR_STATEMENT;
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  static final LoopsStatistics statistics = new LoopsStatistics();
  static final LoopsStatistics simpleStatistics = new LoopsStatistics();
  static final LoopsStatistics definites = new LoopsStatistics();
  private static Table rawWriter;
  private static Table summaryWriter;
  static {
    clazz = Table_Iteratives.class;
    Logger.subscribe(Table_Iteratives::logNPInfo);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    closeWriters();
  }

  private static void closeWriters() {
    rawWriter.close();
    summaryWriter.close();
  }

  public static void logNPInfo(final ASTNode n, final String np) {
    if (!iz.loop(n))
      return;
    statistics.logNPInfo(n, np);
    if (iz.simpleLoop(n))
      simpleStatistics.logNPInfo(n, np);
    if (iz.definiteLoop(n))
      definites.logNPInfo(n, np);
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    if (!excludeMethod(¢))
      try {
        log(spartanalyze(¢ + ""));
      } catch (@SuppressWarnings("unused") final MalformedTreeException | AssertionError | IllegalArgumentException __) {
        System.out.print("X");
      }
    return false;
  }

  private static void log(final String spartanized) {
    into.cu(spartanized).accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (!iz.loop(¢))
          return;
        statistics.logNode(¢);
        if (iz.simpleLoop(¢))
          simpleStatistics.logNode(¢);
        if (iz.definiteLoop(¢))
          definites.logNode(¢);
      }
    });
  }

  private static String spartanalyze(final String ¢) {
    return spartanalyzer.fixedPoint(Wrap.Method.on(¢));
  }

  @Override protected void done(final String path) {
    summarize(path);
    clearAll();
    System.err.println("Finished " + path);
  }

  private static void clearAll() {
    statistics.clear();
    simpleStatistics.clear();
    definites.clear();
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
        .col("EnhancedForLoops", statistics.totalEnhanced())//
        .col("ForLoops", statistics.totalFor())//
        .col("WhileLoops", statistics.totalWhile())//
        .col("DoWhileLoops", statistics.totalDoWhile())//
        .col("Total Loops", statistics.total())//
        .col("Definites", definites.total())//
        //
        .col("Simple EnhancedForLoops", simpleStatistics.totalEnhanced())//
        .col("Simple ForLoops", simpleStatistics.totalFor())//
        .col("Simple While Loops", simpleStatistics.totalWhile())//
        .col("Simple DoWhileLoops", simpleStatistics.totalDoWhile())//
        .col("Simple Total Loops", simpleStatistics.total())//
    ;
    summaryWriter//
        .col("Project", path)//
        .col("Coverage", coverage())//
        .col("Simple", format.perc(simpleStatistics.total(), statistics.total()))//
        .col("Simple Coverage", simpleStatistics.coverage())//
        .col("Enhanced", format.perc(statistics.totalEnhanced(), statistics.total()))//
        .col("Enhanced Coverage", statistics.coverage(ENHANCED))//
        .col("Simple Enhanced", format.perc(simpleStatistics.totalEnhanced(), simpleStatistics.total()))//
        .col("Simple Enhanced Coverage", simpleStatistics.coverage(ENHANCED))//
        .col("Definites", format.perc(definites.total(), statistics.total()))//
        .col("Definites Coverage", definites.coverage())//
    ;
    //
    final HashMap<String, Int> hist = statistics.nanoHistogram(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT));
    // hist.keySet().forEach(λ -> rawWriter.col(λ + " perc.",
    // format.decimal(100 * safe.div(hist.get(λ).inner,
    // statistics.count(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT))))));
    hist.keySet().forEach(λ -> rawWriter.col(λ, hist.get(λ).inner));
    rawWriter.nl();
    summaryWriter.nl();
  }

  private static double coverage() {
    return statistics.coverage();//
    // format.decimal(100 *
    // npStatistics.coverage(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT)));
  }
}
