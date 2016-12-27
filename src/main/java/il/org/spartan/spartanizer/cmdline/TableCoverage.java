package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
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
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
  static {
    clazz = TableCoverage.class;
    TrimmerLog.off();
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
    // System.out.println(packageDeclaration(¢) + " " + name(first(types(¢))));
    return true;
  }

  @Override protected void init(@SuppressWarnings("unused") final String __) {
    System.err.println("Processing: " + presentSourcePath);
  }

  @Override protected void done(final String path) {
    summarizeSortedMethodStatistics(path);
    statementsCoverageStatistics.clear();
    scope.clear();
    System.err.println("Coverage output is in: " + presentSourcePath);
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
    int totalStatements = 0;
    int totalStatementsCovered = 0;
    coverageWriter.put("Project", path);
    for (int i = 0; i < MAX_STATEMENTS_REPORTED; ++i)
      if (!statementsCoverageStatistics.containsKey(i))
        coverageWriter.put(i + "", "-");
      else {
        final List<MethodRecord> rs = statementsCoverageStatistics.get(i);
        totalStatements += i * rs.size();
        totalStatementsCovered += totalStatementsCovered(rs);
        coverageWriter.put(i + "", format.decimal(100 * avgCoverage(rs)));
      }
    coverageWriter.put("total Statements covergae %", format.decimal(100 * safe.div(totalStatementsCovered, totalStatements)));
    coverageWriter.nl();
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
