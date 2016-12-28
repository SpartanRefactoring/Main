package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
public class TableCoverage extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  protected static final int MAX_STATEMENTS_REPORTED = 30;
  private static final Stack<MethodRecord> scope = new Stack<>();
  private static Relation coverageWriter;
  private static int totalStatements;
  protected static int totalMethods;
  private static int totalStatementsCovered;
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
  static {
    clazz = TableCoverage.class;
    TrimmerLog.off();
    Trimmer.silent = true;
    Logger.subscribe((n, np) -> logNanoContainingMethodInfo(n, np));
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
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
      findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
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
    // System.out.println(packageDeclaration(¢) + " " +
    // name(lisp.first(types(¢))));
    return true;
  }

  @Override protected void done(final String path) {
    summarizeSortedMethodStatistics(path);
    statementsCoverageStatistics.clear();
    scope.clear();
    System.err.println("Output is in: " + Relation.temporariesFolder + path);
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || extract.annotations(¢).stream().anyMatch(x -> "@Test".equals(x + ""));
  }

  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    if (!containedInInstanceCreation(n))
      scope.peek().markNP(n, np);
  }

  private static void initializeWriter() {
    coverageWriter = new Relation(TableCoverage.class.getSimpleName());
  }

  @SuppressWarnings("boxing") public static void summarizeSortedMethodStatistics(final String path) {
    if (coverageWriter == null)
      initializeWriter();
    gatherGeneralStatistics();
    coverageWriter.put("Project", path);
    for (int ¢ = 1; ¢ <= MAX_STATEMENTS_REPORTED; ++¢)
      coverageWriter.put(¢ + "",
          !statementsCoverageStatistics.containsKey(¢) ? "-" : format.decimal(100 * avgCoverage(statementsCoverageStatistics.get(¢))));
    coverageWriter.put("total Statements coverage", format.decimal(100 * safe.div(totalStatementsCovered, totalStatements)));
    coverageWriter.nl();
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

  @SuppressWarnings("boxing") private static double avgCoverage(final List<MethodRecord> rs) {
    return safe.div(rs.stream().map(x -> min(1, safe.div(x.numNPStatements, x.numStatements))).reduce((x, y) -> x + y).get(), rs.size());
  }

  @SuppressWarnings("boxing") private static double totalStatementsCovered(final List<MethodRecord> rs) {
    return rs.stream().map(x -> x.numNPStatements).reduce((x, y) -> x + y).get();
  }

  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return searchAncestors.forClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
