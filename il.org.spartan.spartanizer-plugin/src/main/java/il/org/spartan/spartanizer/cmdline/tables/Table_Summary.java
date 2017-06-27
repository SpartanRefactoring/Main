package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;

/** Generates a table summarizing important statistics about nano patterns
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-13 */
public class Table_Summary extends NanoTable {
  static {
    Logger.subscribe(statistics::markNP);
    Logger.subscribe(npDistributionStatistics::logNPInfo);
  }

  public static void main(final String[] args) {
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(getCurrentLocation());
          }
        });
      }

      protected void done(final String path) {
        summarize(path);
        reset();
        System.err.println(" " + path + " Done"); // we need to know if the
                                                  // process is finished or hang
      }
      public void summarize(final String path) {
        initializeWriter();
        table//
            .col("Project", path)//
            .col("Commands", statementsCoverage())//
            .col("Expressions", expressionsCoverage())//
            .col("Nodes", statistics.nodesCoverage())//
            .col("Methods", methodsCovered())//
            .col("Touched", touched())//
            .col("Iteratives", iterativesCoverage())//
            .col("ConditionalExpressions", conditionalExpressionsCoverage())//
            .col("ConditionalCommands", conditionalStatementsCoverage())//
            // .col("total Commands", commands())//
            // .col("total Methods", methods())//
            .nl();
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(Table.classToNormalizedFileName(Table_Summary.class) + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        try {
          ¢.accept(new AnnotationCleanerVisitor());
          statistics.logCompilationUnit(¢);
          final String spartanzied = spartanizer.fixedPoint(¢);
          logAfterSpartanization(into.cu(spartanzied));
          analyze.apply(spartanzied);
        } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
          forget.em(__);
        }
        return true;
      }
      void logAfterSpartanization(final CompilationUnit ¢) {
        statistics.logAfterSpartanization(¢);
        npDistributionStatistics.logNode(¢);
      }
    });
    table.close();
  }
  static int commands() {
    return statistics.commands();
  }
  static int methods() {
    return statistics.methods();
  }
  static double methodsCovered() {
    return getNodeCoverage(ASTNode.METHOD_DECLARATION);
  }
  private static double getNodeCoverage(final int type) {
    return npDistributionStatistics.coverage(type);
  }
  static double iterativesCoverage() {
    return npDistributionStatistics.coverage(ASTNode.ENHANCED_FOR_STATEMENT, ASTNode.FOR_STATEMENT, ASTNode.WHILE_STATEMENT, ASTNode.DO_STATEMENT);
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
