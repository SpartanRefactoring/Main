package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates a table for analyzing loops distribution and nano pattern applied
 * to loops.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-21 */
public class Table_Iteratives extends DeprecatedFolderASTVisitor {
  private static final int ENHANCED = ASTNode.ENHANCED_FOR_STATEMENT;
  private static final int WHILE = ASTNode.WHILE_STATEMENT;
  static final Nanonizer nanonizer = new Nanonizer();
  static final LoopsStatistics all = new LoopsStatistics();
  static final LoopsStatistics simple = new LoopsStatistics();
  static final LoopsStatistics definites = new LoopsStatistics();
  private static Table rawWriter;
  private static Table summaryWriter;
  static {
    clazz = Table_Iteratives.class;
    Logger.subscribe(Table_Iteratives::logNPInfo);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    closeWriters();
  }
  private static void closeWriters() {
    rawWriter.close();
    summaryWriter.close();
  }
  public static void logNPInfo(final ASTNode n, final String np) {
    if (!iz.loop(n))
      return;
    all.logNPInfo(n, np);
    if (iz.simpleLoop(n))
      simple.logNPInfo(n, np);
    if (iz.definiteLoop(n))
      definites.logNPInfo(n, np);
  }
  @Override public boolean visit(final MethodDeclaration d) {
    if (!excludeMethod(d))
      try {
        log(spartanalyze(d + ""));
      } catch (final MalformedTreeException | AssertionError | IllegalArgumentException ¢) {
        note.bug(¢);
      }
    return false;
  }
  private static void log(final String spartanized) {
    into.cu(spartanized).accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (!iz.loop(¢))
          return;
        all.logNode(¢);
        if (iz.simpleLoop(¢))
          simple.logNode(¢);
        if (iz.definiteLoop(¢))
          definites.logNode(¢);
      }
    });
  }
  private static String spartanalyze(final String ¢) {
    return nanonizer.fixedPoint(WrapIntoComilationUnit.Method.on(¢));
  }
  @Override protected void done(final String path) {
    summarize(path);
    clearAll();
    System.err.println("Finished " + path);
  }
  private static void clearAll() {
    all.clear();
    simple.clear();
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
        .col("EnhancedForLoops", all.totalEnhanced())//
        .col("ForLoops", all.totalFor())//
        .col("WhileLoops", all.totalWhile())//
        .col("DoWhileLoops", all.totalDoWhile())//
        .col("Total Loops", all.total())//
        .col("Definites", definites.total())//
        //
        .col("hitss", all.covered(ENHANCED))//
        .col("Simple EnhancedForLoops", simple.totalEnhanced())//
        .col("Simple ForLoops", simple.totalFor())//
        .col("Simple While Loops", simple.totalWhile())//
        .col("Simple DoWhileLoops", simple.totalDoWhile())//
        .col("Simple Total Loops", simple.total())//
    ;
    summaryWriter//
        .col("Project", path)//
        .col("Coverage", all.coverage())//
        .col("Simple / All", format.perc(simple.total(), all.total()))//
        .col("Simple Coverage", simple.coverage())//
        .col("Enhanced / All", format.perc(all.totalEnhanced(), all.total()))//
        .col("Enhanced Coverage", all.coverage(ENHANCED))//
        .col("Simple Enhanced / Simple", format.perc(simple.totalEnhanced(), simple.total()))//
        .col("Simple Enhanced Coverage", simple.coverage(ENHANCED))//
        .col("While / All", format.perc(all.totalWhile(), all.total()))//
        .col("While coverage", all.coverage(WHILE))//
        .col("Simple While / Simple", format.perc(simple.totalWhile(), simple.total()))//
        .col("Simple While coverage", simple.coverage(WHILE))//
        .col("Definites / All", format.perc(definites.total(), all.total()))//
        .col("Definites Coverage", definites.coverage())//
    ;
    //
    final HashMap<String, Int> hist = all.nanoHistogram(Integer.valueOf(ENHANCED));
    // hist.keySet().forEach(λ -> rawWriter.col(λ + " perc.",
    // format.decimal(100 * safe.div(hist.get(λ).inner,
    // statistics.count(Integer.valueOf(ASTNode.ENHANCED_FOR_STATEMENT))))));
    hist.keySet().forEach(λ -> rawWriter.col(λ, hist.get(λ).inner));
    rawWriter.nl();
    summaryWriter.nl();
  }
}
