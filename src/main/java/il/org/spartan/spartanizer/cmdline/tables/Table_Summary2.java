package il.org.spartan.spartanizer.cmdline.tables;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates a table summarizing important statistics about nano patterns
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
public class Table_Summary2 {
  static final AgileSpartanizer spartanizer = new AgileSpartanizer();
  static final CompilationUnitCoverageStatistics statistics = new CompilationUnitCoverageStatistics();
  static final NanoPatternsOccurencesStatistics npDistributionStatistics = new NanoPatternsOccurencesStatistics();
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  protected static Function<String, String> analyze = spartanalyzer::fixedPoint;
  static Table writer;
  static {
    Logger.subscribe(statistics::markNP);
    Logger.subscribe(npDistributionStatistics::logNPInfo);
  }

  public static void summarize(final String path) {
    if (writer == null)
      initializeWriter();
    writer//
        .col("Project", path)//
        .col("Commands", statementsCoverage())//
        .col("Expressions", expressionsCoverage())//
        .col("methodsCovered", methodsCovered())//
        .col("methodsTouched", touched())//
        .col("Iteratives", iterativesCoverage())//
        .col("ConditionalExpressions", conditionalExpressionsCoverage())//
        .col("ConditionalCommands", conditionalStatementsCoverage())//
        .col("total Commands", commands())//
        .col("total Methods", methods())//
        .nl();
  }

  public static void main(final String[] args) {
    new FileSystemASTVisitor(args) {
      @Override protected void done(final String path) {
        summarize(path);
        reset();
      }
    }.fire(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new AnnotationCleanerVisitor());
        try {
          statistics.logCompilationUnit(¢);
          final String spartanzied = spartanizer.fixedPoint(¢);
          logAfterSpartanization(into.cu(spartanzied));
          analyze.apply(spartanzied);
        } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
          ___.unused(__);
        }
        return true;
      }

      void logAfterSpartanization(CompilationUnit ¢) {
        statistics.logAfterSpartanization(¢);
        npDistributionStatistics.logNode(¢);
      }
    });
    writer.close();
  }

  static void reset() {
    statistics.clear();
    npDistributionStatistics.clear();
  }

  static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        || step.body(¢) == null//
        || extract.annotations(¢).stream().anyMatch(λ -> "@Test".equals(λ + ""));
  }

  static void initializeWriter() {
    writer = new Table(Table_Summary2.class);
  }

  static int commands() {
    return statistics.commands();
  }

  static int methods() {
    return statistics.methods();
  }

  static double methodsCovered() {
    return statistics.methodsCoverage();
  }

  private static double getNodeCoverage(final int type) {
    return npDistributionStatistics.coverage(type);
  }

  static double iterativesCoverage() {
    return getNodeCoverage(ASTNode.ENHANCED_FOR_STATEMENT);
  }

  static double conditionalExpressionsCoverage() {
    return getNodeCoverage(ASTNode.CONDITIONAL_EXPRESSION);
  }

  static double conditionalStatementsCoverage() {
    return getNodeCoverage(ASTNode.IF_STATEMENT);
  }

  static double touched() {
    return statistics.touched();
  }

  static double statementsCoverage() {
    return statistics.commandsCoverage();
  }

  static double expressionsCoverage() {
    return statistics.expressionsCoverage();
  }
}
