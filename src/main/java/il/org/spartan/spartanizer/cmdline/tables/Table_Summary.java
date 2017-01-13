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

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
public class Table_Summary extends TableReusabilityIndices {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final NanoPatternsStatistics npStatistics = new NanoPatternsStatistics();
  private static final NanoPatternsDistributionStatistics npDistributionStatistics = new NanoPatternsDistributionStatistics();
  private static final Stack<MethodRecord> scope = new Stack<>();
  private static Table writer; // coverage
  private static int totalStatements;
  protected static int totalMethods;
  private static int totalStatementsCovered;
  private static int totalMethodsTouched;
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
  static {
    clazz = Table_Summary.class;
    Logger.subscribe((n, np) -> logNanoContainingMethodInfo(n, np));
    Logger.subscribe((n, np) -> npStatistics.logNPInfo(n, np));
    Logger.subscribe((n, np) -> npDistributionStatistics.logNPInfo(n, np));
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
      final MethodDeclaration d = findFirst.methodDeclaration(ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
      if (d != null)
        npDistributionStatistics.logMethod(d);
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
    System.err.println("Outcol is in: " + Table.temporariesFolder + path);
  }

  private static void clearAll() {
    statementsCoverageStatistics.clear();
    npDistributionStatistics.clear();
    npStatistics.clear();
    scope.clear();
    totalMethodsTouched = totalStatementsCovered = totalMethods = totalStatements = 0;
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || extract.annotations(¢).stream().anyMatch(x -> "@Test".equals(x + ""));
  }

  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    if (!containedInInstanceCreation(n))
      scope.peek().markNP(n, np);
  }

  private static void initializeWriter() {
    writer = new Table(Table_Summary.class.getSimpleName());
  }

  public void summarizeSortedMethodStatistics(final String path) {
    if (writer == null)
      initializeWriter();
    gatherGeneralStatistics();
    writer.col("Project", path);
    writer.col("Statements", statements());
    writer.col("Coverage", coverage());
    writer.col("Methods", methods());
    writer.col("Touched", touched());
    writer.col("R-Index", rMethod());
    writer.col("Nanos adopted", adopted());
    writer.col("Fmethods", fMethods());
    writer.col("Fiteratives", fIteratives());
    writer.col("FconditionalExps", fConditionalExpressions());
    writer.col("FconditionalStmts", fConditionalStatements());
    writer.nl();
  }

  private static int statements() {
    return totalStatements;
  }

  private static int methods() {
    return totalMethods;
  }

  private static Double fMethods() {
    return getNodeCoverage(ASTNode.METHOD_DECLARATION);
  }

  @SuppressWarnings("boxing") private static Double getNodeCoverage(final int type) {
    return Double.valueOf(format.decimal(100 * npDistributionStatistics.coverage(type)));
  }

  private static Double fIteratives() {
    return getNodeCoverage(ASTNode.ENHANCED_FOR_STATEMENT);
  }

  private static Double fConditionalExpressions() {
    return getNodeCoverage(ASTNode.CONDITIONAL_EXPRESSION);
  }

  private static Double fConditionalStatements() {
    return getNodeCoverage(ASTNode.IF_STATEMENT);
  }

  /** [[SuppressWarningsSpartan]] */
  private long adopted() {
    final int $ = rMethod();
    return npStatistics.keySet().stream()//
        .map(k -> npStatistics.get(k))//
        .filter(n -> n.occurences > $).count();
  }

  private static String touched() {
    return format.decimal(100 * safe.div(totalMethodsTouched, totalMethods));
  }

  private static double totalMethodsTouched(final List<MethodRecord> rs) {
    return rs.stream().filter(x -> x.numNPStatements > 0 || x.numNPExpressions > 0).count();
  }

  private static String coverage() {
    return format.decimal(100 * safe.div(totalStatementsCovered, totalStatements));
  }

  @SuppressWarnings("boxing") private static void gatherGeneralStatistics() {
    totalStatementsCovered = totalMethods = totalStatements = 0;
    for (final Integer ¢ : statementsCoverageStatistics.keySet()) {
      final List<MethodRecord> rs = statementsCoverageStatistics.get(¢);
      totalStatements += ¢ * rs.size();
      totalMethods += rs.size();
      totalStatementsCovered += totalStatementsCovered(rs);
      totalMethodsTouched += totalMethodsTouched(rs);
    }
  }

  @SuppressWarnings("boxing") private static double totalStatementsCovered(final List<MethodRecord> rs) {
    return rs.stream().map(x -> x.numNPStatements).reduce((x, y) -> x + y).get();
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return searchAncestors.forClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
