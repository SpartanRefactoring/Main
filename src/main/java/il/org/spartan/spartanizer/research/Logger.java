package il.org.spartan.spartanizer.research;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** The purpose of this class is to gather information about NPs and summarize
 * it, so we can submit nice papers and win eternal fame.
 * <p>
 * Whenever an NP is matched it should log itself.
 * @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static final Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();
  private static final Map<String, NanoPatternRecord> npStatistics = new HashMap<>();
  private static final Map<String, Int> nodesStatistics = new HashMap<>();
  private static int numMethods;
  private static String currentFile;
  private static Stack<AbstractTypeDeclaration> currentType = new Stack<>();
  private static List<BiConsumer<ASTNode, String>> subscribers = new ArrayList<>();

  public static void summarize(final String outputDir) {
    summarizeMethodStatistics(outputDir);
    summarizeNPStatistics(outputDir);
    reset();
  }

  /** subscribe to logNP. Every time an NP will hit, the subscriber will be
   * invoked.
   * @param ¢ */
  public static void subsribe(final BiConsumer<ASTNode, String> ¢) {
    subscribers.add(¢);
  }

  private static void summarizeMethodStatistics(final String outputDir) {
    final CSVStatistics report = openMethodSummaryFile(outputDir);
    if (report == null)
      return;
    double sumSratio = 0;
    double sumEratio = 0;
    for (final Integer k : methodsStatistics.keySet()) {
      final MethodRecord m = methodsStatistics.get(k);
      report //
          .put("Name", m.methodClassName + "~" + m.methodName) //
          .put("#Statement", m.numStatements) //
          .put("#NP Statements", m.numNPStatements) //
          .put("Statement ratio", m.numStatements == 0 ? 1 : min(1, safeDiv(m.numNPStatements, m.numStatements))) //
          .put("#Expressions", m.numExpressions) //
          .put("#NP expressions", m.numNPExpressions) //
          .put("Expression ratio", m.numExpressions == 0 ? 1 : min(1, safeDiv(m.numNPExpressions, m.numExpressions))) //
          .put("#Parameters", m.numParameters) //
          .put("#NP", m.nps.size()) //
      ;
      report.nl();
      sumSratio += m.numStatements == 0 ? 1 : min(1, safeDiv(m.numNPStatements, m.numStatements));
      sumEratio += m.numExpressions == 0 ? 1 : min(1, safeDiv(m.numNPExpressions, m.numExpressions));
    }
    System.out.println("Total methods: " + numMethods);
    System.out.println("Average statement ratio: " + safeDiv(sumSratio, numMethods));
    System.out.println("Average Expression ratio: " + safeDiv(sumEratio, numMethods));
    report.close();
  }

  @SuppressWarnings("boxing") public static void summarizeSortedMethodStatistics(final String outputDir) {
    final CSVStatistics report = openMethodSummaryFile(outputDir);
    if (report == null)
      return;
    final Map<Integer, List<Double>> ratioMap = new HashMap<>();
    int statementsTotal = 0;
    int methodsTotal = 0;
    for (final MethodRecord m : methodsStatistics.values()) {
      final int key = m.numStatements;
      if (key == 0)
        continue; // don't count methods without body
      ++methodsTotal;
      statementsTotal += key;
      ratioMap.putIfAbsent(key, new ArrayList<>());
      ratioMap.get(key).add(key == 0 ? 1 : min(1, safeDiv(m.numNPStatements, m.numStatements)));
    }
    final NumberFormat formatter = new DecimalFormat("#0.00");
    for (final Integer k : ratioMap.keySet().stream().sorted((x, y) -> x < y ? -1 : x > y ? 1 : 0).collect(Collectors.toList())) {
      final List<Double> li = ratioMap.get(k);
      report //
          .put("num. Statements", k) //
          .put("Count", li.size()) //
          .put("Coverage [Avg.]", formatter.format(100 * safeDiv(li.stream().reduce((x, y) -> x + y).get(), li.size())))//
          .put("perc. of methods", formatter.format(100 * safeDiv(li.size(), methodsTotal))) //
          .put("perc. of statements", formatter.format(100 * safeDiv(k * li.size(), statementsTotal))) //
          .put("perc. touched", formatter.format(100 * safeDiv(li.stream().filter(x -> x > 0).count(), li.size()))) //
      ;
      report.nl();
    }
    report.close();
    renameToCSV(outputDir + "/methodStatistics");
  }

  private static void renameToCSV(final String old) {
    file.rename(old, old + ".csv");
  }

  /** Divide but if b == 0 return 1.
   * @param sumSratio
   * @param d
   * @return */
  private static double safeDiv(final double sumSratio, final double d) {
    return d == 0 ? 1 : sumSratio / d;
  }

  public static void summarizeNPStatistics(final String outputDir) {
    final CSVStatistics report = openNPSummaryFile(outputDir);
    if (report == null)
      return;
    npStatistics.keySet().stream()
        .sorted((k1, k2) -> npStatistics.get(k1).occurences < npStatistics.get(k2).occurences ? 1
            : npStatistics.get(k1).occurences > npStatistics.get(k2).occurences ? -1 : 0)
        .map(k -> npStatistics.get(k))//
        .forEach(n -> {
          report //
              .put("Name", n.name) //
              .put("Type", n.className).put("occurences", n.occurences)//
              .put("Statements", n.numNPStatements) //
              .put("Expressions", n.numNPExpressions) //
          ;
          report.nl();
        });
    report.close();
    file.rename(outputDir + "/npStatistics", outputDir + "/npStatistics.csv");
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

  private static void reset() {
    methodsStatistics.clear();
    numMethods = 0;
  }

  public static void logNP(final ASTNode n, final String np) {
    subscribers.stream().forEach(¢ -> ¢.accept(n, np));
    logNanoContainingMethodInfo(n, np);
    logNPInfo(n, np);
  }

  /** @param n
   * @param np */
  private static void logNPInfo(final ASTNode n, final String np) {
    npStatistics.putIfAbsent(np, new NanoPatternRecord(np, n.getClass()));
    npStatistics.get(np).markNP(n);
  }

  /** @param ¢
   * @param np */
  static void logNodeInfo(final ASTNode ¢) {
    final String nodeClassName = ¢.getClass().getSimpleName();
    nodesStatistics.putIfAbsent(nodeClassName, new Int());
    ++nodesStatistics.get(nodeClassName).inner;
  }

  private static void logNanoContainingMethodInfo(final ASTNode n, final String np) {
    final MethodDeclaration m = findAncestorMethod(n);
    if (m == null) {
      System.out.println("OMG: node [" + n + "] without a mother method!");
      return;
    }
    final Integer key = hashMethod(m);
    methodsStatistics.putIfAbsent(key, new MethodRecord(m));
    methodsStatistics.get(key).markNP(n, np);
  }

  private static void logMethodInfo(final MethodDeclaration ¢) {
    methodsStatistics.putIfAbsent(hashMethod(¢), new MethodRecord(¢));
  }

  private static Integer hashMethod(final MethodDeclaration ¢) {
    return Integer.valueOf((currentFile + "." + getType() + name(¢) + parametersTypes(¢)).hashCode());
  }

  private static String getType() {
    return currentType == null || currentType.isEmpty() ? "" : currentType.peek() + "";
  }

  /** @param ¢
   * @return */
  private static MethodDeclaration findAncestorMethod(final ASTNode ¢) {
    ASTNode $ = ¢;
    while (!iz.methodDeclaration($) && $ != null)
      $ = $.getParent();
    return az.methodDeclaration($);
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param ¢ compilation unit */
  public static void logCompilationUnit(final CompilationUnit ¢) {
    currentType = new Stack<>();
    searchDescendants.forClass(AbstractTypeDeclaration.class).from(¢).stream().filter(haz::methods).forEach(Logger::logType);
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param u compilation unit */
  public static void logType(final AbstractTypeDeclaration d) {
    currentType.push(d);
    final List<MethodDeclaration> ms = step.methods(d).stream().filter(m -> enumerate.statements(m) != 0 && !m.isConstructor())
        .collect(Collectors.toList());
    for (final MethodDeclaration ¢ : ms)
      logMethodInfo(¢);
    numMethods += ms.size();
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param ¢ compilation unit */
  public static void logFile(final String fileName) {
    currentFile = fileName;
  }

  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }

  public static void finishedType() {
    currentType.pop();
  }
}