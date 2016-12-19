package il.org.spartan.spartanizer.cmdline.collector;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** @author Ori Marcovitch
 * @since Dec 14, 2016 */
public class SortedSpartanizedMethodsCollector extends FolderASTVisitor {
  static SpartAnalyzer spartanizer = new SpartAnalyzer();
  private final Stack<MethodRecord> scope = new Stack<>();
  SortedMap<Integer, List<MethodRecord>> methods = new TreeMap<>(new Comparator<Integer>() {
    @Override public int compare(Integer o1, Integer o2) {
      return o1.compareTo(o2);
    }
  });
  static {
    clazz = SortedSpartanizedMethodsCollector.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    wizard.setParserResolveBindings();
    FolderASTVisitor.main(args);
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    if (excludeMethod(¢))
      return false;
    Count.before(¢);
    try {
      Integer key = Integer.valueOf(measure.statements(¢));
      methods.putIfAbsent(key, new ArrayList<>());
      MethodRecord m = new MethodRecord(¢);
      scope.push(m);
      methods.get(key).add(m);
      final MethodDeclaration after = findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
      Count.after(after);
      m.after = after;
    } catch (final AssertionError __) {
      ___.unused(__);
    }
    return true;
  }

  @Override public void endVisit(MethodDeclaration ¢) {
    if (!excludeMethod(¢))
      scope.pop();
  }

  @Override public void endVisit(TypeDeclaration ¢) {
    if (haz.methods(¢))
      Logger.finishedType();
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    Logger.logCompilationUnit(¢);
    ¢.accept(new CleanerVisitor());
    return true;
  }

  @Override public boolean visit(final TypeDeclaration ¢) {
    if (!haz.methods(¢))
      return false;
    Logger.logType(¢);
    return true;
  }

  @Override protected void init(String path) {
    System.err.println("Processing: " + path);
    Logger.subsribe((n, np) -> logNanoContainingMethodInfo(n, np));
  }

  @Override protected void done(String path) {
    dotter.line();
    System.err.println("Done processing: " + path);
    System.err.println("Wait for output files...");
    writeFile(new File(makeFile("after.java")),
        methods.values().stream().map(li -> li.stream().map(x -> format.code(x.after + "")).reduce("", (x, y) -> x + y)).reduce("", (x, y) -> x + y));
    writeFile(new File(makeFile("notTagged.java")), methods.values().stream().map(
        li -> li.stream().map(m -> m.after).filter(m -> !(javadoc(m) + "").contains("[[")).map(x -> format.code(x + "")).reduce("", (x, y) -> x + y))
        .reduce("", (x, y) -> x + y));
    summarizeSortedMethodStatistics(outputFolder);
    // Logger.summarizeNPStatistics(outputFolder);
    dotter.end();
    System.err.println("Your output is in: " + outputFolder);
  }

  private static boolean excludeMethod(MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }

  private void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    scope.peek().markNP(n, np);
  }

  @SuppressWarnings("boxing") public void summarizeSortedMethodStatistics(final String outputDir) {
    final CSVStatistics report = openMethodSummaryFile(outputDir);
    if (report == null)
      return;
    int statementsTotal = 0;
    int methodsTotal = 0;
    for (final Integer numStatements : methods.keySet()) {
      if (numStatements == 0)
        continue; // don't count methods without body
      List<MethodRecord> li = methods.get(numStatements);
      methodsTotal += li.size();
      statementsTotal += numStatements * li.size();
    }
    for (final Integer numStatements : methods.keySet()) {
      List<MethodRecord> li = methods.get(numStatements);
      report //
          .put("num. Statements", numStatements) //
          .put("Count", li.size()) //
          .put("Coverage [Avg.]", format.decimal(100 * avgCoverage(li)))//
          .put("perc. of methods", format.decimal(100 * fractionOfMethods(methodsTotal, li))) //
          .put("perc. of statements", format.decimal(100 * fractionOfStatements(statementsTotal, numStatements, li))) //
          .put("perc. touched", format.decimal(100 * fractionOfMethodsTouched(li))) //
      ;
      report.nl();
    }
    report.close();
    file.renameToCSV(outputDir + "/methodStatistics");
  }

  private static double fractionOfMethodsTouched(List<MethodRecord> rs) {
    return safe.div(rs.stream().filter(x -> x.numNPStatements > 0 || x.numNPExpressions > 0).count(), rs.size());
  }

  private static double fractionOfStatements(int statementsTotal, final Integer numStatements, List<MethodRecord> rs) {
    return safe.div(rs.size() * numStatements.intValue(), statementsTotal);
  }

  private static double fractionOfMethods(int methodsTotal, List<MethodRecord> rs) {
    return safe.div(rs.size(), methodsTotal);
  }

  @SuppressWarnings("boxing") private static double avgCoverage(List<MethodRecord> rs) {
    return safe.div(rs.stream().map(x -> safe.div(x.numNPStatements, x.numStatements)).reduce((x, y) -> x + y).get(), rs.size());
  }

  public static CSVStatistics openMethodSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/methodStatistics");
  }

  public static CSVStatistics openNPSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/npStatistics.csv");
  }

  public static CSVStatistics openSummaryFile(final String $) {
    try {
      return new CSVStatistics($, "property");
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "opening report file");
      return null;
    }
  }
}
