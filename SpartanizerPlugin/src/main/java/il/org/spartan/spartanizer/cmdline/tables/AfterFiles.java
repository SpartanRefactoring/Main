package il.org.spartan.spartanizer.cmdline.tables;

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
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates files after nano spartanization+replacing
 * @author Ori Marcovitch
 * @since Dec 14, 2016 */
public class AfterFiles extends FolderASTVisitor {
  private static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private final Stack<MethodRecord> scope = new Stack<>();
  private final SortedMap<Integer, List<MethodRecord>> methods = new TreeMap<>(Integer::compareTo);
  static {
    clazz = AfterFiles.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    // wizard.setParserResolveBindings();
    TrimmerLog.off();
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
      final MethodDeclaration after = findFirst.instanceOf(MethodDeclaration.class)
          .in(wizard.ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
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
    Count.print();
    System.err.println("Generating output files...");
    writeFile(new File(makeFile("after.java")),
        methods.values().stream().map(li -> li.stream().map(λ -> format.code(λ.after + "")).reduce("", (x, y) -> x + y)).reduce("", (x, y) -> x + y));
    writeFile(new File(makeFile("notTagged.java")), methods.values().stream().map(list -> list.stream().map(λ -> λ.after)
        .filter(λ -> !(javadoc(λ) + "").contains("[[")).map(λ -> format.code(λ + "")).reduce("", (x, y) -> x + y)).reduce("", (x, y) -> x + y));
    summarizeSortedMethodStatistics(path);
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

  @SuppressWarnings("boxing") private void summarizeSortedMethodStatistics(final String path) {
    try (Table report = new Table(path)) {
      int statementsTotal = 0, methodsTotal = 0;
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
            .col("#Statements [before]", numStatements) //
            .col("Count", li.size()) //
            .col("Coverage [Avg.]", format.decimal(100 * avgCoverage(li)))//
            .col("Methods (%)", format.decimal(100 * fractionOfMethods(methodsTotal, li))) //
            .col("Statements (%)", format.decimal(100 * fractionOfStatements(statementsTotal, numStatements, li))) //
            .col("Touched (%)", format.decimal(100 * fractionOfMethodsTouched(li))) //
        ;
        report.nl();
      }
    }
  }

  private static double fractionOfMethodsTouched(final Collection<MethodRecord> rs) {
    return safe.div(rs.stream().filter(λ -> λ.numNPStatements > 0 || λ.numNPExpressions > 0).count(), rs.size());
  }

  private static double fractionOfStatements(final int statementsTotal, final Integer numStatements, final Collection<MethodRecord> rs) {
    return safe.div(rs.size() * numStatements.intValue(), statementsTotal);
  }

  private static double fractionOfMethods(final int methodsTotal, final Collection<MethodRecord> rs) {
    return safe.div(rs.size(), methodsTotal);
  }

  @SuppressWarnings("boxing") private static double avgCoverage(final Collection<MethodRecord> rs) {
    return safe.div(rs.stream().map(λ -> min(1, safe.div(λ.numNPStatements, λ.numStatements))).reduce((x, y) -> x + y).get(), rs.size());
  }

  public static CSVStatistics openMethodSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/methodStatistics");
  }

  private static CSVStatistics openNPSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/npStatistics.csv");
  }

  private static CSVStatistics openSummaryFile(final String $) {
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

  private void summarizeNPStatistics() {
    final CSVStatistics report = openNPSummaryFile(outputFolder);
    if (report == null)
      return;
    npStatistics.keySet().stream()
        .sorted((k1, k2) -> npStatistics.get(k1).occurences < npStatistics.get(k2).occurences ? 1
            : npStatistics.get(k1).occurences > npStatistics.get(k2).occurences ? -1 : 0)
        .map(npStatistics::get)//
        .forEach(λ -> {
          report //
              .put("Name", λ.name) //
              .put("Type", λ.className) //
              .put("occurences", λ.occurences) //
              .put("Statements", λ.numNPStatements) //
              .put("Expressions", λ.numNPExpressions) //
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
    return yieldAncestors.untilClass(ClassInstanceCreation.class).from(¢) != null;
  }
}
