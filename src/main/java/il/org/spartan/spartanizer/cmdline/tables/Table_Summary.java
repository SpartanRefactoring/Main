package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

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
public class Table_Summary extends TableReusabilityIndices {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final NanoPatternsStatistics npStatistics = new NanoPatternsStatistics();
  private static final NanoPatternsOccurencesStatistics npDistributionStatistics = new NanoPatternsOccurencesStatistics();
  private static final Stack<MethodRecord> scope = new Stack<>();
  private static Table writer;
  private static int totalStatements;
  protected static int totalMethods;
  protected static int totalMethodsAfterSpartanization;
  private static int totalStatementsCovered;
  private static int totalMethodsTouched;
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
      return false;
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
    totalMethodsTouched = totalStatementsCovered = totalMethods = totalStatements = 0;
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || extract.annotations(¢).stream().anyMatch(λ -> "@Test".equals(λ .toString()));
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
    gatherGeneralStatistics();
    writer//
        .col("Project", path)//
        .col("Statements", statements())//
        .col("Coverage", coverage())//
        .col("Methods", methods())//
        .col("Touched", touched())//
        .col("R-Index", rMethod())//
        .col("Nanos adopted", adopted())//
        .col("Fmethods", fMethods())//
        .col("Fiteratives", fIteratives())//
        .col("FconditionalExps", fConditionalExpressions())//
        .col("FconditionalStmts", fConditionalStatements())//
        .nl();
  }

  private static int statements() {
    return totalStatements;
  }

  private static int methods() {
    return totalMethods;
  }

  private static double fMethods() {
    return getNodeCoverage(ASTNode.METHOD_DECLARATION);
  }

  private static double getNodeCoverage(final int type) {
    return npDistributionStatistics.coverage(type);
  }

  private static double fIteratives() {
    return getNodeCoverage(ASTNode.ENHANCED_FOR_STATEMENT);
  }

  private static double fConditionalExpressions() {
    return getNodeCoverage(ASTNode.CONDITIONAL_EXPRESSION);
  }

  private static double fConditionalStatements() {
    return getNodeCoverage(ASTNode.IF_STATEMENT);
  }

  private long adopted() {
    final int $ = rMethod();
    return npStatistics.keySet().stream()//
        .map(npStatistics::get)//
        .filter(λ -> λ.occurences > $).count();
  }

  private static double touched() {
    return format.perc(totalMethodsTouched, totalMethods);
  }

  private static double totalMethodsTouched(final Collection<MethodRecord> rs) {
    return rs.stream().filter(λ -> λ.numNPStatements > 0 || λ.numNPExpressions > 0).count();
  }

  private static double coverage() {
    return format.perc(totalStatementsCovered, totalStatements);
  }

  @SuppressWarnings("boxing") private static void gatherGeneralStatistics() {
    totalStatementsCovered = totalMethods = totalStatements = totalMethodsAfterSpartanization = 0;
    for (final Integer ¢ : statementsCoverageStatistics.keySet()) {
      final List<MethodRecord> rs = statementsCoverageStatistics.get(¢);
      totalStatements += ¢ * rs.size();
      totalMethods += rs.size();
      totalStatementsCovered += totalStatementsCovered(rs);
      totalMethodsTouched += totalMethodsTouched(rs);
    }
  }

  @SuppressWarnings("boxing") private static double totalStatementsCovered(final Collection<MethodRecord> rs) {
    return rs.stream().map(λ -> λ.numNPStatements).reduce((x, y) -> x + y).get();
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
