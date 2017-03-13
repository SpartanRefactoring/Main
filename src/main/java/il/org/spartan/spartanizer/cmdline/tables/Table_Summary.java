package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates a table summarizing important statistics about nano patterns
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
public class Table_Summary {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final NanoPatternsStatistics npStatistics = new NanoPatternsStatistics();
  static final NanoPatternsOccurencesStatistics npDistributionStatistics = new NanoPatternsOccurencesStatistics();
  static final Stack<MethodRecord> scope = new Stack<>();
  static Table writer;
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>(Integer::compareTo);
  static {
    Logger.subscribe(Table_Summary::logNanoContainingMethodInfo);
    Logger.subscribe(npStatistics::logNPInfo);
    Logger.subscribe(npDistributionStatistics::logNPInfo);
  }

  public static void main(final String[] args) {
    new FileSystemASTVisitor(args) {
      @Override protected void done(final String path) {
        summarize(path);
        resetAll();
      }

      public void summarize(final String path) {
        if (writer == null)
          initializeWriter();
        writer//
            .col("Project", path)//
            .col("Commands", statementsCoverage())//
            .col("Expressions", expressionsCoverage())//
            .col("methodsCovered", fMethods())//
            .col("methodsTouched", touched())//
            .col("Iteratives", iterativesCoverage())//
            .col("ConditionalExpressions", conditionalExpressionsCoverage())//
            .col("ConditionalCommands", conditionalStatementsCoverage())//
            .col("total Commands", statements())//
            .col("total Methods", methods())//
            .nl();
      }
    }.fire(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        if (excludeMethod(¢))
          return true;
        try {
          final MethodRecord m = new MethodRecord(¢);
          scope.push(m);
          final MethodDeclaration d = findFirst.instanceOf(MethodDeclaration.class)
              .in(ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
          if (d != null)
            npDistributionStatistics.logNode(d);
          final Integer key = Integer.valueOf(measure.commands(d));
          statementsCoverageStatistics.putIfAbsent(key, new ArrayList<>());
          statementsCoverageStatistics.get(key).add(m);
        } catch (final AssertionError __) {
          ___.unused(__);
        }
        return true;
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        spartanalyzer.fixedPoint(ast(¢ + ""));
        return true;
      }

      @Override public void endVisit(final MethodDeclaration ¢) {
        if (!excludeMethod(¢))
          scope.pop();
      }

      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new CleanerVisitor());
        return true;
      }
    });
    writer.close();
  }

  static void resetAll() {
    statementsCoverageStatistics.clear();
    npDistributionStatistics.clear();
    npStatistics.clear();
    scope.clear();
  }

  static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        || step.body(¢) == null//
        || extract.annotations(¢).stream().anyMatch(λ -> "@Test".equals(λ + ""));
  }

  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    if (!containedInInstanceCreation(n))
      scope.peek().markNP(n, np);
  }

  static void initializeWriter() {
    writer = new Table(Table_Summary.class);
  }

  static int statements() {
    return statementsCoverageStatistics.keySet().stream().mapToInt(λ -> Unbox.it(λ) * statementsCoverageStatistics.get(λ).size()).sum();
  }

  private static int statementsCovered() {
    return statementsCoverageStatistics.values().stream().flatMap(Collection::stream).mapToInt(MethodRecord::numNPStatements).sum();
  }

  private static int expressionsCovered() {
    return statementsCoverageStatistics.values().stream().flatMap(Collection::stream).mapToInt(MethodRecord::numNPExpressions).sum();
  }

  private static int expressions() {
    return statementsCoverageStatistics.values().stream().flatMap(Collection::stream).mapToInt(λ -> λ.numExpressions).sum();
  }

  static int methods() {
    return (int) statementsCoverageStatistics.values().stream().mapToLong(Collection::size).sum();
  }

  static double fMethods() {
    return getNodeCoverage(ASTNode.METHOD_DECLARATION);
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
    return format.perc(methodsTouched(), methods() - methodsCovered());
  }

  static double statementsCoverage() {
    return format.perc(statementsCovered(), statements());
  }

  static double expressionsCoverage() {
    return format.perc(expressionsCovered(), expressions());
  }

  private static int methodsTouched() {
    return (int) statementsCoverageStatistics.values().stream().flatMap(Collection::stream).filter(λ -> λ.touched() && !λ.fullyMatched()).count();
  }

  private static int methodsCovered() {
    return (int) statementsCoverageStatistics.values().stream().flatMap(Collection::stream).filter(MethodRecord::fullyMatched).count();
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
