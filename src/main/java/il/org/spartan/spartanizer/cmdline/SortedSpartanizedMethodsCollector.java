package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** @author Ori Marcovitch
 * @since Dec 14, 2016 */
public class SortedSpartanizedMethodsCollector extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private final Stack<MethodRecord> scope = new Stack<>();
  private final SortedMap<Integer, List<MethodRecord>> methods = new TreeMap<>(Integer::compareTo);
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
      final Integer key = Integer.valueOf(measure.statements(¢));
      methods.putIfAbsent(key, new ArrayList<>());
      final MethodRecord m = new MethodRecord(¢);
      scope.push(m);
      methods.get(key).add(m);
      final MethodDeclaration after = findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
      Count.after(after);
      m.after = after;
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

  @Override protected void init(final String path) {
    System.err.println("Processing: " + path);
    Logger.subscribe(this::logAll);
  }

  @Override protected void done(final String path) {
    dotter.line();
    System.err.println("Done processing: " + path);
    System.err.println("Wait for output files...");
    writeFile(new File(makeFile("after.java")),
        methods.values().stream().map(li -> li.stream().map(x -> format.code(x.after + "")).reduce("", (x, y) -> x + y)).reduce("", (x, y) -> x + y));
    writeFile(new File(makeFile("notTagged.java")), methods.values().stream().map(
        li -> li.stream().map(m -> m.after).filter(m -> !(javadoc(m) + "").contains("[[")).map(x -> format.code(x + "")).reduce("", (x, y) -> x + y))
        .reduce("", (x, y) -> x + y));
    summarizeSortedMethodStatistics();
    summarizeNPStatistics();
    dotter.end();
    System.err.println("Your output is in: " + outputFolder);
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }

  private void logAll(final ASTNode n, final String np) {
    if (containedInInstanceCreation(n))
      return;
    logNanoContainingMethodInfo(n, np);
    logNPInfo(n, np);
  }

  private void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    scope.peek().markNP(n, np);
  }

  @SuppressWarnings("boxing") public void summarizeSortedMethodStatistics() {
    final CSVStatistics report = openMethodSummaryFile(outputFolder);
    if (report == null)
      return;
    int statementsTotal = 0;
    int methodsTotal = 0;
    for (final Integer numStatements : methods.keySet()) {
      if (numStatements == 0)
        continue; // don't count methods without body
      final List<MethodRecord> li = methods.get(numStatements);
      methodsTotal += li.size();
      statementsTotal += numStatements * li.size();
    }
    for (final Integer numStatements : methods.keySet()) {
      if (numStatements == 0)
        continue;
      final List<MethodRecord> li = methods.get(numStatements);
      report //
          .put("num. Statements [before]", numStatements) //
          .put("Count", li.size()) //
          .put("Coverage [Avg.]", format.decimal(100 * avgCoverage(li)))//
          .put("perc. of methods", format.decimal(100 * fractionOfMethods(methodsTotal, li))) //
          .put("perc. of statements", format.decimal(100 * fractionOfStatements(statementsTotal, numStatements, li))) //
          .put("perc. touched", format.decimal(100 * fractionOfMethodsTouched(li))) //
      ;
      report.nl();
    }
    report.close();
    file.renameToCSV(outputFolder + "/methodStatistics");
  }

  private static double fractionOfMethodsTouched(final List<MethodRecord> rs) {
    return safe.div(rs.stream().filter(x -> x.numNPStatements > 0 || x.numNPExpressions > 0).count(), rs.size());
  }

  private static double fractionOfStatements(final int statementsTotal, final Integer numStatements, final List<MethodRecord> rs) {
    return safe.div(rs.size() * numStatements.intValue(), statementsTotal);
  }

  private static double fractionOfMethods(final int methodsTotal, final List<MethodRecord> rs) {
    return safe.div(rs.size(), methodsTotal);
  }

  @SuppressWarnings("boxing") private static double avgCoverage(final List<MethodRecord> rs) {
    return safe.div(rs.stream().map(x -> min(1, safe.div(x.numNPStatements, x.numStatements))).reduce((x, y) -> x + y).get(), rs.size());
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

  private final Map<String, NanoPatternRecord> npStatistics = new HashMap<>();

  private void logNPInfo(final ASTNode n, final String np) {
    if (!npStatistics.containsKey(np))
      npStatistics.put(np, new NanoPatternRecord(np, n.getClass()));
    npStatistics.get(np).markNP(n);
  }

  public void summarizeNPStatistics() {
    final CSVStatistics report = openNPSummaryFile(outputFolder);
    if (report == null)
      return;
    npStatistics.keySet().stream()
        .sorted((k1, k2) -> npStatistics.get(k1).occurences < npStatistics.get(k2).occurences ? 1
            : npStatistics.get(k1).occurences > npStatistics.get(k2).occurences ? -1 : 0)
        .map(npStatistics::get)//
        .forEach(n -> {
          report //
              .put("Name", n.name) //
              .put("Type", n.className) //
              .put("occurences", n.occurences) //
              .put("Statements", n.numNPStatements) //
              .put("Expressions", n.numNPExpressions) //
          ;
          report.nl();
        });
    report.close();
    file.renameToCSV(outputFolder + "/npStatistics");
  }

  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }

  private static boolean containedInInstanceCreation(final ASTNode ¢) {
    return searchAncestors.forClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
