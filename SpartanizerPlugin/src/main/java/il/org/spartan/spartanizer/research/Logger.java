package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.utils.tdd.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** The purpose of this class is to gather information about NPs and summarize
 * it, so we can submit nice papers and win eternal fame.
 * <p>
 * Whenever an NP is matched it should log itself.
 * @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static final Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();
  private static final Map<String, NPRecord> npStatistics = new HashMap<>();
  private static final Map<String, Int> nodesStatistics = new HashMap<>();
  private static int numMethods;
  private static String currentFile;
  private static AbstractTypeDeclaration currentType;

  public static void summarize(final String outputDir) {
    summarizeMethodStatistics(outputDir);
    summarizeNPStatistics(outputDir);
    reset();
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
    Map<Integer, List<Double>> ratioMap = new HashMap<>();
    int statementsTotal = 0;
    int methodsTotal = 0;
    for (MethodRecord m : methodsStatistics.values()) {
      int key = m.numStatements;
      if (key == 0)
        continue; // don't count methods without body
      ++methodsTotal;
      statementsTotal += key;
      ratioMap.putIfAbsent(key, new ArrayList<>());
      ratioMap.get(key).add(key == 0 ? 1 : min(1, safeDiv(m.numNPStatements, m.numStatements)));
    }
    for (final Integer k : ratioMap.keySet().stream().sorted((x, y) -> x < y ? -1 : x > y ? 1 : 0).collect(Collectors.toList())) {
      final List<Double> li = ratioMap.get(k);
      report //
          .put("#Statement", k) //
          .put("count", li.size()) //
          .put("Statement ratio", safeDiv(li.stream().reduce((x, y) -> x + y).get(), li.size()))//
          .put("#methods fraction", safeDiv(li.size(), methodsTotal)) //
          .put("#statements fraction", safeDiv(k * li.size(), statementsTotal)) //
      ;
      report.nl();
    }
    report.close();
    file.rename(outputDir + "/methodStatistics", outputDir + "/methodStatistics.csv");
  }

  /** Divide but if b == 0 return 1.
   * @param sumSratio
   * @param d
   * @return */
  private static double safeDiv(final double sumSratio, final double d) {
    return d == 0 ? 1 : sumSratio / d;
  }

  private static void summarizeNPStatistics(final String outputDir) {
    final CSVStatistics report = openNPSummaryFile(outputDir);
    if (report == null)
      return;
    for (final String k : npStatistics.keySet()) {
      final NPRecord n = npStatistics.get(k);
      report //
          .put("Name", n.name) //
          .put("#Statement", n.numNPStatements) //
          .put("#Expression", n.numNPExpressions) //
      ;
      report.nl();
    }
    report.close();
  }

  public static CSVStatistics openMethodSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/methodStatistics");
  }

  public static CSVStatistics openNPSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/npStatistics.csv");
  }

  public static CSVStatistics openSummaryFile(final String fileName) {
    try {
      return new CSVStatistics(fileName, "property");
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
    logNanoContainingMethodInfo(n, np);
    logNPInfo(n, np);
    AnalyzerOptions.tickNP();
  }

  /** @param n
   * @param np */
  private static void logNPInfo(final ASTNode n, final String np) {
    npStatistics.putIfAbsent(np, new NPRecord(np, n.getClass()));
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
    final MethodDeclaration m = findMethodAncestor(n);
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
    return Integer.valueOf((currentFile + "." + currentType + name(¢) + parametersTypes(¢)).hashCode());
  }

  /** @param ¢
   * @return */
  private static MethodDeclaration findMethodAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    while (!iz.methodDeclaration(n) && n != null)
      n = n.getParent();
    return az.methodDeclaration(n);
  }

  /** @param ¢
   * @return */
  static String findTypeAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    String $ = "";
    while (n != null) {
      while (!iz.abstractTypeDeclaration(n) && n != null)
        n = n.getParent();
      if (n == null)
        break;
      $ += "." + az.abstractTypeDeclaration(n).getName();
      n = n.getParent();
    }
    return $.substring(1);
  }

  /** Collects statistics for a method in which a nanopattern was found.
   * @author Ori Marcovitch
   * @since 2016 */
  static class MethodRecord {
    public String methodName;
    public String methodClassName;
    public int numNPStatements;
    public int numNPExpressions;
    public List<String> nps = new ArrayList<>();
    public int numParameters;
    public int numStatements;
    public int numExpressions;

    public MethodRecord(final MethodDeclaration m) {
      methodName = m.getName() + "";
      methodClassName = findTypeAncestor(m);
      numParameters = m.parameters().size();
      numStatements = measure.statements(m);
      numExpressions = measure.expressions(m);
    }

    /** @param n matched node
     * @param np matching nanopattern */
    public void markNP(final ASTNode n, final String np) {
      numNPStatements += measure.statements(n);
      numNPExpressions += measure.expressions(n);
      nps.add(np);
      logNodeInfo(n);
    }
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param u compilation unit */
  public static void logCompilationUnit(final CompilationUnit u) {
    for (AbstractTypeDeclaration ¢ : searchDescendants.forClass(AbstractTypeDeclaration.class).from(u))
      logType(¢);
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param u compilation unit */
  public static void logType(final AbstractTypeDeclaration d) {
    currentType = d;
    for (MethodDeclaration ¢ : searchDescendants.forClass(MethodDeclaration.class).from(d))
      if (enumerate.statements(¢) != 0)
        logMethodInfo(¢);
    numMethods += enumerate.methodsWithBody(d);
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param ¢ compilation unit */
  public static void logFile(final String fileName) {
    currentFile = fileName;
  }

  /** Collects statistics for a nanopattern.
   * @author Ori Marcovitch
   * @since 2016 */
  static class NPRecord {
    final String name;
    int occurences;
    int numNPStatements;
    int numNPExpressions;
    final String className;

    /** @param name
     * @param cl */
    public NPRecord(final String name, final Class<? extends ASTNode> cl) {
      this.name = name;
      className = cl.getSimpleName();
    }

    /** @param ¢ matched node */
    public void markNP(final ASTNode ¢) {
      ++occurences;
      numNPStatements += measure.statements(¢);
      numNPExpressions += measure.expressions(¢);
    }
  }

  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }
}