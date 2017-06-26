package il.org.spartan.spartanizer.cmdline.tables.visitors;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.good.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

/** Table representing coverage for methods up to 30 statements
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-25 */
@Deprecated
public class TableNanosCoverage extends DeprecatedFolderASTVisitor {
  static final Nanonizer nanonizer = new Nanonizer();
  protected static final int MAX_STATEMENTS_REPORTED = 30;
  private static final Stack<MethodRecord> scope = new Stack<>();
  private static Table cWriter; // coverage
  private static int totalStatements;
  protected static int totalMethods;
  private static int totalStatementsCovered;
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>(Integer::compareTo);
  static {
    clazz = TableNanosCoverage.class;
    Logger.subscribe(TableNanosCoverage::logNanoContainingMethodInfo);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
  }
  @Override public boolean visit(final MethodDeclaration ¢) {
    if (excludeMethod(¢))
      return false;
    try {
      final Integer key = Integer.valueOf(measure.commands(¢));
      statementsCoverageStatistics.putIfAbsent(key, an.empty.list());
      final MethodRecord m = new MethodRecord(¢);
      scope.push(m);
      statementsCoverageStatistics.get(key).add(m);
      nanonizer.fixedPoint(WrapIntoComilationUnit.Method.on(¢ + ""));
    } catch (final AssertionError __) {
      forget.em(__);
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
    statementsCoverageStatistics.clear();
    scope.clear();
    System.err.println("Output is in: " + system.tmp + path);
  }
  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || extract.annotations(¢).stream().anyMatch(λ -> "@Test".equals(λ + ""));
  }
  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    if (!containedInInstanceCreation(n))
      scope.peek().markNP(n, np);
  }
  private static void initializeWriter() {
    cWriter = new Table(TableNanosCoverage.class);
  }
  @SuppressWarnings("boxing") public static void summarizeSortedMethodStatistics(final String path) {
    if (cWriter == null)
      initializeWriter();
    gatherGeneralStatistics();
    cWriter.put("Project", path);
    for (int ¢ = 1; ¢ <= MAX_STATEMENTS_REPORTED; ++¢)
      cWriter.put(¢ + "",
          !statementsCoverageStatistics.containsKey(¢) ? 0 : Double.valueOf(format.decimal(100 * avgCoverage(statementsCoverageStatistics.get(¢)))));
    cWriter.put("total Statements coverage", format.decimal(100 * safe.div(totalStatementsCovered, totalStatements)));
    cWriter.nl();
  }
  @SuppressWarnings("boxing") private static void gatherGeneralStatistics() {
    totalStatementsCovered = totalMethods = totalStatements = 0;
    for (final Integer ¢ : statementsCoverageStatistics.keySet()) {
      final List<MethodRecord> rs = statementsCoverageStatistics.get(¢);
      totalStatements += ¢ * rs.size();
      totalMethods += rs.size();
      totalStatementsCovered += totalStatementsCovered(rs);
    }
  }
  @SuppressWarnings("boxing") private static double avgCoverage(final Collection<MethodRecord> rs) {
    return safe.div(rs.stream().map(λ -> min(1, safe.div(λ.numNPStatements(), λ.numStatements))).reduce((x, y) -> x + y).get(), rs.size());
  }
  @SuppressWarnings("boxing") private static double totalStatementsCovered(final Collection<MethodRecord> rs) {
    return rs.stream().map(MethodRecord::numNPStatements).reduce((x, y) -> x + y).get();
  }
  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }
  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
