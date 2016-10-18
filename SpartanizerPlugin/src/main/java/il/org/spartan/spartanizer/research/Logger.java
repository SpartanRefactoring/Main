package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The purpose of this class is to gather information about NPs and summarize
 * it, so we can submit nice papers and win eternal fame.
 * <p>
 * Whenever an NP is matched it should log itself.
 * @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static final Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();
  private static final Map<String, NpRecord> npStatistics = new HashMap<>();
  private static int numMethods;

  public static void summarize(final String outputDir) {
    final CSVStatistics report = openSummaryFile(outputDir);
    if (report == null)
      return;
    summarizeMethodStatistics(report);
    report.close();
    reset();
  }

  private static void summarizeMethodStatistics(final CSVStatistics report) {
    double sumSratio = 0;
    double sumEratio = 0;
    for (final Integer k : methodsStatistics.keySet()) {
      final MethodRecord m = methodsStatistics.get(k);
      reportMethod(report, m);
      sumSratio += m.numStatements == 0 ? 1 : m.numNPStatements / m.numStatements;
      sumEratio += m.numExpressions == 0 ? 1 : m.numNPExpressions / m.numExpressions;
    }
    System.out.println("Total methods number: " + numMethods);
    System.out.println("Average statement ratio: " + sumSratio / numMethods);
    System.out.println("Average Expression ratio: " + sumEratio / numMethods);
  }

  private static void reportMethod(final CSVStatistics report, final MethodRecord r) {
    report //
        .put("Name", r.methodClassName + "~" + r.methodName) //
        .put("#Statement", r.numStatements) //
        .put("#NP Statements", r.numNPStatements) //
        .put("Statement ratio", r.numStatements == 0 ? 1 : r.numNPStatements / r.numStatements) //
        .put("#Expressions", r.numExpressions) //
        .put("#NP expressions", r.numNPExpressions) //
        .put("Expression ratio", r.numExpressions == 0 ? 1 : r.numNPExpressions / r.numExpressions) //
        .put("#Parameters", r.numParameters) //
        .put("#NP", r.nps.size()) //
    ;
    report.nl();
  }

  public static CSVStatistics openSummaryFile(final String outputDir) {
    try {
      return new CSVStatistics(outputDir + "/report.csv", "property");
    } catch (final IOException x) {
      monitor.infoIOException(x, "opening report file");
      return null;
    }
  }

  private static void reset() {
    methodsStatistics.clear();
    numMethods = 0;
  }

  public static void logNP(final ASTNode n, final String np) {
    logMethodInfo(n, np);
    logNPInfo(n, np);
  }

  /** @param n
   * @param np */
  private static void logNPInfo(ASTNode n, String np) {
    if (!npStatistics.containsKey(np))
      npStatistics.put(np, new NpRecord(np));
    npStatistics.get(np).markNP(n);
  }

  private static void logMethodInfo(final ASTNode n, final String np) {
    final MethodDeclaration m = findMethodAncestor(n);
    final Integer key = Integer.valueOf(m.hashCode());
    if (!methodsStatistics.containsKey(key))
      methodsStatistics.put(key, new MethodRecord(m));
    methodsStatistics.get(key).markNP(n, np);
  }

  /** @param ¢
   * @return */
  private static MethodDeclaration findMethodAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    while (!iz.methodDeclaration(n))
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
      numStatements = metrics.countStatements(m);
      numExpressions = metrics.countExpressions(m);
    }

    /** @param n matched node
     * @param np matching nanopattern */
    public void markNP(final ASTNode n, final String np) {
      numNPStatements += metrics.countStatements(n);
      numNPExpressions += metrics.countExpressions(n);
      nps.add(np);
    }
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param cu compilation unit */
  public static void logCompilationUnit(final ASTNode cu) {
    numMethods += metrics.countMethods(cu);
  }

  /** Collects statistics for a nanopattern.
   * @author Ori Marcovitch
   * @since 2016 */
  static class NpRecord {
    final String name;
    int occurences;
    int numNPStatements;
    int numNPExpressions;

    /** @param name */
    public NpRecord(String name) {
      this.name = name;
    }

    /** @param ¢ matched node */
    public void markNP(ASTNode ¢) {
      ++occurences;
      numNPStatements += metrics.countStatements(¢);
      numNPExpressions += metrics.countExpressions(¢);
    }
  }
}