package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-27 */
public class Table1To3Statements extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  protected static final int MIN_STATEMENTS_REPORTED = 1;
  protected static final int MAX_STATEMENTS_REPORTED = 3;
  private static final Stack<MethodRecord> scope = new Stack<>();
  private static Table writer;
  protected static final SortedMap<Integer, List<MethodRecord>> statementsCoverageStatistics = new TreeMap<>(Integer::compareTo);
  private static int totalStatements;
  private static int totalMethods;
  private static int totalStatementsCovered;
  static {
    clazz = Table1To3Statements.class;
    TrimmerLog.off();
    Trimmer.silent = true;
    Logger.subscribe(Table1To3Statements::logNanoContainingMethodInfo);
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
      findFirst.instanceOf(MethodDeclaration.class).in(wizard.ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
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
    statementsCoverageStatistics.clear();
    scope.clear();
    System.err.println("Output is in: " + Table.temporariesFolder + path);
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || extract.annotations(¢).stream().anyMatch(λ -> "@Test".equals(λ + ""));
  }

  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    if (!containedInInstanceCreation(n))
      scope.peek().markNP(n, np);
  }

  private static void initializeWriter() {
    writer = new Table(Table1To3Statements.class.getSimpleName());
  }

  @SuppressWarnings("boxing") public static void summarizeSortedMethodStatistics(final String path) {
    if (writer == null)
      initializeWriter();
    gatherGeneralStatistics();
    writer.put("Project", path);
    for (int i = MIN_STATEMENTS_REPORTED; i <= MAX_STATEMENTS_REPORTED; ++i)
      if (!statementsCoverageStatistics.containsKey(i))
        writer.col(i + " Count", "-")//
            .col(i + "perc. of methods", 0)//
            .col(i + " perc. of statements", 0)//
            .col(i + " perc. touched", 100);
      else {
        final List<MethodRecord> rs = statementsCoverageStatistics.get(i);
        writer.col(i + " Count", rs.size()).col(i + " Coverage", format.decimal(100 * avgCoverage(rs)))//
            .col(i + "perc. of methods", format.decimal(100 * fractionOfMethods(totalMethods, rs)))//
            .col(i + " perc. of statements", format.decimal(100 * fractionOfStatements(totalStatements, i, rs)))//
            .col(i + " perc. touched", format.decimal(100 * fractionOfMethodsTouched(rs)));
      }
    writer.col("total Statements covergae ", format.decimal(100 * safe.div(totalStatementsCovered, totalStatements)));
    writer.nl();
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
    return safe.div(rs.stream().map(λ -> min(1, safe.div(λ.numNPStatements, λ.numStatements))).reduce((x, y) -> x + y).get(), rs.size());
  }

  private static double fractionOfMethodsTouched(final List<MethodRecord> rs) {
    return safe.div(rs.stream().filter(λ -> λ.numNPStatements > 0 || λ.numNPExpressions > 0).count(), rs.size());
  }

  private static double fractionOfStatements(final int statementsTotal, final Integer numStatements, final List<MethodRecord> rs) {
    return safe.div(rs.size() * numStatements.intValue(), statementsTotal);
  }

  private static double fractionOfMethods(final int methodsTotal, final List<MethodRecord> rs) {
    return safe.div(rs.size(), methodsTotal);
  }

  @SuppressWarnings("boxing") private static double totalStatementsCovered(final List<MethodRecord> rs) {
    return rs.stream().map(λ -> λ.numNPStatements).reduce((x, y) -> x + y).get();
  }

  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
