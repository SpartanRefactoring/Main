package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
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
public class Table_Summary extends Table_ReusabilityIndices {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final NanoPatternsStatistics npStatistics = new NanoPatternsStatistics();
  private static final NanoPatternsOccurencesStatistics npDistributionStatistics = new NanoPatternsOccurencesStatistics();
  private static final Stack<MethodRecord> scope = new Stack<>();
  private static Table writer;
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>(Integer::compareTo);
  static {
    clazz = Table_Summary.class;
    Logger.subscribe(Table_Summary::logNanoContainingMethodInfo);
    Logger.subscribe(npStatistics::logNPInfo);
    Logger.subscribe(npDistributionStatistics::logNPInfo);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    writer.close();
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    if (excludeMethod(¢))
      return true;
    try {
      final Integer key = Integer.valueOf(measure.statements(¢));
      statementsCoverageStatistics.putIfAbsent(key, new ArrayList<>());
      final MethodRecord m = new MethodRecord(¢);
      scope.push(m);
      statementsCoverageStatistics.get(key).add(m);
      final MethodDeclaration d = findFirst.instanceOf(MethodDeclaration.class)
          .in(ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
      if (d != null)
        npDistributionStatistics.logNode(d);
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

  @Override protected void done(final String path) {
    summarizeSortedMethodStatistics(path);
    clearAll();
    System.err.println("Output is in: " + Table.temporariesFolder + path);
  }

  private static void clearAll() {
    statementsCoverageStatistics.clear();
    npDistributionStatistics.clear();
    npStatistics.clear();
    scope.clear();
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        || step.body(¢) == null//
        || extract.annotations(¢).stream().anyMatch(λ -> "@Test".equals(λ + ""));
  }

  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    if (!containedInInstanceCreation(n))
      scope.peek().markNP(n, np);
  }

  private static void initializeWriter() {
    writer = new Table(clazz);
  }

  public void summarizeSortedMethodStatistics(final String path) {
    if (writer == null)
      initializeWriter();
    writer//
        .col("Project", path)//
        .col("R-Index", rMethod())//
        .col("Nanos adopted", adopted())//
        .col("Statements", statements())//
        .col("Coverage", statementsCoverage())//
        .col("Methods", methods())//
        .col("Fmethods", fMethods())//
        .col("Touched", touched())//
        .col("Iteratives Coverage", iterativesCoverage())//
        .col("ConditionalExps", conditionalExpressionsCoverage())//
        .col("ConditionalStmts", conditionalStatementsCoverage())//
        .col("Expressions", expressionsCoverage())//
        .nl();
  }

  private static int statements() {
    return statementsCoverageStatistics.keySet().stream().mapToInt(λ -> Unbox.it(λ) * statementsCoverageStatistics.get(λ).size()).sum();
  }

  private static int statementsCovered() {
    return statementsCoverageStatistics.values().stream().flatMap(Collection::stream).mapToInt(λ -> λ.numNPStatements).sum();
  }

  private static int expressionsCovered() {
    return statementsCoverageStatistics.values().stream().flatMap(Collection::stream).mapToInt(λ -> λ.numNPExpressions).sum();
  }

  private static int expressions() {
    return statementsCoverageStatistics.values().stream().flatMap(Collection::stream).mapToInt(λ -> λ.numExpressions).sum();
  }

  private static int methods() {
    return (int) statementsCoverageStatistics.values().stream().flatMap(λ -> λ.stream()).count();
  }

  private static double fMethods() {
    return getNodeCoverage(ASTNode.METHOD_DECLARATION);
  }

  private static double getNodeCoverage(final int type) {
    return npDistributionStatistics.coverage(type);
  }

  private static double iterativesCoverage() {
    return getNodeCoverage(ASTNode.ENHANCED_FOR_STATEMENT);
  }

  private static double conditionalExpressionsCoverage() {
    return getNodeCoverage(ASTNode.CONDITIONAL_EXPRESSION);
  }

  private static double conditionalStatementsCoverage() {
    return getNodeCoverage(ASTNode.IF_STATEMENT);
  }

  private long adopted() {
    final int $ = rMethod();
    return npStatistics.keySet().stream()//
        .map(npStatistics::get)//
        .filter(λ -> λ.occurences > $).count();
  }

  private static double touched() {
    return format.perc(methodsTouched(), methods() - methodsCovered());
  }

  private static double statementsCoverage() {
    return format.perc(statementsCovered(), statements());
  }

  private static double expressionsCoverage() {
    return format.perc(expressionsCovered(), expressions());
  }

  private static int methodsTouched() {
    return (int) statementsCoverageStatistics.values().stream().flatMap(λ -> λ.stream())
        .filter(λ -> (λ.numNPStatements > 0 || λ.numNPExpressions > 0) && !λ.fullyMatched()).count();
  }

  private static int methodsCovered() {
    return (int) statementsCoverageStatistics.values().stream().flatMap(Collection::stream).filter(MethodRecord::fullyMatched).count();
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
